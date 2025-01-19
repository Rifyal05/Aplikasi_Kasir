package function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class LaporanController {

    public static void simpanDataLaporan(String kasir, double totalPemasukan, JTable daftarproduk) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = koneksi_database.getConnection();

            // 1. Hitung total qty dari daftarproduk
            int totalQty = 0;
            for (int i = 0; i < daftarproduk.getRowCount(); i++) {
                totalQty += Integer.parseInt(daftarproduk.getValueAt(i, 2).toString());
            }

            // 2. Ambil tanggal hari ini
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            // 3. Cek apakah sudah ada data di tabel laporan_keuangan dengan tanggal dan kasir yang sama
            String cekQuery = "SELECT * FROM laporan_keuangan WHERE tanggal = ? AND kasir = ?";
            stmt = conn.prepareStatement(cekQuery);
            stmt.setDate(1, sqlDate);
            stmt.setString(2, kasir);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // 4a. Jika data sudah ada, update produk_terjual dan total_pemasukan
                int produkTerjualLama = rs.getInt("produk_terjual");
                double totalPemasukanLama = rs.getDouble("total_pemasukan");
                int produkTerjualBaru = produkTerjualLama + totalQty;
                double totalPemasukanBaru = totalPemasukanLama + totalPemasukan;

                String updateQuery = "UPDATE laporan_keuangan SET produk_terjual = ?, total_pemasukan = ? WHERE tanggal = ? AND kasir = ?";
                stmt = conn.prepareStatement(updateQuery);
                stmt.setInt(1, produkTerjualBaru);
                stmt.setDouble(2, totalPemasukanBaru);
                stmt.setDate(3, sqlDate);
                stmt.setString(4, kasir);
                stmt.executeUpdate();
            } else {
                // 4b. Jika data belum ada, insert data baru
                String insertQuery = "INSERT INTO laporan_keuangan (tanggal, kasir, produk_terjual, total_pemasukan) VALUES (?, ?, ?, ?)";
                stmt = conn.prepareStatement(insertQuery);
                stmt.setDate(1, sqlDate);
                stmt.setString(2, kasir);
                stmt.setInt(3, totalQty);
                stmt.setDouble(4, totalPemasukan);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat menyimpan data laporan: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error saat menutup koneksi database: " + e.getMessage());
            }
        }
    }
}