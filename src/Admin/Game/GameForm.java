/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin.Game;

import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


/**
 *
 * @author Asus
 */
public class GameForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GameForm.class.getName());
    private entity.user currentUser;
    private GameView parentView;

    /**
     * Creates new form GameForm
     */
    public GameForm() {
        initComponents();
        this.setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/asset/gamecnh.png")).getImage());
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int jawaban = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin ingin menutup aplikasi?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (jawaban == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
    }

    public GameForm(entity.user usr, GameView parent) {
        this.currentUser = usr;
        this.parentView = parent;
        initComponents();
        txtIdGame.setText(generateGameID());
        tampil_komponen_data();
        this.setLocationRelativeTo(null);
        
    }
    
    
    // Helper method untuk memuat data Genre ke JList dan Platform ke ComboBox
    private void tampil_komponen_data() {
        java.sql.Connection conn = null;
        java.sql.Statement st = null;
        java.sql.ResultSet res = null;
        try {
            conn = Connection.Koneksi.getConnection();
            st = conn.createStatement();

            // 1. Ambil data Genre dari database dan masukkan ke Model JList
            javax.swing.DefaultListModel<String> listModel = new javax.swing.DefaultListModel<>();
            res = st.executeQuery("SELECT name FROM genres ORDER BY name ASC");
            while (res.next()) {
                listModel.addElement(res.getString("name"));
            }
            listGenre.setModel(listModel);

            // 2. Set pilihan item pada cbPlatform secara dinamis
            cbPlatform.removeAllItems();
            cbPlatform.addItem("PC");
            cbPlatform.addItem("PlayStation 5");
            cbPlatform.addItem("Xbox Series X/S");
            cbPlatform.addItem("Nintendo Switch");
            cbPlatform.addItem("Android/iOS");

            // Reset text placeholder bawaan NetBeans
            txtDeveloper.setText("");
            txtJudul.setText("");
            txtTahun.setText("");
            txtDeskripsi.setText("");

        } catch (SQLException e) {
            System.err.println("Gagal memuat komponen data: " + e.getMessage());
        } finally {
            try {
                if (res != null)
                    res.close();
            } catch (SQLException e) {
            }
            try {
                if (st != null)
                    st.close();
            } catch (SQLException e) {
            }
        }
    }

    // Helper method untuk membuat ID Game otomatis secara berurutan (Contoh: G005)
    private String generateGameID() {
        String newID = "G001";
        java.sql.Connection conn = null;
        java.sql.Statement st = null;
        java.sql.ResultSet res = null;
        try {
            String sql = "SELECT game_id FROM games ORDER BY game_id DESC LIMIT 1";
            conn = Connection.Koneksi.getConnection();
            st = conn.createStatement();
            res = st.executeQuery(sql);
            if (res.next()) {
                String lastID = res.getString("game_id");
                int idNum = Integer.parseInt(lastID.substring(1)) + 1;
                newID = String.format("G%03d", idNum);
            }
        } catch (Exception e) {
            System.err.println("Gagal generate ID Game: " + e.getMessage());
        } finally {
            try {
                if (res != null)
                    res.close();
            } catch (SQLException e) {
            }
            try {
                if (st != null)
                    st.close();
            } catch (SQLException e) {
            }
        }
        return newID;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblTambahGame = new javax.swing.JLabel();
        lblDataKatalogGame = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblGenerate = new javax.swing.JLabel();
        txtTahun = new javax.swing.JTextField();
        lblTahun = new javax.swing.JLabel();
        txtIdGame = new javax.swing.JTextField();
        lblIdGame = new javax.swing.JLabel();
        lblJudulGame = new javax.swing.JLabel();
        txtJudul = new javax.swing.JTextField();
        lblGenre = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listGenre = new javax.swing.JList<>();
        cbPlatform = new javax.swing.JComboBox<>();
        lblDeveloper = new javax.swing.JLabel();
        txtDeveloper = new javax.swing.JTextField();
        lblDeskripsi = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDeskripsi = new javax.swing.JTextArea();
        jSeparator2 = new javax.swing.JSeparator();
        btnBatal = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        lblPlatform = new javax.swing.JLabel();

        jLabel3.setText("jLabel3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Game Form");

        jPanel1.setBackground(new java.awt.Color(48, 48, 46));

        lblTambahGame.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblTambahGame.setForeground(new java.awt.Color(217, 216, 213));
        lblTambahGame.setText("Tambah Game");

        lblDataKatalogGame.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lblDataKatalogGame.setForeground(new java.awt.Color(217, 216, 213));
        lblDataKatalogGame.setText("Data Katalog Game");

        lblGenerate.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        lblGenerate.setForeground(new java.awt.Color(217, 216, 213));
        lblGenerate.setText("Generate Otomatis");

        lblTahun.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblTahun.setForeground(new java.awt.Color(217, 216, 213));
        lblTahun.setText("Tahun Rilis");

        txtIdGame.setEditable(false);

        lblIdGame.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblIdGame.setForeground(new java.awt.Color(217, 216, 213));
        lblIdGame.setText("ID Game");

        lblJudulGame.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblJudulGame.setForeground(new java.awt.Color(217, 216, 213));
        lblJudulGame.setText("Judul Game *");

        lblGenre.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblGenre.setForeground(new java.awt.Color(217, 216, 213));
        lblGenre.setText("Genre * (Bisa pilih multi)");

        jScrollPane2.setViewportView(listGenre);

        cbPlatform.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblDeveloper.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblDeveloper.setForeground(new java.awt.Color(217, 216, 213));
        lblDeveloper.setText("Developer/Publisher *");

        txtDeveloper.setText("jTextField1");

        lblDeskripsi.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblDeskripsi.setForeground(new java.awt.Color(217, 216, 213));
        lblDeskripsi.setText("Deskripsi Singkat");

        txtDeskripsi.setColumns(20);
        txtDeskripsi.setRows(5);
        jScrollPane1.setViewportView(txtDeskripsi);

        btnBatal.setText("Batal");
        btnBatal.addActionListener(this::btnBatalActionPerformed);

        btnSimpan.setText("Simpan Game");
        btnSimpan.addActionListener(this::btnSimpanActionPerformed);

        lblPlatform.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblPlatform.setForeground(new java.awt.Color(217, 216, 213));
        lblPlatform.setText("Platform *");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDataKatalogGame)
                            .addComponent(lblTambahGame)
                            .addComponent(lblDeveloper))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtJudul)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtIdGame, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblIdGame))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTahun, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTahun)))
                            .addComponent(txtDeveloper)
                            .addComponent(lblDeskripsi)
                            .addComponent(lblJudulGame)
                            .addComponent(lblGenerate)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblGenre)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPlatform)
                                    .addComponent(cbPlatform, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(21, 21, 21))))
            .addComponent(jSeparator2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBatal)
                .addGap(18, 18, 18)
                .addComponent(btnSimpan)
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(lblTambahGame)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDataKatalogGame)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTahun)
                    .addComponent(lblIdGame))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdGame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGenerate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblJudulGame)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtJudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGenre)
                    .addComponent(lblPlatform))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbPlatform, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblDeveloper)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDeveloper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDeskripsi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBatal)
                    .addComponent(btnSimpan))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnBatalActionPerformed
        this.dispose();
    }// GEN-LAST:event_btnBatalActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSimpanActionPerformed
        java.sql.Connection conn = null;
        java.sql.PreparedStatement pstGenre = null;
        java.sql.PreparedStatement pstGame = null;
        java.sql.PreparedStatement pstLink = null;

        try {
            // 1. Ambil input dari form UI
            String id = txtIdGame.getText();
            String judul = txtJudul.getText().trim();
            String tahun = txtTahun.getText().trim();
            String platform = cbPlatform.getSelectedItem().toString();
            String developer = txtDeveloper.getText().trim();
            String deskripsi = txtDeskripsi.getText().trim();

            // Ambil daftar nama genre yang dipilih oleh user dari JList (bisa multi-genre)
            java.util.List<String> selectedGenres = listGenre.getSelectedValuesList();

            // Validasi field kosong yang bersifat mandatory (*) + Validasi minimal pilih 1
            // genre
            if (judul.isEmpty() || tahun.isEmpty() || developer.isEmpty() || selectedGenres.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Judul, Tahun Rilis, Developer, dan minimal pilih 1 Genre tidak boleh kosong!");
                return;
            }

            // Validasi Input Angka pada Tahun Rilis (Mencegah NumberFormatException)
            if (!tahun.matches("\\d{4}")) {
                javax.swing.JOptionPane.showMessageDialog(this, "Tahun rilis harus berupa 4 digit angka!");
                return;
            }

            conn = Connection.Koneksi.getConnection();
            // Matikan Auto-Commit untuk memulai transaksi manual (Many-to-Many aman)
            conn.setAutoCommit(false);

            // 2. Simpan data game ke dalam tabel utama `games`
            String sqlGame = "INSERT INTO games (game_id, title, platform, release_year, developer, description) VALUES (?, ?, ?, ?, ?, ?)";
            pstGame = conn.prepareStatement(sqlGame);
            pstGame.setString(1, id);
            pstGame.setString(2, judul);
            pstGame.setString(3, platform);
            pstGame.setInt(4, Integer.parseInt(tahun));
            pstGame.setString(5, developer);
            pstGame.setString(6, deskripsi);
            pstGame.executeUpdate();

            // 3. Simpan relasi ke tabel jembatan `gamegenre` dengan melakukan looping data
            // genre yang dipilih
            String sqlGenre = "SELECT id FROM genres WHERE name = ? LIMIT 1";
            String sqlLink = "INSERT INTO gamegenre (id, genre_id, game_id) VALUES (?, ?, ?)";

            pstGenre = conn.prepareStatement(sqlGenre);
            pstLink = conn.prepareStatement(sqlLink);

            int counter = 0;
            for (String namaGenre : selectedGenres) {
                // Ambil id dari database berdasarkan nama genre yang dipilih di JList
                pstGenre.setString(1, namaGenre);
                try (java.sql.ResultSet resGenre = pstGenre.executeQuery()) {
                    String genreId = "GEN01"; // Fallback default
                    if (resGenre.next()) {
                        genreId = resGenre.getString("id");
                    }

                    // Membuat ID unik berurutan acak untuk PK tabel jembatan gamegenre
                    String idGameGenre = "GG" + String.valueOf(System.currentTimeMillis()).substring(7) + counter;

                    pstLink.setString(1, idGameGenre);
                    pstLink.setString(2, genreId);
                    pstLink.setString(3, id);
                    pstLink.executeUpdate();
                }
                counter++;
            }

            // Jika seluruh proses insert (games & gamegenre) sukses tanpa error, lakukan
            // commit
            conn.commit();
            javax.swing.JOptionPane.showMessageDialog(null, "Data Game berhasil disimpan!");

            // 4. Callback otomatis untuk memicu refresh data tabel di GameView
            if (parentView != null) {
                parentView.load_table("");
            }

            // 5. Tutup pop-up dialog form
            this.dispose();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            // Batalkan semua query / rollback jika terjadi kegagalan di tengah jalan
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Gagal melakukan rollback: " + ex.getMessage());
                }
            }
            javax.swing.JOptionPane.showMessageDialog(this, "Data gagal disimpan: " + e.getMessage());
        } finally {
            // Bersihkan semua resource database
            try {
                if (pstGenre != null)
                    pstGenre.close();
            } catch (SQLException e) {
            }
            try {
                if (pstGame != null)
                    pstGame.close();
            } catch (SQLException e) {
            }
            try {
                if (pstLink != null)
                    pstLink.close();
            } catch (SQLException e) {
            }
            try {
                if (conn != null)
                    conn.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
    }// GEN-LAST:event_btnSimpanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new GameForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cbPlatform;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblDataKatalogGame;
    private javax.swing.JLabel lblDeskripsi;
    private javax.swing.JLabel lblDeveloper;
    private javax.swing.JLabel lblGenerate;
    private javax.swing.JLabel lblGenre;
    private javax.swing.JLabel lblIdGame;
    private javax.swing.JLabel lblJudulGame;
    private javax.swing.JLabel lblPlatform;
    private javax.swing.JLabel lblTahun;
    private javax.swing.JLabel lblTambahGame;
    private javax.swing.JList<String> listGenre;
    private javax.swing.JTextArea txtDeskripsi;
    private javax.swing.JTextField txtDeveloper;
    private javax.swing.JTextField txtIdGame;
    private javax.swing.JTextField txtJudul;
    private javax.swing.JTextField txtTahun;
    // End of variables declaration//GEN-END:variables
}