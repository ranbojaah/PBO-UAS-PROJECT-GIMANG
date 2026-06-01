/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin.Genre;

import Dashboard.DashboardView;
import entity.user;
import implement.GenreImpl;
import interfc.GenreInterfc;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Asus
 */
public class GenreView extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GenreView.class.getName());
    private user currentUser;
    private GenreInterfc genreService = new GenreImpl();

    
    /**
     * Creates new form GenreView
     */
    public GenreView(user usr) {
        initComponents();
        this.currentUser = usr;
        styleTable();
        checkUserRole();
        loadData();
    }
    
    private void checkUserRole() {
        // Validasi jika objek user kosong/null demi keamanan aplikasi
        if (currentUser == null || currentUser.getRole() == null) {
            btTambahGenre.setVisible(false);
            btEditGenre.setVisible(false);
            btHapusGenre.setVisible(false);
            return;
        }
        
        // Periksa apakah role bernilai "admin" (tidak sensitif huruf besar/kecil)
        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            btTambahGenre.setVisible(true);
            btEditGenre.setVisible(true);
            btHapusGenre.setVisible(true);
        } else {
            // Jika bukan admin, sembunyikan semua tombol aksi manajemen data
            btTambahGenre.setVisible(false);
            btEditGenre.setVisible(false);
            btHapusGenre.setVisible(false);
        }
    }
    
    public void loadData() {
        DefaultTableModel model = (DefaultTableModel) tbGenre.getModel();
        model.setRowCount(0); // Reset isi tabel biar ga duplikat

        try {
            java.util.List<entity.genre> list = genreService.getAllGenre();
            for (entity.genre g : list) {
                model.addRow(new Object[]{
                    g.getIdGenre(),
                    g.getNamaGenre().toUpperCase(), // Biar rapi seragam kapital
                    g.getTotalGame() + " Game"
                });
            }
        } catch (java.sql.SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal memuat data genre: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    private void styleTable(){
        tbGenre.setRowHeight(48);

        tbGenre.setBackground(new Color(10,10,20));
        tbGenre.setForeground(Color.WHITE);

        tbGenre.setSelectionBackground(new Color(24,24,32));
        tbGenre.setSelectionForeground(Color.WHITE);

        tbGenre.setGridColor(new Color(30,30,40));

        tbGenre.setShowGrid(false);

        tbGenre.setIntercellSpacing(new Dimension(0,0));

        JTableHeader header = tbGenre.getTableHeader();
        header.setOpaque(false);

        header.setDefaultRenderer(new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, 
                    boolean hasFocus, int row, int column){

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                lbl.setBackground(new Color(24,24,32));
                lbl.setForeground(new Color(180,180,200));

                lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(45,45,60)),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
                ));

                return lbl;
            }
        });
        
        DefaultTableCellRenderer bodyRenderer = new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, 
                    boolean hasFocus, int row, int column){

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                lbl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
                return lbl;
            }
        };

        // Menerapkan renderer isi ke seluruh kolom tabel secara otomatis
        for(int i = 0; i < tbGenre.getColumnCount(); i++){
            tbGenre.getColumnModel().getColumn(i).setCellRenderer(bodyRenderer);
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

        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        btDashboard = new javax.swing.JButton();
        lbTitle = new javax.swing.JLabel();
        btTambahGenre = new javax.swing.JButton();
        btEditGenre = new javax.swing.JButton();
        btHapusGenre = new javax.swing.JButton();
        scrollPaneTable = new javax.swing.JScrollPane();
        tbGenre = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(41, 41, 52));

        headerPanel.setBackground(new java.awt.Color(41, 41, 52));

        btDashboard.setBackground(new java.awt.Color(18, 18, 40));
        btDashboard.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btDashboard.setForeground(new java.awt.Color(255, 255, 255));
        btDashboard.setText("< Dashboard");
        btDashboard.setBorder(null);
        btDashboard.addActionListener(this::btDashboardActionPerformed);

        lbTitle.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(255, 255, 255));
        lbTitle.setText("Kelola Genre");

        btTambahGenre.setBackground(new java.awt.Color(167, 139, 250));
        btTambahGenre.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btTambahGenre.setForeground(new java.awt.Color(17, 17, 29));
        btTambahGenre.setText("+ Tambah");
        btTambahGenre.setBorder(null);
        btTambahGenre.addActionListener(this::btTambahGenreActionPerformed);

        btEditGenre.setBackground(new java.awt.Color(102, 102, 255));
        btEditGenre.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btEditGenre.setForeground(new java.awt.Color(255, 255, 255));
        btEditGenre.setText("Edit");
        btEditGenre.setBorder(null);
        btEditGenre.addActionListener(this::btEditGenreActionPerformed);

        btHapusGenre.setBackground(new java.awt.Color(208, 59, 70));
        btHapusGenre.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btHapusGenre.setForeground(new java.awt.Color(255, 255, 255));
        btHapusGenre.setText("Hapus");
        btHapusGenre.setBorder(null);
        btHapusGenre.addActionListener(this::btHapusGenreActionPerformed);

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(btDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
                .addComponent(btTambahGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btEditGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btHapusGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTitle)
                    .addComponent(btTambahGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btEditGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btHapusGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        scrollPaneTable.setBackground(new java.awt.Color(18, 18, 28));
        scrollPaneTable.setBorder(null);

        tbGenre.setBackground(new java.awt.Color(18, 18, 28));
        tbGenre.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "NAMA GENRE", "TOTAL GAME"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPaneTable.setViewportView(tbGenre);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneTable)
                .addGap(16, 16, 16))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scrollPaneTable, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btTambahGenreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahGenreActionPerformed
        // TODO add your handling code here:
        GenreForm formInput = new GenreForm(currentUser, this);
        formInput.setVisible(true);
    }//GEN-LAST:event_btTambahGenreActionPerformed

    private void btEditGenreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditGenreActionPerformed
        // TODO add your handling code here:
        int selectedRow = tbGenre.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Pilih baris genre di tabel yang ingin diedit terlebih dahulu!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mengambil data dari baris tabel yang diklik
        String idGenre = tbGenre.getValueAt(selectedRow, 0).toString();
        String namaGenre = tbGenre.getValueAt(selectedRow, 1).toString();

        // Membungkus data ke dalam objek model entity genre
        entity.genre gObjek = new entity.genre();
        gObjek.setIdGenre(idGenre);
        gObjek.setNamaGenre(namaGenre);

        // Buka GenreForm menggunakan Constructor ke-2 (Mengirim data objek genre)
        GenreForm formEdit = new GenreForm(currentUser, this, gObjek);
        formEdit.setVisible(true);
    }//GEN-LAST:event_btEditGenreActionPerformed

    private void btHapusGenreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btHapusGenreActionPerformed
        // TODO add your handling code here:
        int selectedRow = tbGenre.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Pilih genre yang ingin dihapus terlebih dahulu!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Ambil data total game dari kolom indeks ke-2 (Kolom TOTAL GAME)
        String totalGameStr = tbGenre.getValueAt(selectedRow, 2).toString(); // Isinya misal "3 Game" atau "0 Game"

        // 2. Proteksi: Jika jumlah game lebih dari 0, langsung blokir!
        if (!totalGameStr.startsWith("0")) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                    "Genre ini tidak bisa dihapus karena masih memiliki game aktif!", 
                    "Akses Ditolak", javax.swing.JOptionPane.ERROR_MESSAGE);
            return; // Menghentikan fungsi hapus, aman!
        }

        // Jika lolos (0 Game), baru lanjut ke proses konfirmasi hapus biasa
        String idGenre = tbGenre.getValueAt(selectedRow, 0).toString();
        String namaGenre = tbGenre.getValueAt(selectedRow, 1).toString();

        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus genre [" + idGenre + " - " + namaGenre + "]?", 
                "Konfirmasi Hapus", javax.swing.JOptionPane.YES_NO_OPTION);

        if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {
            try {
                boolean berhasil = genreService.deleteGenre(idGenre);
                if (berhasil) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Genre berhasil dihapus!");
                    loadData(); // Refresh tabel
                }
            } catch (java.sql.SQLException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Gagal menghapus database error.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                logger.log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btHapusGenreActionPerformed

    private void btDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDashboardActionPerformed
        // TODO add your handling code here:
        new DashboardView().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btDashboardActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDashboard;
    private javax.swing.JButton btEditGenre;
    private javax.swing.JButton btHapusGenre;
    private javax.swing.JButton btTambahGenre;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane scrollPaneTable;
    private javax.swing.JTable tbGenre;
    // End of variables declaration//GEN-END:variables
}
