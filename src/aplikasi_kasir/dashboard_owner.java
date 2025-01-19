/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package aplikasi_kasir;

import java.sql.Statement;
import function.GrafikPendapatanController;
import function.GrafikPenjualanController;
import function.KinerjaKasirController;
import function.User;
import function.UserController;
import function.koneksi_database;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author rifial
 */
@SuppressWarnings("serial")
public class dashboard_owner extends javax.swing.JFrame {

    private User user;

    private void custompendapatanActionPerformed(java.awt.event.ActionEvent evt) {
        String pilihanWaktu = (String) custompendapatan.getSelectedItem();
        GrafikPendapatanController.tampilkanGrafikPendapatan(grafikpendapatan, pilihanWaktu);
    }

    /**
     * Creates new form dashboard_owner
     *
     * @param user
     */
    public dashboard_owner(User user) {
        initComponents();

        buttonubahpassword.addActionListener((java.awt.event.ActionEvent evt) -> {
            buttonubahpasswordActionPerformed(evt);
        });
        tampilkanDaftarNota();
        tampilkanDaftarLog();
        filtertabelgrafik.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"HARI INI", "MINGGU INI", "BULAN INI"}));

        filtertabelgrafik.addActionListener((java.awt.event.ActionEvent evt) -> {
            // Di ActionListener filtertabelgrafik
            String pilihanWaktu = (String) filtertabelgrafik.getSelectedItem();
            KinerjaKasirController.filterDataKinerjaKasir(tabelkinerjakasir, panelgrafikkinerjakasir, pilihanWaktu); // Hapus parameter false
        });

        KinerjaKasirController.tampilkanDataKinerjaKasir(tabelkinerjakasir, panelgrafikkinerjakasir, "HARI INI", true);  // pilihanWaktu = "HARI INI"
