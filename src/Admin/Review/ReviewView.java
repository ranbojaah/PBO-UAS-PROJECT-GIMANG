/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin.Review;
import Dashboard.DashboardView;
import static com.mysql.cj.conf.PropertyKey.logger;
import entity.Review;
import entity.user;
import implement.Reviewimpl;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Asus
 */
public class ReviewView extends javax.swing.JFrame {

private user currentUser;
private Reviewimpl reviewImpl = new Reviewimpl();
    private String selectedReviewId = null;

    public ReviewView(user usr) {
        initComponents();
        this.setLocationRelativeTo(null); // Membuat frame di tengah layar
        this.currentUser = usr;

        // Panggil metode dekorasi & load data
        styleTable();
        setupRole();
        loadReview();

        
            txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchReview(); // Memanggil method searchReview di atas
            }
        });
        
        tfKomentarLengkap.setLineWrap(true);
        tfKomentarLengkap.setWrapStyleWord(true);
        tfKomentarLengkap.setEditable(false);
        tfKomentarLengkap.setOpaque(false);
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
    
   
      private void styleTable() 
    {
    tbReview.setRowHeight(48);
    tbReview.setBackground(new Color(10, 10, 20));
    tbReview.setForeground(Color.WHITE);
    tbReview.setSelectionBackground(new Color(24, 24, 32));
    tbReview.setSelectionForeground(Color.WHITE);
    tbReview.setGridColor(new Color(30, 30, 40));
    tbReview.setShowGrid(false);
    tbReview.setIntercellSpacing(new Dimension(0, 0));

    // Styling Header Tabel
        JTableHeader header = tbReview.getTableHeader();
    header.setOpaque(false);
    header.setDefaultRenderer(new DefaultTableCellRenderer(){
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setBackground(new Color(24, 24, 32));
            lbl.setForeground(new Color(180, 180, 200));
            lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(45, 45, 60)),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));
            return lbl;
        }
    });

    // Styling Baris/Body Tabel (Padding cell)
    DefaultTableCellRenderer bodyRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            return lbl;
        }
    };

    for (int i = 0; i < tbReview.getColumnCount(); i++) {
        tbReview.getColumnModel().getColumn(i).setCellRenderer(bodyRenderer);
    }
}
      
       private void setupRole() {
    String role = currentUser.getRole();
    
    

    if (role.equalsIgnoreCase("admin")) {
        // Admin bisa melihat dan menggunakan semua tombol moderasi
        btnDeletePermanently.setVisible(true);
    } else if (role.equalsIgnoreCase("seller")) {
        // Seller hanya bisa membaca review, tidak bisa memoderasi
        btnDeletePermanently.setVisible(false);
        
        // Opsional: Jika panel penampung tombol ingin disembunyikan sekaligus
        // panelAksiReview.setVisible(false);
    }
}
    
  public void loadReview() {
    try {
        List<Review> data;
        String role = currentUser.getRole(); // Mengambil role pengguna

        if (role.equalsIgnoreCase("admin")) {
            data = reviewImpl.getAll(); 
        } else if (role.equalsIgnoreCase("seller")) {
            // Memanggil metode untuk Seller
            data = reviewImpl.getBySeller(currentUser.getIdUser()); 
        } else {
            // Diasumsikan role lain adalah "buyer"
            data = reviewImpl.getByBuyer(currentUser.getIdUser()); 
        }

        fillTable(data);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(
                this,
                "Gagal memuat data review: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
    private void fillTable(List<Review> data) {
        DefaultTableModel model = new DefaultTableModel();
    
        model.addColumn("ID REVIEW");
        model.addColumn("GAME");
        model.addColumn("PEMBELI");
        model.addColumn("RATING");
        model.addColumn("TANGGAL");

        for (Review r : data) {
        model.addRow(new Object[] {
        r.getReviewId(),
        r.getGameTitle(),
        r.getReviewerUsername(), 
        r.getRating() + " / 5",
        r.getReviewDate()
    });
    }

    tbReview.setModel(model);

//    styleTable(); 
}   
    
    
   public void searchReview() {
    try {
        String keyword = txtSearch.getText().trim();
        List<Review> data;
        String role = currentUser.getRole();

        if (keyword.isEmpty()) {
            loadReview();
            return;
        }

        if (role.equalsIgnoreCase("admin")) {
            // Admin cari kesemua data
            data = reviewImpl.search(keyword); 
        } else {
            // Non-Admin (Buyer) hanya mencari di dalam review miliknya sendiri
            String buyerIdStr = currentUser.getIdUser() + "";
            data = reviewImpl.searchByBuyer(buyerIdStr, keyword); 
        }

        fillTable(data);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(
                this,
                "Gagal melakukan pencarian: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
   
    

    
    private void clearDetailFields() {
    lbGameListing.setText("-");
    lbReviewer.setText("-");
    lbRatingScore.setText("-");
    lbSubmissionDate.setText("-");
    tfKomentarLengkap.setText("");
    selectedReviewId = null; // Kosongkan pointer ID yang aktif
}

    

   
private void showDetailReview(String reviewId) {
    try {

        Review r = reviewImpl.getById(reviewId);

        // Validasi jika data tidak ditemukan
        if (r == null) {
            return;
        }


        lbGameListing.setText(r.getGameTitle());             
        lbReviewer.setText(r.getReviewerUsername());          
        lbRatingScore.setText(r.getRating() + " / 5");        
        lbSubmissionDate.setText(String.valueOf(r.getReviewDate()));
        

        tfKomentarLengkap.setText(r.getComment());             

        selectedReviewId = r.getReviewId();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(
                this,
                "Gagal menampilkan detail review: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
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
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnDashboard = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbReview = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbGameListing = new javax.swing.JLabel();
        lbReviewer = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbRatingScore = new javax.swing.JLabel();
        lbSubmissionDate = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tfKomentarLengkap = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        btnDeletePermanently = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Reviews");
        setBackground(new java.awt.Color(13, 18, 25));

        jPanel1.setBackground(new java.awt.Color(13, 18, 25));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBackground(new java.awt.Color(18, 23, 31));
        jPanel2.setPreferredSize(new java.awt.Dimension(960, 360));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setName(""); // NOI18N
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(474, 36));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        txtSearch.setBackground(new java.awt.Color(22, 28, 38));
        txtSearch.setForeground(new java.awt.Color(255, 255, 255));
        txtSearch.setPreferredSize(new java.awt.Dimension(300, 26));
        jPanel5.add(txtSearch);

        btnDashboard.setBackground(new java.awt.Color(133, 144, 224));
        btnDashboard.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(13, 18, 25));
        btnDashboard.setText("Dashboard");
        btnDashboard.setBorderPainted(false);
        btnDashboard.setPreferredSize(new java.awt.Dimension(120, 26));
        btnDashboard.addActionListener(this::btnDashboardActionPerformed);
        jPanel5.add(btnDashboard);

        jPanel3.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("REVIEW");
        jLabel1.setPreferredSize(new java.awt.Dimension(228, 18));
        jPanel4.add(jLabel1);

        jPanel3.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        tbReview.setBackground(new java.awt.Color(18, 23, 31));
        tbReview.setForeground(new java.awt.Color(224, 224, 224));
        tbReview.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "GAME ", "REVIEWER", "RATING", "KOMENTAR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbReview.setGridColor(new java.awt.Color(25, 32, 43));
        tbReview.setRowHeight(42);
        tbReview.setSelectionBackground(new java.awt.Color(30, 34, 58));
        tbReview.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbReviewMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbReview);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel6.setBackground(new java.awt.Color(18, 23, 31));
        jPanel6.setPreferredSize(new java.awt.Dimension(960, 260));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Detail Review");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 20);
        jPanel6.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(136, 136, 136));
        jLabel5.setText("GAME LISTING");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.15;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 10);
        jPanel6.add(jLabel5, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(136, 136, 136));
        jLabel6.setText("REVIEWER");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.15;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 20);
        jPanel6.add(jLabel6, gridBagConstraints);

        lbGameListing.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lbGameListing.setForeground(new java.awt.Color(238, 238, 238));
        lbGameListing.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.15;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 10);
        jPanel6.add(lbGameListing, gridBagConstraints);

        lbReviewer.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lbReviewer.setForeground(new java.awt.Color(238, 238, 238));
        lbReviewer.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.15;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 20, 20);
        jPanel6.add(lbReviewer, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(136, 136, 136));
        jLabel9.setText("RATING SCORE");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.15;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 10);
        jPanel6.add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(136, 136, 136));
        jLabel10.setText("SUBMISSION DATE");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.15;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 20);
        jPanel6.add(jLabel10, gridBagConstraints);

        lbRatingScore.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbRatingScore.setForeground(new java.awt.Color(168, 136, 255));
        lbRatingScore.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.15;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel6.add(lbRatingScore, gridBagConstraints);

        lbSubmissionDate.setForeground(new java.awt.Color(204, 204, 204));
        lbSubmissionDate.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.15;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 20);
        jPanel6.add(lbSubmissionDate, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(136, 136, 136));
        jLabel13.setText("KOMENTAR LENGKAP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 20);
        jPanel6.add(jLabel13, gridBagConstraints);

        tfKomentarLengkap.setEditable(false);
        tfKomentarLengkap.setBackground(new java.awt.Color(18, 23, 31));
        tfKomentarLengkap.setColumns(20);
        tfKomentarLengkap.setForeground(new java.awt.Color(221, 221, 221));
        tfKomentarLengkap.setLineWrap(true);
        tfKomentarLengkap.setRows(5);
        tfKomentarLengkap.setWrapStyleWord(true);
        jScrollPane2.setViewportView(tfKomentarLengkap);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        jPanel6.add(jScrollPane2, gridBagConstraints);

        jPanel7.setOpaque(false);
        jPanel7.setLayout(new java.awt.GridLayout(3, 1, 0, 10));

        btnDeletePermanently.setBackground(new java.awt.Color(153, 15, 17));
        btnDeletePermanently.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDeletePermanently.setForeground(new java.awt.Color(255, 255, 255));
        btnDeletePermanently.setText("Delete Permanently");
        btnDeletePermanently.setBorderPainted(false);
        btnDeletePermanently.addActionListener(this::btnDeletePermanentlyActionPerformed);
        jPanel7.add(btnDeletePermanently);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.4;
        jPanel1.add(jPanel6, gridBagConstraints);

        getContentPane().add(jPanel1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeletePermanentlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePermanentlyActionPerformed
        // TODO add your handling code here:
        // 1. Validasi apakah ada review yang sedang dipilih
    if (selectedReviewId == null) {
        JOptionPane.showMessageDialog(this, 
                "Silahkan pilih review terlebih dahulu dari tabel!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // 2. Konfirmasi tindakan ke Admin
    int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus review ini secara permanen?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // 3. Eksekusi hapus data melalui implementasi
            reviewImpl.delete(selectedReviewId);
            
            JOptionPane.showMessageDialog(this, "Review berhasil dihapus secara permanen!");
            
            // 4. Reset form detail dan refresh tabel
            clearDetailFields();
            loadReview();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                    "Gagal menghapus review: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_btnDeletePermanentlyActionPerformed

    private void tbReviewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbReviewMouseClicked
      int row = tbReview.getSelectedRow();
    if (row != -1) {
        // Mengonversi indeks baris view ke model asli untuk mencegah salah ambil data
        int modelRow = tbReview.convertRowIndexToModel(row);
        
        // Ambil ID dari kolom pertama (index 0)
        Object value = tbReview.getModel().getValueAt(modelRow, 0);
        
        if (value != null) {
            String reviewId = value.toString();
            showDetailReview(reviewId);
        }
    }
    }//GEN-LAST:event_tbReviewMouseClicked

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        // TODO add your handling code here:
        new DashboardView().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnDashboardActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        //</editor-fold>

        /* Create and display the form */
  try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
    java.util.logging.Logger.getLogger(ReviewView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnDeletePermanently;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbGameListing;
    private javax.swing.JLabel lbRatingScore;
    private javax.swing.JLabel lbReviewer;
    private javax.swing.JLabel lbSubmissionDate;
    private javax.swing.JTable tbReview;
    private javax.swing.JTextArea tfKomentarLengkap;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
