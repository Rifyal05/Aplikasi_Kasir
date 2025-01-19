package function;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

public class LaporanTableModel extends AbstractTableModel {

    private final String[] columnNames = {"TANGGAL", "KASIR", "PRODUK TERJUAL", "TOTAL PEMASUKAN"};
    private List<Object[]> data = new ArrayList<>();

    public LaporanTableModel() {
        loadData();
    }

    private void loadData() {
        try {
            Connection conn = koneksi_database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM laporan_keuangan");

            while (rs.next()) {
                Object[] rowData = {
                    rs.getDate("tanggal"),
                    rs.getString("kasir"),
                    rs.getInt("produk_terjual"),
                    rs.getDouble("total_pemasukan")
                };
                data.add(rowData);
            }

            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void clearData() {
        data.clear();
        fireTableDataChanged();
    }

    public void addRow(Object[] rowData) {
        data.add(rowData);
        fireTableDataChanged();
    }
}