//        KinerjaKasirController.tampilkanDataKinerjaKasir(tabelkinerjakasir, panelgrafikkinerjakasir, false);

        tabellog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabellog.rowAtPoint(e.getPoint());
                int col = tabellog.columnAtPoint(e.getPoint());
                if (col == 1) { // Kolom "FILE LOG"
                    String fileName = (String) tabellog.getValueAt(row, col);
                    try {
                        // Buka file log
                        Desktop.getDesktop().open(new File("src/log/Log_Activity/" + fileName));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(dashboard_owner.this, "Error: " + ex.getMessage());
                    }
                }
            }
        });

        tabelnota.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelnota.rowAtPoint(e.getPoint());
                int col = tabelnota.columnAtPoint(e.getPoint());
                if (col == 1) { // Kolom "FILE NOTA"
                    String fileName = (String) tabelnota.getValueAt(row, col);
                    try {
                        // Buka file PDF
                        Desktop.getDesktop().open(new File("src/doc/" + fileName)); // Ganti dengan direktori nota kamu
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(dashboard_owner.this, "Error: " + ex.getMessage());
                    }
                }
            }
        });

        buttonubahusername.addActionListener((java.awt.event.ActionEvent evt) -> {
            buttonubahusernameActionPerformed(evt);
        });

        try {
            // 1. Koneksi ke database
            Connection conn = koneksi_database.getConnection();
            Statement stmt = conn.createStatement();

            // 2. Query untuk mengambil total produk terjual hari ini
            ResultSet rs = stmt.executeQuery("SELECT SUM(produk_terjual) AS total FROM laporan_keuangan WHERE DATE(tanggal) = CURDATE()");

            // 3. Tampilkan total produk terjual di label
            if (rs.next()) {
                int totalProdukTerjual = rs.getInt("total");
                produkterjual.setText(String.valueOf(totalProdukTerjual));
            }

            // 4. Tutup koneksi database
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        try {
            // 1. Koneksi ke database
            Connection conn = koneksi_database.getConnection();
            Statement stmt = conn.createStatement();

            // 2. Query untuk mengambil total pendapatan hari ini
            ResultSet rs = stmt.executeQuery("SELECT SUM(total_pendapatan) AS total FROM riwayat_penjualan WHERE DATE(tanggal) = CURDATE()");

            // 3. Tampilkan total pendapatan di label
            if (rs.next()) {
                double totalPendapatan = rs.getDouble("total");
                pendapatan.setText(String.valueOf(totalPendapatan));
            }

            // 4. Tutup koneksi database
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
        grafikpenjualanproduk.setLayout(new BorderLayout());
        grafikpenjualanproduk.setPreferredSize(new Dimension(1000, 300));

        custompendapatan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"PER-HARI", "PER-MINGGU", "PER-BULAN"}));

        custompendapatan.addActionListener((java.awt.event.ActionEvent evt) -> {
            custompendapatanActionPerformed(evt);
        });

        GrafikPendapatanController.tampilkanGrafikPendapatan(grafikpendapatan, "PER-HARI");
        updateWaktu();
        grafikpendapatan.revalidate();
        grafikpendapatan.repaint();

        GrafikPenjualanController.tampilkanGrafikPenjualan(grafikpenjualanproduk); // Penting!
        grafikpenjualanproduk.revalidate();
        grafikpenjualanproduk.repaint();

        Timer timerAnimasiPenjualan = new Timer(150000000, (ActionEvent e) -> {
            String pilihanWaktuPendapatan = (String) custompendapatan.getSelectedItem();
            GrafikPendapatanController.tampilkanGrafikPendapatan(grafikpendapatan, pilihanWaktuPendapatan);

        });

        Timer timerRefreshGrafik = new Timer(15000000, new ActionListener() { // Refresh setiap 5 detik
            @Override
            public void actionPerformed(ActionEvent e) {
                GrafikPenjualanController.tampilkanGrafikPenjualan(grafikpenjualanproduk);
            }
        });
        timerRefreshGrafik.restart();
        timerAnimasiPenjualan.start();

        Timer timer = new Timer(0, (ActionEvent e) -> {

            updateWaktu();
        });
        timer.start();
        this.user = user;

        this.user = user;
        System.out.println(user);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        System.out.println("User: " + user);
        if (user != null) {

            String namaUppercase = user.getNama().toUpperCase();
            jLabel2.setText(namaUppercase);

        } else {
            jLabel2.setText("NULL");
        }

        if (user != null) {
            jLabel2.setText(user.getNama().toUpperCase());
        } else {
            jLabel2.setText("NULL");
        }

        if (user != null) {

            tampilkanFotoProfil(user.getUsername());
            int id = user.getIdUser();
            String nama = user.getNama().toUpperCase();
            String username = user.getUsername().toUpperCase();
            String level = user.getLevelAkses().toUpperCase();
            iduser.setText("" + id);
            this.nama.setText("" + nama);
            this.username.setText("" + username);
            levelakses.setText("" + level);

        } else {
            jLabel5.setText("Error: Data user tidak ditemukan.");
        }
        // Tambahkan TableModelListener di sini
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void tampilkanFotoProfil(String username) {
        try (Connection conn = koneksi_database.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT foto FROM pengguna WHERE Username = ?")) {

            stmt.setString(1, username);
            System.out.println("Query: SELECT foto FROM pengguna WHERE Username = " + username); // Debug
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Data ditemukan di ResultSet"); // Debug
                    byte[] imageData = rs.getBytes("foto");
                    System.out.println("imageData: " + imageData); // Debug
                    if (imageData != null) {
                        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
                        try {
                            BufferedImage image = ImageIO.read(bais);
                            if (image != null) {

                                int lebar = 151; // Contoh lebar 100 pixel
                                int tinggi = 151; // Contoh tinggi 100 pixel

                                // Resize gambar
                                Image scaledImage = image.getScaledInstance(lebar, tinggi, Image.SCALE_SMOOTH);

                                ImageIcon imageIcon = new ImageIcon(scaledImage);
                                LabelFoto.setIcon(imageIcon);

                            } else {
                                // Handle jika gambar tidak valid
                                LabelFoto.setText("Gambar tidak valid");
                                System.err.println("Error: Gambar tidak valid");
                            }
                        } catch (IOException e) {
                            LabelFoto.setText("Error membaca gambar");
                            System.err.println("Error membaca gambar: " + e.getMessage());
                        }
                    } else {
                        // Handle jika gambar tidak ditemukan di database
                        LabelFoto.setText("Gambar tidak tersedia");
                        System.err.println("Error: Gambar tidak tersedia di database");
                    }
                } else {
                    System.out.println("Data tidak ditemukan di ResultSet"); // Debug
                    LabelFoto.setText("Data user tidak ditemukan");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error SQL: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labelwaktu = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        LabelFoto = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        iduser = new javax.swing.JLabel();
        nama = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        levelakses = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        password = new javax.swing.JTextField();
        usernamelama = new javax.swing.JTextField();
        passwordbaru = new javax.swing.JTextField();
        usernamebaru = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        buttonubahusername = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        logout = new javax.swing.JButton();
        buttonubahpassword = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        produkterjual = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        pendapatan = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelnota = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabellog = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        grafikpenjualanproduk = new javax.swing.JPanel();
        grafikpendapatan = new javax.swing.JPanel();
        custompendapatan = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        panelgrafikkinerjakasir = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelkinerjakasir = new javax.swing.JTable();
        filtertabelgrafik = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("DASHBOARD KASIRKU");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("USER APP");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("SELAMAT DATANG ");

        labelwaktu.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        labelwaktu.setText("WAKTU");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelwaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(323, 323, 323)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(labelwaktu))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(153, 153, 255));

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "USER PROFIL", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N
        jPanel4.setForeground(new java.awt.Color(51, 51, 51));
        jPanel4.setToolTipText("");
        jPanel4.setPreferredSize(new java.awt.Dimension(632, 404));

        LabelFoto.setBackground(new java.awt.Color(0, 0, 0));
        LabelFoto.setForeground(new java.awt.Color(0, 0, 0));
        LabelFoto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        LabelFoto.setMaximumSize(new java.awt.Dimension(151, 151));
        LabelFoto.setMinimumSize(new java.awt.Dimension(151, 151));
        LabelFoto.setPreferredSize(new java.awt.Dimension(151, 151));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("NAMA");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("USERNAME");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("ID USER");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("LEVEL AKSES");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("  :");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("  :");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("  :");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("  :");

        iduser.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        iduser.setForeground(new java.awt.Color(0, 0, 0));
        iduser.setText("ID USER");

        nama.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        nama.setForeground(new java.awt.Color(0, 0, 0));
        nama.setText("NAMA");

        username.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        username.setForeground(new java.awt.Color(0, 0, 0));
        username.setText("USERNAME");

        levelakses.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        levelakses.setForeground(new java.awt.Color(0, 0, 0));
        levelakses.setText("LEVEL AKSES");

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));
        jPanel6.setForeground(new java.awt.Color(51, 51, 51));
        jPanel6.setPreferredSize(new java.awt.Dimension(460, 5));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("UBAH FOTO");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        password.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        password.setText("MASUKKAN PASSWORD LAMA");
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordFocusLost(evt);
            }
        });
        password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordActionPerformed(evt);
            }
        });

        usernamelama.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        usernamelama.setText("MASUKKAN USERNAME LAMA");
        usernamelama.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernamelamaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                usernamelamaFocusLost(evt);
            }
        });
        usernamelama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernamelamaActionPerformed(evt);
            }
        });

        passwordbaru.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        passwordbaru.setText("MASUKKAN PASSWORD BARU");
        passwordbaru.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordbaruFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordbaruFocusLost(evt);
            }
        });
        passwordbaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordbaruActionPerformed(evt);
            }
        });

        usernamebaru.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        usernamebaru.setText("MASUKKAN USERNAME BARU");
        usernamebaru.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernamebaruFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                usernamebaruFocusLost(evt);
            }
        });
        usernamebaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernamebaruActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("UBAH PASSWORD");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("UBAH USERNAME");

        buttonubahusername.setBackground(new java.awt.Color(0, 0, 0));

        buttonubahusername.setFont(new java.awt.Font("Segoe UI", 1, 12));

        buttonubahusername.setForeground(new java.awt.Color(255, 255, 255));

        buttonubahusername.setText("UBAH USERNAME");

        jLabel18.setBackground(new java.awt.Color(0, 0, 0));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("TOKOKU");

        jLabel19.setBackground(new java.awt.Color(0, 0, 0));
        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("EMPLOYEE CARD");

        logout.setBackground(new java.awt.Color(0, 0, 0));
        logout.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setText("LOG OUT");

        buttonubahpassword.setBackground(new java.awt.Color(0, 0, 0));
        buttonubahpassword.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        buttonubahpassword.setForeground(new java.awt.Color(255, 255, 255));
        buttonubahpassword.setText("UBAH PASSWORD");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel18)
                                .addGap(28, 28, 28))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(levelakses, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(iduser, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addGap(75, 75, 75))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordbaru, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(password, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(buttonubahpassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(usernamelama)
                            .addComponent(usernamebaru)
                            .addComponent(buttonubahusername, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
                        .addGap(20, 20, 20))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(iduser))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(nama)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel10)
                            .addComponent(username))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel11)
                            .addComponent(levelakses)))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(LabelFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(usernamelama, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(passwordbaru, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usernamebaru, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonubahusername)
                            .addComponent(logout)
                            .addComponent(buttonubahpassword)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("PRODUK TERJUAL HARI INI");

        produkterjual.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        produkterjual.setText("PRODUK");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("TOTAL PENDAPATAN HARI INI");

        pendapatan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        pendapatan.setText("PENDAPATAN");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(168, 168, 168)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(produkterjual, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(produkterjual)
                    .addComponent(jLabel14)
                    .addComponent(pendapatan))
                .addContainerGap())
        );

        jScrollPane1.setBackground(new java.awt.Color(204, 204, 255));
        jScrollPane1.setForeground(new java.awt.Color(204, 204, 255));

        tabelnota.setBackground(new java.awt.Color(204, 204, 255));
        tabelnota.setForeground(new java.awt.Color(0, 0, 0));
        tabelnota.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "TANGGAL", "FILE NOTA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabelnota);

        jScrollPane2.setBackground(new java.awt.Color(204, 204, 255));
        jScrollPane2.setForeground(new java.awt.Color(204, 204, 255));

        tabellog.setBackground(new java.awt.Color(204, 204, 255));
        tabellog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "TANGGAL", "FILE LOG"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tabellog);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setText("TABEL DAFTAR NOTA");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("TABEL LOG APLIKASI");

        grafikpenjualanproduk.setBackground(new java.awt.Color(204, 153, 255));

        javax.swing.GroupLayout grafikpenjualanprodukLayout = new javax.swing.GroupLayout(grafikpenjualanproduk);
        grafikpenjualanproduk.setLayout(grafikpenjualanprodukLayout);
        grafikpenjualanprodukLayout.setHorizontalGroup(
            grafikpenjualanprodukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 970, Short.MAX_VALUE)
        );
        grafikpenjualanprodukLayout.setVerticalGroup(
            grafikpenjualanprodukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );

        grafikpendapatan.setBackground(new java.awt.Color(204, 153, 255));

        javax.swing.GroupLayout grafikpendapatanLayout = new javax.swing.GroupLayout(grafikpendapatan);
        grafikpendapatan.setLayout(grafikpendapatanLayout);
        grafikpendapatanLayout.setHorizontalGroup(
            grafikpendapatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 951, Short.MAX_VALUE)
        );
        grafikpendapatanLayout.setVerticalGroup(
            grafikpendapatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );

        custompendapatan.setBackground(new java.awt.Color(153, 153, 255));
        custompendapatan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("PENJUALAN PRODUK SELAMA 3 BULAN TERAKHIR");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setText("LAPORAN KEUANGAN");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)))
                                .addContainerGap())
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(202, 202, 202)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel15)
                                .addGap(233, 233, 233))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(grafikpenjualanproduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel20)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(grafikpendapatan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(custompendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(custompendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grafikpenjualanproduk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(grafikpendapatan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("DASHBOARD UTAMA", jPanel2);

        jPanel3.setBackground(new java.awt.Color(153, 153, 255));
        jPanel3.setForeground(new java.awt.Color(153, 153, 225));

        panelgrafikkinerjakasir.setBackground(new java.awt.Color(255, 102, 255));

        javax.swing.GroupLayout panelgrafikkinerjakasirLayout = new javax.swing.GroupLayout(panelgrafikkinerjakasir);
        panelgrafikkinerjakasir.setLayout(panelgrafikkinerjakasirLayout);
        panelgrafikkinerjakasirLayout.setHorizontalGroup(
            panelgrafikkinerjakasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelgrafikkinerjakasirLayout.setVerticalGroup(
            panelgrafikkinerjakasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 708, Short.MAX_VALUE)
        );

        tabelkinerjakasir.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "KASIR", "PRODUK TERJUAL"
            }
        ));
        jScrollPane3.setViewportView(tabelkinerjakasir);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 751, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        filtertabelgrafik.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        filtertabelgrafik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtertabelgrafikActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel22.setText("FILTER TABEL DAN GRAFIK       :");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        jLabel23.setText("SELAMAT DATANG DI HALAMAN KINERJA KASIR");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addContainerGap(591, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelgrafikkinerjakasir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(filtertabelgrafik, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 27, 27))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filtertabelgrafik, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(panelgrafikkinerjakasir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("KINERJA KASIR", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        UserController userController = new UserController();

        // 2. Buka JFileChooser untuk memilih gambar
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {

                // 3. Ambil path file gambar yang dipilih
                String imagePath = fileChooser.getSelectedFile().getAbsolutePath();

                // 4. Ambil username pengguna yang sedang login
                String username = user.getUsername();

                // 5. Panggil method ubahFotoProfil dari UserController
                String resultUbahFoto = userController.ubahFotoProfil(username, imagePath);

                // 6. Tampilkan pesan sukses/error
                JOptionPane.showMessageDialog(this, resultUbahFoto);

                // --- Refresh tampilan foto profil ---
                if (resultUbahFoto.equals("Foto profil berhasil diubah!")) {
                    // 7a. Ambil foto terbaru dari database
                    byte[] foto = userController.getFotoProfil(username);
                    if (foto != null) {
                        // 7b. Ubah byte[] foto menjadi ImageIcon
                        ImageIcon imageIcon = new ImageIcon(foto);

                        // 7c. Resize ImageIcon menjadi 151x151 piksel
                        Image image = imageIcon.getImage().getScaledInstance(151, 151, java.awt.Image.SCALE_SMOOTH);
                        imageIcon = new ImageIcon(image);

                        // 7d. Set ikon JLabel dengan ImageIcon baru
                        LabelFoto.setIcon(imageIcon);

                        // 7e. Refresh tampilan JLabel
                        LabelFoto.revalidate();
                        LabelFoto.repaint();
                    } else {
                        // Handle jika foto tidak ditemukan di database
                        JOptionPane.showMessageDialog(this, "Gagal mengambil foto profil dari database", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                // --- End refresh foto profil ---

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


    }//GEN-LAST:event_jButton1ActionPerformed


    private void passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordActionPerformed

    }//GEN-LAST:event_passwordActionPerformed

    private void usernamebaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernamebaruActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernamebaruActionPerformed

    private void passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusGained
        String pass = password.getText();
        if (pass.equals("MASUKKAN PASSWORD LAMA")) {
            password.setText("");
        }
    }//GEN-LAST:event_passwordFocusGained

    private void usernamelamaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernamelamaFocusGained
        String username = usernamelama.getText();
        if (username.equals("MASUKKAN USERNAME LAMA")) {
            usernamelama.setText("");
        }
    }//GEN-LAST:event_usernamelamaFocusGained

    private void usernamelamaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernamelamaFocusLost
        String username = usernamelama.getText();
        if (username.equals("") || username.equals("MASUKKAN USERNAME LAMA")) {
            usernamelama.setText("MASUKKAN USERNAME LAMA");
        }
    }//GEN-LAST:event_usernamelamaFocusLost

    private void usernamelamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernamelamaActionPerformed

    }//GEN-LAST:event_usernamelamaActionPerformed

    private void usernamebaruFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernamebaruFocusGained
        String username = usernamebaru.getText();
        if (username.equals("MASUKKAN USERNAME BARU")) {
            usernamebaru.setText("");
        }
    }//GEN-LAST:event_usernamebaruFocusGained

    private void usernamebaruFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernamebaruFocusLost
        String username = usernamebaru.getText();
        if (username.equals("") || username.equals("MASUKKAN USERNAME BARU")) {
            usernamebaru.setText("MASUKKAN USERNAME BARU");
        }
    }//GEN-LAST:event_usernamebaruFocusLost

    private void passwordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusLost
        String pass = password.getText();
        if (pass.equals("") || pass.equals("MASUKKAN PASSWORD LAMA")) {
            password.setText("MASUKKAN PASSWORD LAMA");
        }
    }//GEN-LAST:event_passwordFocusLost

    private void passwordbaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordbaruActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordbaruActionPerformed

    private void passwordbaruFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordbaruFocusGained
        String pass = passwordbaru.getText();
        if (pass.equals("MASUKKAN PASSWORD BARU")) {
            passwordbaru.setText("");
        }
    }//GEN-LAST:event_passwordbaruFocusGained

    private void passwordbaruFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordbaruFocusLost
        String pass = passwordbaru.getText();
        if (pass.equals("") || pass.equals("MASUKKAN PASSWORD BARU")) {
            passwordbaru.setText("MASUKKAN PASSWORD BARU");
        }
    }//GEN-LAST:event_passwordbaruFocusLost

    private void buttonubahpasswordActionPerformed(java.awt.event.ActionEvent evt) {                                                   

        String passwordLama = password.getText();
        String passwordBaru = passwordbaru.getText();

        // 1. Validasi input
        if (passwordLama.isEmpty() || passwordBaru.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password lama dan baru harus diisi!");
            return;
        }

        // 2. Ambil username user yang sedang login
        String username = user.getUsername();

        // 3. Ubah password di database
        UserController userController = new UserController();
        String result = userController.ubahPassword(username, passwordLama, passwordBaru);

        // 4. Tampilkan pesan sukses/error
        if (result.equals("Password berhasil diubah!")) {  // Cek apakah password berhasil diubah
            JOptionPane.showMessageDialog(this, result);
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah password. " + result, "Error", JOptionPane.ERROR_MESSAGE);
        }


    }
    
    private void filtertabelgrafikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filtertabelgrafikActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filtertabelgrafikActionPerformed

    private void buttonubahusernameActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String usernameLama = usernamelama.getText();
        String usernameBaru = usernamebaru.getText();

        // 1. Validasi input
        if (usernameLama.isEmpty() || usernameBaru.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username lama dan baru harus diisi!");
            return;
        }

        // 2. Ambil username user yang sedang login
        String username = user.getUsername();

        // 3. Ubah username di database
        UserController userController = new UserController();
        String result = userController.ubahUsername(username, usernameLama, usernameBaru);

        // 4. Tampilkan pesan sukses/error
        JOptionPane.showMessageDialog(this, result);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(dashboard_owner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dashboard_owner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dashboard_owner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboard_owner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Login_Page loginPage = new Login_Page();
                loginPage.setVisible(true);
            }
        });
    }

    private void updateWaktu() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
        Date sekarang = new Date();
        String waktu = sdf.format(sekarang).toUpperCase();
        labelwaktu.setText(waktu);
    }

    private void tampilkanDaftarNota() {
        try {
            File folder = new File("src/doc");
            File[] listOfFiles = folder.listFiles();

            DefaultTableModel model = (DefaultTableModel) tabelnota.getModel();
            model.setRowCount(0);

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().endsWith(".pdf")) {
                        long lastModified = file.lastModified();
                        long sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);
                        if (lastModified < sevenDaysAgo) {
                            if (file.delete()) {
                                System.out.println("File " + file.getName() + " berhasil dihapus.");
                            } else {
                                System.out.println("File " + file.getName() + " gagal dihapus.");
                            }
                        } else {
                            Date lastModifiedDate = new Date(lastModified);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = sdf.format(lastModifiedDate);
                            model.addRow(new Object[]{formattedDate, file.getName()});
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void tampilkanDaftarLog() {
        try {
            File folder = new File("src/log/Log_Activity");
            File[] listOfFiles = folder.listFiles();

            DefaultTableModel model = (DefaultTableModel) tabellog.getModel();
            model.setRowCount(0);

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        long lastModified = file.lastModified();
                        long sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);
                        if (lastModified < sevenDaysAgo) {
                            if (file.delete()) {
                                System.out.println("File " + file.getName() + " berhasil dihapus.");
                            } else {
                                System.out.println("File " + file.getName() + " gagal dihapus.");
                            }
                        } else {
                            Date lastModifiedDate = new Date(lastModified);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = sdf.format(lastModifiedDate);
                            model.addRow(new Object[]{formattedDate, file.getName()});
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelFoto;
    private javax.swing.JButton buttonubahpassword;
    private javax.swing.JButton buttonubahusername;
    private javax.swing.JComboBox<String> custompendapatan;
    private javax.swing.JComboBox<String> filtertabelgrafik;
    private javax.swing.JPanel grafikpendapatan;
    private javax.swing.JPanel grafikpenjualanproduk;
    private javax.swing.JLabel iduser;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelwaktu;
    private javax.swing.JLabel levelakses;
    private javax.swing.JButton logout;
    private javax.swing.JLabel nama;
    private javax.swing.JPanel panelgrafikkinerjakasir;
    private javax.swing.JTextField password;
    private javax.swing.JTextField passwordbaru;
    private javax.swing.JLabel pendapatan;
    private javax.swing.JLabel produkterjual;
    private javax.swing.JTable tabelkinerjakasir;
    private javax.swing.JTable tabellog;
    private javax.swing.JTable tabelnota;
    private javax.swing.JLabel username;
    private javax.swing.JTextField usernamebaru;
    private javax.swing.JTextField usernamelama;
    // End of variables declaration//GEN-END:variables
}
