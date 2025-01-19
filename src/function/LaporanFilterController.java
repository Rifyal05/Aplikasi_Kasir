package function;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class LaporanFilterController {

    public static void filterData(JTable Tbl, String pilihanFilter) {
        try {
            Connection conn = koneksi_database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs;

            LaporanTableModel model = (LaporanTableModel) Tbl.getModel();
            model.clearData();

            switch (pilihanFilter) {
                case "PER-HARI":
                    rs = stmt.executeQuery("SELECT tanggal, kasir, SUM(produk_terjual) AS total_transaksi, SUM(total_pemasukan) AS total_pemasukan " +
                            "FROM laporan_keuangan GROUP BY tanggal, kasir ORDER BY tanggal, kasir");
                    break;
                case "PER-MINGGU":
                    rs = stmt.executeQuery("SELECT YEARWEEK(tanggal, 3) AS minggu, kasir, SUM(produk_terjual) AS total_transaksi, SUM(total_pemasukan) AS total_pemasukan " +
                            "FROM laporan_keuangan GROUP BY minggu, kasir ORDER BY minggu, kasir");
                    break;
                case "PER-BULAN":
                    rs = stmt.executeQuery("SELECT DATE_FORMAT(tanggal, '%Y-%m') AS bulan, kasir, SUM(produk_terjual) AS total_transaksi, SUM(total_pemasukan) AS total_pemasukan " +
                            "FROM laporan_keuangan GROUP BY bulan, kasir ORDER BY bulan, kasir");
                    break;
                case "BERDASARKAN KASIR":
                    rs = stmt.executeQuery("SELECT tanggal, kasir, SUM(produk_terjual) AS total_transaksi, SUM(total_pemasukan) AS total_pemasukan " +
                            "FROM laporan_keuangan GROUP BY kasir, tanggal ORDER BY kasir, tanggal");
                    break;
                case "BERDASARKAN KASIR (TOTAL)":
                    rs = stmt.executeQuery("SELECT kasir, SUM(produk_terjual) AS total_transaksi, SUM(total_pemasukan) AS total_pemasukan " +
                            "FROM laporan_keuangan GROUP BY kasir ORDER BY kasir");
                    break;
                default:
                    return;
            }

            while (rs.next()) {
                Object[] rowData;
                if (pilihanFilter.equals("PER-MINGGU")) {
                    String awalMinggu = LaporanFilter.getAwalMinggu(rs.getString("minggu"));
                    String akhirMinggu = LaporanFilter.getAkhirMinggu(rs.getString("minggu"));
                    String rentangTanggal = awalMinggu + " sampai " + akhirMinggu;
                    rowData = new Object[]{rentangTanggal, rs.getString("kasir"), rs.getInt("total_transaksi"), rs.getDouble("total_pemasukan")};
                } else if (pilihanFilter.equals("PER-BULAN")) {
                    String awalBulan = LaporanFilter.getAwalBulan(rs.getString("bulan"));
                    String akhirBulan = LaporanFilter.getAkhirBulan(rs.getString("bulan"));
                    String rentangTanggal = awalBulan + " sampai " + akhirBulan;
                    rowData = new Object[]{rentangTanggal, rs.getString("kasir"), rs.getInt("total_transaksi"), rs.getDouble("total_pemasukan")};
                } else if (pilihanFilter.equals("BERDASARKAN KASIR (TOTAL)")) {
                    rowData = new Object[]{null, rs.getString("kasir"), rs.getInt("total_transaksi"), rs.getDouble("total_pemasukan")}; 
                } else {
                    rowData = new Object[]{rs.getDate("tanggal"), rs.getString("kasir"), rs.getInt("total_transaksi"), rs.getDouble("total_pemasukan")};
                }
                model.addRow(rowData);
            }

            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}