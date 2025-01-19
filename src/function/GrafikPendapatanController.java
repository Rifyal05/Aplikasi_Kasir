package function;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.sql.*;
import java.text.SimpleDateFormat;

public class GrafikPendapatanController {

    private static DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private static WeakReference<JFreeChart> chartRef; // Gunakan WeakReference untuk JFreeChart
    private static WeakReference<ChartPanel> chartPanelRef; // Gunakan WeakReference untuk ChartPanel
    private static DefaultCategoryDataset tempDataset = new DefaultCategoryDataset(); // tempDataset sebagai variabel instance

    public static void tampilkanGrafikPendapatan(JPanel panelGrafik, String pilihanWaktu) {
        try (Connection conn = koneksi_database.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs;
            // 1. Ambil data dari database
            String query = "";
            switch (pilihanWaktu) {
                case "PER-HARI":
                    query = "SELECT tanggal, SUM(total_pemasukan) AS total_pendapatan "
                            + "FROM laporan_keuangan "
                            + "WHERE tanggal >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) "
                            + "GROUP BY tanggal "
                            + "ORDER BY tanggal DESC "
                            + "LIMIT 7";
                    break;
                case "PER-MINGGU":
                    query = "SELECT MIN(tanggal) AS tanggal_awal, MAX(tanggal) AS tanggal_akhir, SUM(total_pemasukan) AS total_pendapatan "
                            + "FROM laporan_keuangan "
                            + "WHERE tanggal >= DATE_SUB(CURDATE(), INTERVAL 8 WEEK) "
                            + "GROUP BY WEEK(tanggal) ORDER BY WEEK(tanggal)";
                    break;
                case "PER-BULAN":
                    query = "SELECT MIN(tanggal) AS tanggal_awal, MAX(tanggal) AS tanggal_akhir, SUM(total_pemasukan) AS total_pendapatan "
                            + "FROM laporan_keuangan "
                            + "WHERE tanggal >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) "
                            + "GROUP BY MONTH(tanggal) ORDER BY MONTH(tanggal)";
                    break;
                default:
                    return;
            }
            rs = stmt.executeQuery(query);

            // 2. Bersihkan tempDataset
            tempDataset.clear();

            // 3. Isi tempDataset dengan data dari database
            while (rs.next()) {
                switch (pilihanWaktu) {
                    case "PER-HARI":
                        tempDataset.addValue(rs.getDouble("total_pendapatan"), "Pendapatan", rs.getDate("tanggal").toString());
                        break;
                    case "PER-MINGGU":
                        Date tanggalAwalMinggu = rs.getDate("tanggal_awal");
                        Date tanggalAkhirMinggu = rs.getDate("tanggal_akhir");
                        SimpleDateFormat sdfMinggu = new SimpleDateFormat("yyyy-MM-dd");
                        String rentangTanggalMinggu = sdfMinggu.format(tanggalAwalMinggu) + " - " + sdfMinggu.format(tanggalAkhirMinggu);
                        tempDataset.addValue(rs.getDouble("total_pendapatan"), "Pendapatan", rentangTanggalMinggu);
                        break;
                    case "PER-BULAN":
                        Date tanggalAwalBulan = rs.getDate("tanggal_awal");
                        Date tanggalAkhirBulan = rs.getDate("tanggal_akhir");
                        SimpleDateFormat sdfBulan = new SimpleDateFormat("yyyy-MM-dd");
                        String rentangTanggalBulan = sdfBulan.format(tanggalAwalBulan) + " - " + sdfBulan.format(tanggalAkhirBulan);
                        tempDataset.addValue(rs.getDouble("total_pendapatan"), "Pendapatan", rentangTanggalBulan);
                        break;
                }
            }

            // 3. Reset dataset utama
            dataset.clear();

            // 4. Tambahkan data ke dataset utama secara bertahap dengan animasi
            Timer timerAnimasi = new Timer(120, new ActionListener() {
                int i = 0;
                double addedValue = 0;
                JFreeChart chart = null;
       
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (i < tempDataset.getColumnCount()) {
                        for (int j = 0; j < tempDataset.getRowCount(); j++) {
                            double totalValue = tempDataset.getValue(j, i).doubleValue();
                            double increment = totalValue / 5;
                            addedValue += increment;

                            if (addedValue >= totalValue) {
                                addedValue = totalValue;
                            }

                            dataset.setValue(addedValue, tempDataset.getRowKey(j), tempDataset.getColumnKey(i));

                            // Membuat warna ungu kebiruan
                            Color purpleBlue = new Color(90, 50, 180); // Sesuaikan nilai RGB untuk mendapatkan warna yang diinginkan

                            chart = ChartFactory.createBarChart(
                                    "Grafik Pendapatan",
                                    pilihanWaktu,
                                    "Pendapatan",
                                    dataset
                            );

                            CategoryPlot plot = chart.getCategoryPlot();
                            // Mengubah warna background plot menjadi ungu muda
                            plot.setBackgroundPaint(new Color(230, 220, 250)); // Warna ungu muda, sesuaikan sesuai keinginan

                            BarRenderer renderer = (BarRenderer) plot.getRenderer();
                            renderer.setBarPainter(new StandardBarPainter());
                            renderer.setItemMargin(0.2);

                            // Mengubah warna bar menjadi ungu kebiruan
                            renderer.setSeriesPaint(0, purpleBlue);

                            chartPanelRef = new WeakReference<>(new ChartPanel(chart)); // Gunakan WeakReference
                            panelGrafik.removeAll();
                            panelGrafik.setLayout(new BorderLayout());
                            panelGrafik.add(chartPanelRef.get(), BorderLayout.CENTER); // Gunakan chartPanelRef.get()
                            panelGrafik.revalidate();
                            panelGrafik.repaint();

                            if (addedValue == totalValue) {
                                i++;
                                addedValue = 0;
                            }
                        }
                    } else {
                        ((Timer) e.getSource()).stop();
                    }
                    chartRef = new WeakReference<>(chart); // Gunakan WeakReference
                    chart = null;
                    chartPanelRef = null;
                    
                }
            });
            timerAnimasi.start();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}