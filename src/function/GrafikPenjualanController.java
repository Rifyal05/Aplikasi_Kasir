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

public class GrafikPenjualanController {

    private static final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    // Gunakan WeakReference untuk JFreeChart
    private static WeakReference<ChartPanel> chartPanelRef; // Gunakan WeakReference untuk ChartPanel
    private static final DefaultCategoryDataset tempDataset = new DefaultCategoryDataset(); // tempDataset sebagai variabel instance

    public static void tampilkanGrafikPenjualan(JPanel panelGrafik) {
        try (Connection conn = koneksi_database.getConnection();
             Statement stmt = conn.createStatement()) 
        {
            ResultSet rs;

            // 1. Ambil data dari database (3 bulan terakhir)
            String query = "SELECT nama_produk, SUM(total_terjual) AS total_terjual " +
                           "FROM riwayat_penjualan " +
                           "WHERE tanggal >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH) " +
                           "GROUP BY nama_produk " +
                           "ORDER BY total_terjual DESC";
            rs = stmt.executeQuery(query);

            // 2. Bersihkan tempDataset
            tempDataset.clear();

            // 3. Isi tempDataset dengan data dari database
            while (rs.next()) {
                tempDataset.addValue(rs.getDouble("total_terjual"), "Total Terjual", rs.getString("nama_produk"));
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

                            Color purpleBlue = new Color(90, 50, 180);

                            chart = ChartFactory.createBarChart(
                                    "Grafik Penjualan Produk",
                                    "Nama Produk",
                                    "Total Terjual",
                                    dataset
                            );
                            CategoryPlot plot = chart.getCategoryPlot();
                            plot.setBackgroundPaint(new Color(230, 220, 250));

                            BarRenderer renderer = (BarRenderer) plot.getRenderer();
                            renderer.setBarPainter(new StandardBarPainter());
                            renderer.setItemMargin(0.2);
                            renderer.setSeriesPaint(0, purpleBlue);

                            chartPanelRef = new WeakReference<>(new ChartPanel(chart)); // Gunakan WeakReference
                            panelGrafik.removeAll();
                            panelGrafik.setLayout(new BorderLayout());
                            panelGrafik.add(chartPanelRef.get(), BorderLayout.CENTER);
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

                    // Hapus referensi ke chart dan chartPanel setelah animasi selesai
                    WeakReference<JFreeChart> weakReference = new WeakReference<>(chart);
                    chart = null;
                    chartPanelRef = null;
                }
            });
            timerAnimasi.restart();
            panelGrafik.removeAll();
            

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
