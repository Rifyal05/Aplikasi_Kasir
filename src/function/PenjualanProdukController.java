/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package function;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PenjualanProdukController {

    public static void tampilkanDataPenjualanProduk(JTable tabel) {
        try {
            // 1. Koneksi ke database
            Connection conn = koneksi_database.getConnection();
            Statement stmt = conn.createStatement();

            // 2. Query untuk mengambil data penjualan produk 6 bulan terakhir dari tabel penjualan_produk
            String query = "SELECT nama_produk, SUM(jumlah_terjual) AS Jumlah_Terjual, SUM(total_pendapatan) AS Total_Pendapatan " +
                           "FROM penjualan_produk " +
                           "WHERE tanggal >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) " +
                           "GROUP BY nama_produk ORDER BY nama_produk";
            ResultSet rs = stmt.executeQuery(query);

            // 3. Buat model tabel
            DefaultTableModel model = new DefaultTableModel(new Object[]{"NAMA PRODUK", "JUMLAH TERJUAL", "TOTAL PENDAPATAN"}, 0);

            // 4. Tambahkan data ke model tabel
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("nama_produk"), rs.getInt("Jumlah_Terjual"), rs.getDouble("Total_Pendapatan")});
            }

            // 5. Tutup koneksi database
            conn.close();

            // 6. Set model tabel
            tabel.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}