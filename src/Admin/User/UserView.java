/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin.User;

import Dashboard.DashboardView;
import entity.user;
import implement.UserImpl;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
 * @author Bintang K
 */
public class UserView extends javax.swing.JFrame {
    UserImpl userImpl = new UserImpl();

    /**
     * Creates new form UserView
     */
    public UserView() {
        initComponents();

        // --- FORCE FIX SIZE (1117 x 871) ---
        this.setSize(1117, 871);
        this.setResizable(false); // Mengunci agar ukuran dashboard tidak bisa ditarik/diubah manual oleh user
        this.setLocationRelativeTo(null); // Membuat frame otomatis muncul di tengah layar monitor

        tableUsers.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] { "ID User", "Username", "Fullname", "Email", "Role" }));
        loadData("");
        styleTable();
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

    private void styleTable() {
        tableUsers.setRowHeight(48);

        tableUsers.setBackground(new Color(10, 10, 20));
        tableUsers.setForeground(Color.WHITE);

        tableUsers.setSelectionBackground(new Color(24, 24, 32));
        tableUsers.setSelectionForeground(Color.WHITE);

        tableUsers.setGridColor(new Color(30, 30, 40));
        tableUsers.setShowGrid(false);

        tableUsers.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = tableUsers.getTableHeader();
        header.setOpaque(false);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                lbl.setBackground(new Color(24, 24, 32));
                lbl.setForeground(new Color(180, 180, 200));

                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(45, 45, 60)),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)));

                return lbl;
            }
        });

        DefaultTableCellRenderer bodyRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                lbl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
                return lbl;
            }
        };

        for (int i = 0; i < tableUsers.getColumnCount(); i++) {
            tableUsers.getColumnModel().getColumn(i).setCellRenderer(bodyRenderer);
        }
    }

    public void loadData(String keyword) {
        try {
            List<user> list = userImpl.getAll(keyword);
            DefaultTableModel model = (DefaultTableModel) tableUsers.getModel();
            model.setRowCount(0);
            for (user usr : list) {
                model.addRow(new Object[] {
                        usr.getIdUser(),
                        usr.getUsername(),
                        usr.getFullname(),
                        usr.getEmail(),
                        usr.getRole()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableUsers = new javax.swing.JTable();
        tfCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Kelola Users");

        jPanel1.setBackground(new java.awt.Color(18, 18, 28));
        jPanel1.setForeground(new java.awt.Color(202, 196, 212));

        jScrollPane1.setBackground(new java.awt.Color(27, 27, 37));

        tableUsers.setBackground(new java.awt.Color(27, 27, 37));
        tableUsers.setForeground(new java.awt.Color(202, 196, 212));
        tableUsers.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null }
                },
                new String[] {
                        "ID User", "Username", "Fullname", "Email", "Role"
                }) {
            Class[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        tableUsers.setGridColor(new java.awt.Color(27, 27, 37));
        tableUsers.setRowHeight(30);
        jScrollPane1.setViewportView(tableUsers);

        tfCari.setBackground(new java.awt.Color(41, 41, 52));
        tfCari.setForeground(new java.awt.Color(228, 225, 239));
        tfCari.setToolTipText("Cari user...");
        tfCari.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(73, 69, 82), 1, true));

        btnCari.setBackground(new java.awt.Color(41, 41, 52));
        btnCari.setForeground(new java.awt.Color(202, 196, 212));
        btnCari.setText("Cari");
        btnCari.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(73, 69, 82), 1, true));
        btnCari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCariMouseClicked(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(41, 41, 52));
        btnEdit.setForeground(new java.awt.Color(202, 196, 212));
        btnEdit.setText("Edit");
        btnEdit.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(73, 69, 82), 1, true));
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
        });
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        btnHapus.setBackground(new java.awt.Color(41, 41, 52));
        btnHapus.setForeground(new java.awt.Color(202, 196, 212));
        btnHapus.setText("Hapus");
        btnHapus.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(73, 69, 82), 1, true));
        btnHapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHapusMouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(27, 27, 37));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(73, 69, 82)));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(206, 189, 255));
        jLabel5.setText("Kelola Users");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(jLabel5)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel5)
                                .addGap(25, 25, 25)));

        btnBack.setBackground(new java.awt.Color(41, 41, 52));
        btnBack.setForeground(new java.awt.Color(202, 196, 212));
        btnBack.setText("Kembali");
        btnBack.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(73, 69, 82), 1, true));
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1017,
                                                Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(tfCari, javax.swing.GroupLayout.PREFERRED_SIZE, 350,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(15, 15, 15)
                                                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(15, 15, 15)
                                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 120,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(50, 50, 50)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfCari, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 520,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHapusMouseClicked(java.awt.event.MouseEvent evt) {
        int pilihan = tableUsers.getSelectedRow();

        if (pilihan == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data yang ingin dihapus terlebih dahulu.",
                    "Tidak Ada Data Dipilih",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUser = tableUsers.getValueAt(pilihan, 0).toString();
        String username = tableUsers.getValueAt(pilihan, 1).toString();

        int jawaban = JOptionPane.showConfirmDialog(this,
                "Hapus user \"" + username + "\"?\nTindakan ini tidak bisa dibatalkan.",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (jawaban == JOptionPane.YES_OPTION) {
            try {
                userImpl.delete(idUser);
                loadData("");
                JOptionPane.showMessageDialog(this,
                        "User \"" + username + "\" berhasil dihapus.",
                        "Berhasil",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Gagal menghapus data.\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void btnCariMouseClicked(java.awt.event.MouseEvent evt) {
        loadData(tfCari.getText().trim());
    }

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {
        int pilihan = tableUsers.getSelectedRow();

        if (pilihan == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data yang ingin diedit terlebih dahulu.",
                    "Tidak Ada Data Dipilih",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUser = tableUsers.getValueAt(pilihan, 0).toString();
        try {
            new UserForm(idUser).setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal membuka form edit.\n" + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnBackMouseClicked(java.awt.event.MouseEvent evt) {
        new DashboardView().setVisible(true);
        this.dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableUsers;
    private javax.swing.JTextField tfCari;
    // End of variables declaration//GEN-END:variables
}