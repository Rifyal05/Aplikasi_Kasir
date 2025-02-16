/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package aplikasi_kasir;

import function.LoginController;
import function.User;
import function.LaporanFilterController;
import function.LaporanTableModel;
import function.PenjualanProdukController;
import function.RiwayatPenjualanController;
import javax.swing.JOptionPane;
import unused.Profile;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Laporan_Keuangan_admin extends javax.swing.JFrame {

    private User user;

//    static DefaultTableModel m, mod_p;
    /**
     * Creates new form AdminPage
     */
    public Laporan_Keuangan_admin() {
        initComponents();

        filterwaktu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"HARIAN", "MINGGUAN", "BULANAN"}));
        filterurutan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"DEFAULT", "TOTAL TERJUAL TERENDAH", "TOTAL TERJUAL TERTINGGI", "TOTAL PENDAPATAN TERENDAH", "TOTAL PENDAPATAN TERTINGGI", "TANGGAL TERLAMA", "TANGGAL TERBARU", "KASIR", "NAMA PRODUK"}));
        filterwaktu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String pilihanWaktu = (String) filterwaktu.getSelectedItem();
                RiwayatPenjualanController.filterData(tabelriwayatpenjualan, pilihanWaktu);
            }
        });

        filterurutan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String pilihanUrutan = (String) filterurutan.getSelectedItem();
                RiwayatPenjualanController.urutkanData(tabelriwayatpenjualan, pilihanUrutan);
            }
        });

        RiwayatPenjualanController.tampilkanDataRiwayatPenjualan(tabelriwayatpenjualan);
        PenjualanProdukController.tampilkanDataPenjualanProduk(tabelpenjualanproduk);
        String username = Login_Page.loggedInUsername;

        // 2. Ambil data user dari database
        LoginController loginController = new LoginController();
        this.user = loginController.getProfil(username);
        this.user = user; // Inisialisasi atribut user
        System.out.println(user);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        System.out.println("User: " + user);
        if (user != null) {

            String namaUppercase = user.getNama().toUpperCase(); // Ubah nama menjadi uppercase
            jLabel2.setText(namaUppercase);
            jLabel5.setText(namaUppercase);
            jLabel7.setText(namaUppercase);

        } else {
            jLabel2.setText("Null");
            jLabel5.setText("Null");
            jLabel7.setText("Null");
        }

        filter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"PER-HARI", "PER-MINGGU", "PER-BULAN", "BERDASARKAN KASIR", "BERDASARKAN KASIR (TOTAL)"}));

        Tbl.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null}
                },
                new String[]{
                    "TANGGAL", "KASIR", "PRODUK TERJUAL", "TOTAL PEMASUKAN" // Ubah "JUMLAH TRANSAKSI" menjadi "PRODUK TERJUAL"
                }
        ));

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Tbl.setModel(new LaporanTableModel());
//        settingTable();        

        function.Log.savelog("\n Admin page initialized with user profile"); // Log initialization

    }

    public Laporan_Keuangan_admin(Profile P) {
        initComponents();

        Tbl.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null}
                },
                new String[]{
                    "TANGGAL", "KASIR", "PRODUK TERJUAL", "TOTAL PEMASUKAN" // Ubah "JUMLAH TRANSAKSI" menjadi "PRODUK TERJUAL"
                }
        ));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Tbl.setModel(new LaporanTableModel());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        laporankeuangan = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        filter = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tbl = new javax.swing.JTable();
        penjualanproduk = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelpenjualanproduk = new javax.swing.JTable();
        riwayatpenjualan = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        filterwaktu = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        filterurutan = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelriwayatpenjualan = new javax.swing.JTable();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        laporankeuangan.setLayout(new java.awt.BorderLayout());

        jButton1.setText("MENU UTAMA");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("ADMIN PAGE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        filter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilihan 1", "Pilihan 2", "Pilihan 3" }));
        filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterActionPerformed(evt);
            }
        });

        jButton4.setText("LOG OUT");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 27)); // NOI18N
        jLabel1.setText("SELAMAT DATANG KEMBALI");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 27)); // NOI18N
        jLabel2.setText("USER");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("PENJUALAN");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(filter, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap(28, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filter, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(14, 14, 14))
        );

        laporankeuangan.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(456, 800));

        Tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "TANGGAL", "KASIR", "PRODUK TERJUAL", "TOTAL PEMASUKAN"
            }
        ));
        jScrollPane3.setViewportView(Tbl);

        laporankeuangan.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("LAPORAN KEUANGAN", laporankeuangan);

        jPanel2.setPreferredSize(new java.awt.Dimension(0, 130));

        jButton6.setText("ADMIN PAGE");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton5.setText("MENU UTAMA");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setText("LOG OUT");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 27)); // NOI18N
        jLabel4.setText("SELAMAT DATANG KEMBALI");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 27)); // NOI18N
        jLabel5.setText("USER");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        tabelpenjualanproduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "NAMA PRODUK", "JUMLAH TERJUAL", "TOTAL PENDAPATAN", "TIME LINE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tabelpenjualanproduk);

        javax.swing.GroupLayout penjualanprodukLayout = new javax.swing.GroupLayout(penjualanproduk);
        penjualanproduk.setLayout(penjualanprodukLayout);
        penjualanprodukLayout.setHorizontalGroup(
            penjualanprodukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1120, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1120, Short.MAX_VALUE)
        );
        penjualanprodukLayout.setVerticalGroup(
            penjualanprodukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(penjualanprodukLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 823, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("PENJUALAN PRODUK", penjualanproduk);

        jPanel5.setPreferredSize(new java.awt.Dimension(0, 130));

        jButton8.setText("ADMIN PAGE");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("LOG OUT");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("MENU UTAMA");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 27)); // NOI18N
        jLabel6.setText("SELAMAT DATANG KEMBALI");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 27)); // NOI18N
        jLabel7.setText("USER");

        filterwaktu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilihan 1", "Pilihan 2", "Pilihan 3" }));
        filterwaktu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterwaktuActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("FILTER TABEL");

        filterurutan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilihan 1", "Pilihan 2", "Pilihan 3" }));
        filterurutan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterurutanActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("URUTKAN BERDASARKAN");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filterurutan, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterwaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterwaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(filterurutan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap())
        );

        tabelriwayatpenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "TANGGAL", "KASIR", "PRODUK", "TOTAL TERJUAL", "TOTAL PENDAPATAN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabelriwayatpenjualan);

        javax.swing.GroupLayout riwayatpenjualanLayout = new javax.swing.GroupLayout(riwayatpenjualan);
        riwayatpenjualan.setLayout(riwayatpenjualanLayout);
        riwayatpenjualanLayout.setHorizontalGroup(
            riwayatpenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 1120, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1120, Short.MAX_VALUE)
        );
        riwayatpenjualanLayout.setVerticalGroup(
            riwayatpenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(riwayatpenjualanLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("RIWAYAT PENJUALAN", riwayatpenjualan);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.PAGE_START);
        getContentPane().add(jTabbedPane2, java.awt.BorderLayout.CENTER);

        jMenu1.setText("HALAMAN INFORMASI");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin logout?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Tutup form saat ini
            this.dispose();

            // Tampilkan form login
            Login_Page loginForm = new Login_Page();
            loginForm.setVisible(true);
            LoginController loginController = new LoginController();
            String username = Login_Page.loggedInUsername; // Ambil username dari objek Profile
            function.Log.savelog("\n User logged out: " + username);
            User user = loginController.getProfil(username);

            if (user != null) {
                function.Log.savelog("\n User logged out information: " + user.toString()); // Gunakan toString() dari objek User
            } else {
                function.Log.savelog("\n User logged out (Profile not found for " + username + ")");
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterActionPerformed
        // TODO add your handling code here:
        String pilihanFilter = (String) filter.getSelectedItem();
        LaporanFilterController.filterData(Tbl, pilihanFilter);
    }//GEN-LAST:event_filterActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Admin_Page admin = new Admin_Page();
        admin.setVisible(true);// TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // 1. Dapatkan username user yang sedang login
        String username = Login_Page.loggedInUsername;

        // 2. Ambil data user dari database
        LoginController loginController = new LoginController();
        User user = loginController.getProfil(username);

        // 3. Buat objek Menu_Utama dan teruskan objek user
        Menu_Utama menuUtama = new Menu_Utama(user);
        menuUtama.setVisible(true);
        this.dispose();
        function.Log.savelog("Navigated to main menu");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        Admin_Page admin = new Admin_Page();
        admin.setVisible(true);// TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        // 1. Dapatkan username user yang sedang login
        String username = Login_Page.loggedInUsername;

        // 2. Ambil data user dari database
        LoginController loginController = new LoginController();
        User user = loginController.getProfil(username);

        // 3. Buat objek Menu_Utama dan teruskan objek user
        Menu_Utama menuUtama = new Menu_Utama(user);
        menuUtama.setVisible(true);
        this.dispose();
        function.Log.savelog("Navigated to main menu");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin logout?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Tutup form saat ini
            this.dispose();

            // Tampilkan form login
            Login_Page loginForm = new Login_Page();
            loginForm.setVisible(true);
            LoginController loginController = new LoginController();
            String username = Login_Page.loggedInUsername; // Ambil username dari objek Profile
            function.Log.savelog("\n User logged out: " + username);
            User user = loginController.getProfil(username);

            if (user != null) {
                function.Log.savelog("\n User logged out information: " + user.toString()); // Gunakan toString() dari objek User
            } else {
                function.Log.savelog("\n User logged out (Profile not found for " + username + ")");
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        Admin_Page admin = new Admin_Page();
        admin.setVisible(true);// TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin logout?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Tutup form saat ini
            this.dispose();

            // Tampilkan form login
            Login_Page loginForm = new Login_Page();
            loginForm.setVisible(true);
            LoginController loginController = new LoginController();
            String username = Login_Page.loggedInUsername; // Ambil username dari objek Profile
            function.Log.savelog("\n User logged out: " + username);
            User user = loginController.getProfil(username);

            if (user != null) {
                function.Log.savelog("\n User logged out information: " + user.toString()); // Gunakan toString() dari objek User
            } else {
                function.Log.savelog("\n User logged out (Profile not found for " + username + ")");
            }
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        // 1. Dapatkan username user yang sedang login
        String username = Login_Page.loggedInUsername;

        // 2. Ambil data user dari database
        LoginController loginController = new LoginController();
        User user = loginController.getProfil(username);

        // 3. Buat objek Menu_Utama dan teruskan objek user
        Menu_Utama menuUtama = new Menu_Utama(user);
        menuUtama.setVisible(true);
        this.dispose();
        function.Log.savelog("Navigated to main menu");
        
    }//GEN-LAST:event_jButton10ActionPerformed

    private void filterwaktuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterwaktuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filterwaktuActionPerformed

    private void filterurutanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterurutanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filterurutanActionPerformed

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
            java.util.logging.Logger.getLogger(Laporan_Keuangan_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Laporan_Keuangan_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Laporan_Keuangan_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Laporan_Keuangan_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Laporan_Keuangan_admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tbl;
    private javax.swing.JComboBox<String> filter;
    private javax.swing.JComboBox<String> filterurutan;
    private javax.swing.JComboBox<String> filterwaktu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JPanel laporankeuangan;
    private javax.swing.JPanel penjualanproduk;
    private javax.swing.JPanel riwayatpenjualan;
    private javax.swing.JTable tabelpenjualanproduk;
    private javax.swing.JTable tabelriwayatpenjualan;
    // End of variables declaration//GEN-END:variables

    private void settingTable() {

//        m = (DefaultTableModel) Tbl.getModel();        
//        Tbl.getColumnModel().getColumn(0).setMinWidth(150);
//        Tbl.getColumnModel().getColumn(0).setMaxWidth(200);
//
//        Tbl.getColumnModel().getColumn(1).setMinWidth(170);
//        Tbl.getColumnModel().getColumn(1).setMaxWidth(200);
//
//        Tbl.getColumnModel().getColumn(2).setMinWidth(200);
//        Tbl.getColumnModel().getColumn(2).setMaxWidth(500); 
//        
//        Tbl.getColumnModel().getColumn(3).setMinWidth(280);
//        Tbl.getColumnModel().getColumn(3).setMaxWidth(500);  
//    
    }

}
