package function;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

public class KinerjaKasirController {

    private static DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    public static void tampilkanDataKinerjaKasir(JTable tabel, JPanel panelGrafik, String pilihanWaktu, boolean animasi) {
        try {
            Connection conn = koneksi_database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT kasir, SUM(total_terjual) AS total_produk_terjual "
                    + "FROM riwayat_penjualan "
                    + "GROUP BY kasir ORDER BY kasir");

            DefaultTableModel model = (DefaultTableModel) tabel.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Object[] rowData = {
                        rs.getString("kasir"),
                        rs.getInt("total_produk_terjual")
                };
                model.addRow(rowData);
            }

            conn.close();

            // Ambil data untuk grafik
            DefaultCategoryDataset tempDataset = buatDatasetKinerjaKasir(pilihanWaktu);

            // Reset dataset utama
            dataset.clear();

            JFreeChart chart = buatGrafikKinerjaKasir(dataset);

            if (animasi) {
                // Animasi grafik
                Timer timerAnimasi = new Timer(120, new ActionListener() {
                    int i = 0;
                    double addedValue = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (i < tempDataset.getColumnCount()) {
                            for (int j = 0; j < tempDataset.getRowCount(); j++) {
                                double totalValue = tempDataset.getValue(j, i).doubleValue();
                                double increment = totalValue / 6;
                                addedValue += increment;

                                if (addedValue >= totalValue) {
                                    addedValue = totalValue;
                                }

                                dataset.setValue(addedValue, tempDataset.getRowKey(j), tempDataset.getColumnKey(i));

                                final JFreeChart chart = buatGrafikKinerjaKasir(dataset);

                                ChartPanel chartPanel = new ChartPanel(chart);
                                chartPanel.setPreferredSize(new Dimension(500, 800));
                                panelGrafik.removeAll();
                                panelGrafik.setLayout(new BorderLayout());
                                panelGrafik.add(chartPanel, BorderLayout.CENTER);
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
                    }
                });
                timerAnimasi.start();
            } else {
                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(500, 800));
                panelGrafik.removeAll();
                panelGrafik.setLayout(new BorderLayout());
                panelGrafik.add(chartPanel, BorderLayout.CENTER);
                panelGrafik.revalidate();
                panelGrafik.repaint();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private static DefaultCategoryDataset buatDatasetKinerjaKasir(String pilihanWaktu) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    try {
        Connection conn = koneksi_database.getConnection();
        Statement stmt = conn.createStatement();
        String query = "SELECT kasir, SUM(total_terjual) AS total_produk_terjual " +
                       "FROM riwayat_penjualan WHERE ";

        // Filter berdasarkan waktu
        switch (pilihanWaktu) {
            case "HARI INI":
                query += "DATE(tanggal) = CURDATE() ";
                break;
            case "MINGGU INI":
                query += "YEARWEEK(tanggal, 3) = YEARWEEK(CURDATE(), 3) ";
                break;
            case "BULAN INI":
                query += "MONTH(tanggal) = MONTH(CURDATE()) ";
                break;
        }

        query += "GROUP BY kasir ORDER BY kasir";

        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            dataset.addValue(rs.getDouble("total_produk_terjual"), "Produk Terjual", rs.getString("kasir"));
        }
        conn.close();
    } catch (SQLException e) {
        // ... (error handling)
    }
    return dataset;
}

    private static JFreeChart buatGrafikKinerjaKasir(DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createStackedBarChart(
                "Grafik Kinerja Kasir",
                "Kasir",
                "Total Produk Terjual",
                dataset
        );

        // --- Kustomisasi grafik ---
        CategoryPlot plot = chart.getCategoryPlot();

        // Hilangkan border dan garis grid
        plot.setOutlineVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        // Ubah warna background dan bar renderer
        plot.setBackgroundPaint(new Color(230, 220, 250));
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setSeriesPaint(0, new Color(90, 50, 180));  // Ubah warna batang

        // Tambahkan label data
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        // --- End kustomisasi ---

        return chart;
    }
    
    public static void filterDataKinerjaKasir(JTable tabel, JPanel panelGrafik, String pilihanWaktu) {
    try {
        Connection conn = koneksi_database.getConnection();
        Statement stmt = conn.createStatement();
        String query = "SELECT kasir, SUM(total_terjual) AS total_produk_terjual " +
                       "FROM riwayat_penjualan WHERE ";

        // Filter berdasarkan waktu
        switch (pilihanWaktu) {
            case "HARI INI":
                query += "DATE(tanggal) = CURDATE() ";
                break;
            case "MINGGU INI":
                query += "YEARWEEK(tanggal, 3) = YEARWEEK(CURDATE(), 3) ";
                break;
            case "BULAN INI":
                query += "MONTH(tanggal) = MONTH(CURDATE()) ";
                break;
        }

        query += "GROUP BY kasir ORDER BY kasir";

        ResultSet rs = stmt.executeQuery(query);
        DefaultTableModel model = (DefaultTableModel) tabel.getModel();
        model.setRowCount(0);
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("kasir"),
                rs.getInt("total_produk_terjual")
            });
        }
        conn.close();

        // Update grafik
        dataset.clear();

        // Ambil data untuk grafik
        DefaultCategoryDataset tempDataset = buatDatasetKinerjaKasir(pilihanWaktu);

        // Animasi grafik
        Timer timerAnimasi = new Timer(120, new ActionListener() {
            int i = 0;
            double addedValue = 0;
            JFreeChart chart = null;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (i < tempDataset.getColumnCount()) {
                    for (int j = 0; j < tempDataset.getRowCount(); j++) {
                        double totalValue = tempDataset.getValue(j, i).doubleValue();
                        double increment = totalValue / 6;
                        addedValue += increment;

                        if (addedValue >= totalValue) {
                            addedValue = totalValue;
                        }

                        dataset.setValue(addedValue, tempDataset.getRowKey(j), tempDataset.getColumnKey(i));

                        chart = buatGrafikKinerjaKasir(dataset);

                        ChartPanel chartPanel = new ChartPanel(chart);
                        chartPanel.setPreferredSize(new Dimension(500, 800));
                        panelGrafik.removeAll();
                        panelGrafik.setLayout(new BorderLayout());
                        panelGrafik.add(chartPanel, BorderLayout.CENTER);
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
            }
        });
        timerAnimasi.start();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}
}