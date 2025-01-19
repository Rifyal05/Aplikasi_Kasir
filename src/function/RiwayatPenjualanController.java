package function;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RiwayatPenjualanController {

    public static void tampilkanDataRiwayatPenjualan(JTable tabel) {
        try {
            Connection conn = koneksi_database.getConnection();
            Statement stmt = conn.createStatement();

            String query = "SELECT * FROM riwayat_penjualan";
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = (DefaultTableModel) tabel.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Object[] rowData = {
                    rs.getDate("tanggal"),
                    rs.getString("kasir"),
                    rs.getString("nama_produk"),
                    rs.getInt("total_terjual"),
                    rs.getDouble("total_pendapatan")
                };
                model.addRow(rowData);
            }

            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void simpanDataRiwayatPenjualan(JTable daftarproduk, String labelkasir) {
        Connection conn = null;
        PreparedStatement stmtRiwayat = null;
        try {
            conn = koneksi_database.getConnection();
            conn.setAutoCommit(false);

            DefaultTableModel model = (DefaultTableModel) daftarproduk.getModel();

            String queryRiwayat = "INSERT INTO riwayat_penjualan (tanggal, kasir, nama_produk, total_terjual, total_pendapatan) "
                    + "VALUES (?, ?, ?, ?, ?)";
            stmtRiwayat = conn.prepareStatement(queryRiwayat);
            for (int i = 0; i < model.getRowCount(); i++) {
                String namaProduk = model.getValueAt(i, 1).toString();
                int jumlahTerjual = Integer.parseInt(model.getValueAt(i, 2).toString());
                double totalPendapatan = Double.parseDouble(model.getValueAt(i, 3).toString()) * jumlahTerjual;

                stmtRiwayat.setDate(1, new java.sql.Date(new Date().getTime()));
                stmtRiwayat.setString(2, labelkasir);
                stmtRiwayat.setString(3, namaProduk);
                stmtRiwayat.setInt(4, jumlahTerjual);
                stmtRiwayat.setDouble(5, totalPendapatan);
                stmtRiwayat.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // ... (error handling) ...
                }
            }
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            if (stmtRiwayat != null) {
                try {
                    stmtRiwayat.close();
                } catch (SQLException ex) {
                    // ... (error handling) ...
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    // ... (error handling) ...
                }
            }
        }
    }

    public static void filterData(JTable tabel, String pilihanWaktu) {
        try {
            Connection conn = koneksi_database.getConnection();
            Statement stmt = conn.createStatement();
            String query = "SELECT ";

            switch (pilihanWaktu) {
                case "MINGGUAN" -> query += "YEARWEEK(tanggal, 3) AS minggu, ";
                case "BULANAN" -> query += "DATE_FORMAT(tanggal, '%Y-%m') AS bulan, ";
                default -> // "HARIAN"
                    query += "tanggal, ";
            }

            query += "kasir, nama_produk, SUM(total_terjual) AS total_terjual, SUM(total_pendapatan) AS total_pendapatan "
                    + "FROM riwayat_penjualan ";

            switch (pilihanWaktu) {
                case "MINGGUAN":
                    query += "GROUP BY minggu, kasir, nama_produk ";
                    break;
                case "BULANAN":
                    query += "GROUP BY bulan, kasir, nama_produk ";
                    break;
                default: // "HARIAN"
                    query += "GROUP BY tanggal, kasir, nama_produk ";
                    break;
            }

            ResultSet rs = stmt.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) tabel.getModel();
            model.setRowCount(0);

            switch (pilihanWaktu) {
                case "MINGGUAN" -> {
                    while (rs.next()) {
                        String awalMinggu = LaporanFilter.getAwalMinggu(rs.getString("minggu"));
                        String akhirMinggu = LaporanFilter.getAkhirMinggu(rs.getString("minggu"));
                        String rentangTanggal = awalMinggu + " sampai " + akhirMinggu;

                        model.addRow(new Object[]{
                            rentangTanggal,
                            rs.getString("kasir"),
                            rs.getString("nama_produk"),
                            rs.getInt("total_terjual"),
                            rs.getDouble("total_pendapatan")
                        });
                    }
                }
                case "BULANAN" -> {
                    while (rs.next()) {
                        String awalBulan = LaporanFilter.getAwalBulan(rs.getString("bulan"));
                        String akhirBulan = LaporanFilter.getAkhirBulan(rs.getString("bulan"));
                        String rentangTanggal = awalBulan + " sampai " + akhirBulan;

                        model.addRow(new Object[]{
                            rentangTanggal,
                            rs.getString("kasir"),
                            rs.getString("nama_produk"),
                            rs.getInt("total_terjual"),
                            rs.getDouble("total_pendapatan")
                        });
                    }
                }
                default -> {
                    // "HARIAN"
                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getDate("tanggal"),
                            rs.getString("kasir"),
                            rs.getString("nama_produk"),
                            rs.getInt("total_terjual"),
                            rs.getDouble("total_pendapatan")
                        });
                    }
                }
            }

            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void urutkanData(JTable tabel, String pilihanUrutan) {
        DefaultTableModel model = (DefaultTableModel) tabel.getModel();
        int rowCount = model.getRowCount();
        if (rowCount == 0) {
            return;
        }

        Object[][] data = new Object[rowCount][model.getColumnCount()];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                data[i][j] = model.getValueAt(i, j);
            }
        }

        Arrays.sort(data, (Object[] row1, Object[] row2) -> {
            switch (pilihanUrutan) {
                case "TOTAL TERJUAL TERENDAH" -> {
                    return Integer.compare((int) row1[3], (int) row2[3]);
                }
                case "TOTAL TERJUAL TERTINGGI" -> {
                    return Integer.compare((int) row2[3], (int) row1[3]);
                }
                case "TOTAL PENDAPATAN TERENDAH" -> {
                    return Double.compare((double) row1[4], (double) row2[4]);
                }
                case "TOTAL PENDAPATAN TERTINGGI" -> {
                    return Double.compare((double) row2[4], (double) row1[4]);
                }
                case "TANGGAL TERLAMA" -> {
                    if (row1[0] instanceof Date && row2[0] instanceof Date) {
                        return ((Date) row1[0]).compareTo((Date) row2[0]);
                    } else {
                        return ((String) row1[0]).compareTo((String) row2[0]);
                    }
                }
                case "TANGGAL TERBARU" -> {
                    if (row1[0] instanceof Date && row2[0] instanceof Date) {
                        return ((Date) row2[0]).compareTo((Date) row1[0]);
                    } else {
                        return ((String) row2[0]).compareTo((String) row1[0]);
                    }
                }
                case "KASIR" -> {
                    return ((String) row1[1]).compareTo((String) row2[1]);
                }
                case "NAMA PRODUK" -> {
                    return ((String) row1[2]).compareTo((String) row2[2]);
                }
                default -> {
                    return 0;
                }
            }
        });

        model.setRowCount(0);
        for (Object[] rowData : data) {
            model.addRow(rowData);
        }
    }
}
