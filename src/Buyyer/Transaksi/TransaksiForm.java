/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Buyyer.Transaksi;

import Admin.Transaksi.TransaksiView;
import entity.user;
import entity.transaksi;
import interfc.TransaksiInterfc;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 *
 * @author Asus
 */
public class TransaksiForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TransaksiForm.class.getName());

    private user currentUser;
    private TransaksiInterfc transaksiRepo = new implement.TransaksiImpl();
    private TransaksiView transaksiView;
    /**
     * Creates new form TransaksiForm
     */
    public TransaksiForm(user currentUser, TransaksiView parentView) {
        initComponents();
        this.currentUser = currentUser;
        this.transaksiView = parentView;
        this.setLocationRelativeTo(null);
        
        styleComboBox(cbListing);
        styleComboBox(cbMetode);
        
        initFormLogic();
    }
    
    private void initFormLogic() {
        dcTanggal.setDate(new java.util.Date());

        tfId.setEditable(false);
        tfTotal.setEditable(false);
        tfTotal.setBackground(new Color(45, 45, 55));
        tfTotal.setForeground(new Color(202, 196, 212));

        try {
            String newId = transaksiRepo.generateNewTransactionId();
            tfId.setText(newId);
        } catch (java.sql.SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Gagal generate ID transaksi", e);
            tfId.setText("T000");
        }
        
        tfTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c)) {
                    evt.consume();
                }
            }
        });
        
        // 4. Ambil data asli untuk dimasukkan ke ComboBox
        loadListingData();
        loadMetodeBayar();
    }
    
    private void loadListingData() {
        cbListing.removeAllItems();
        cbListing.addItem("Pilih listing");
        
        // Mengambil list data dari database via Impl
        java.util.List<String[]> data = transaksiRepo.getAvailableListing();
        for (String[] listing : data) {
            String id = listing[0];
            String nama = listing[1];
            cbListing.addItem(id + " - " + nama);
        }
    }

    private void loadMetodeBayar() {
        cbMetode.removeAllItems();
        cbMetode.addItem("Pilih Metode");
        cbMetode.addItem("Transfer Bank");
        cbMetode.addItem("COD");
        cbMetode.addItem("E-Wallet");
    }
    
    private void styleComboBox(JComboBox comboBox) {
        Color bg = new Color(32, 32, 41);
        Color fg = new Color(202, 196, 212);
        Color border = new Color(90, 90, 110);
        Color selectedBg = new Color(55, 55, 70);

        comboBox.setUI(new BasicComboBoxUI() {

            @Override
            protected JButton createArrowButton() {
                BasicArrowButton button = new BasicArrowButton(
                        BasicArrowButton.SOUTH,
                        bg,          // background
                        border,      // shadow
                        fg,          // arrow color
                        bg           // highlight
                );

                button.setBackground(bg);
                button.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, border));
                button.setOpaque(true);
                button.setContentAreaFilled(true);
                button.setFocusPainted(false);

                return button;
            }

            @Override
            public void paintCurrentValueBackground(
                    Graphics g,
                    Rectangle bounds,
                    boolean hasFocus
            ) {
                g.setColor(bg);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            @Override
            protected BasicComboPopup createPopup() {
                BasicComboPopup popup = new BasicComboPopup(comboBox) {
                    @Override
                    protected void configureList() {
                        super.configureList();

                        list.setBackground(bg);
                        list.setForeground(fg);
                        list.setSelectionBackground(selectedBg);
                        list.setSelectionForeground(fg);
                        list.setBorder(BorderFactory.createLineBorder(border, 1));
                    }
                };

                popup.setBorder(BorderFactory.createLineBorder(border, 1));
                return popup;
            }
        });

        comboBox.setBackground(bg);
        comboBox.setForeground(fg);
        comboBox.setOpaque(true);
        comboBox.setFocusable(false);
        comboBox.setBorder(BorderFactory.createLineBorder(border, 1));

        comboBox.setPreferredSize(
                new Dimension(comboBox.getPreferredSize().width, 32)
        );

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );

                label.setOpaque(true);
                label.setForeground(fg);
                label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

                if (isSelected) {
                    label.setBackground(selectedBg);
                } else {
                    label.setBackground(bg);
                }

                return label;
            }
        });

        comboBox.revalidate();
        comboBox.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tfId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        dcTanggal = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbListing = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbMetode = new javax.swing.JComboBox<>();
        tfTotal = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btCatat = new javax.swing.JButton();
        btBatal = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(18, 18, 28));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(73, 69, 82)));

        jPanel2.setBackground(new java.awt.Color(27, 27, 37));

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(206, 189, 255));
        jLabel1.setText("Catat/Edit Listing");

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(202, 196, 212));
        jLabel2.setText("Input transaksi jual beli");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(21, 21, 21))
        );

        jPanel4.setBackground(new java.awt.Color(18, 18, 28));

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(202, 196, 212));
        jLabel3.setText("ID Transaksi");

        tfId.setBackground(new java.awt.Color(32, 32, 41));
        tfId.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        tfId.setForeground(new java.awt.Color(202, 196, 212));
        tfId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 108, 120)));
        tfId.addActionListener(this::tfIdActionPerformed);

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(202, 196, 212));
        jLabel4.setText("Tanggal Transaksi");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(tfId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(dcTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(36, 36, 36))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfId)
                    .addComponent(dcTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(18, 18, 28));

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(202, 196, 212));
        jLabel5.setText("Listing*");

        cbListing.setBackground(new java.awt.Color(32, 32, 41));
        cbListing.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        cbListing.setForeground(new java.awt.Color(202, 196, 212));
        cbListing.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih listing", " ", "Item 1", "Item 2", "Item 3" }));
        cbListing.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 108, 120)));
        cbListing.addActionListener(this::cbListingActionPerformed);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(cbListing, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(37, 37, 37))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addComponent(jLabel5)
                    .addContainerGap(431, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(cbListing, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel5)
                    .addContainerGap(44, Short.MAX_VALUE)))
        );

        jPanel6.setBackground(new java.awt.Color(18, 18, 28));

        jLabel9.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(202, 196, 212));
        jLabel9.setText("Total harga(Rp)");

        jLabel8.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(202, 196, 212));
        jLabel8.setText("Metode Bayar*");

        cbMetode.setBackground(new java.awt.Color(32, 32, 41));
        cbMetode.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        cbMetode.setForeground(new java.awt.Color(202, 196, 212));
        cbMetode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Metode", "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbMetode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 108, 120)));

        tfTotal.setBackground(new java.awt.Color(32, 32, 41));
        tfTotal.setForeground(new java.awt.Color(202, 196, 212));
        tfTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 108, 120)));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(tfTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(cbMetode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(37, 37, 37))))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addComponent(jLabel9)
                    .addContainerGap(358, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbMetode, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(jLabel9)
                    .addContainerGap(45, Short.MAX_VALUE)))
        );

        jPanel8.setBackground(new java.awt.Color(18, 18, 28));

        jPanel3.setBackground(new java.awt.Color(27, 27, 37));

        btCatat.setBackground(new java.awt.Color(167, 139, 250));
        btCatat.setFont(new java.awt.Font("Verdana", 1, 13)); // NOI18N
        btCatat.setForeground(new java.awt.Color(60, 25, 137));
        btCatat.setText("Catat Transaksi");
        btCatat.setBorder(null);
        btCatat.addActionListener(this::btCatatActionPerformed);

        btBatal.setBackground(new java.awt.Color(147, 0, 10));
        btBatal.setFont(new java.awt.Font("Verdana", 1, 13)); // NOI18N
        btBatal.setForeground(new java.awt.Color(255, 218, 214));
        btBatal.setText("Batal");
        btBatal.setBorder(null);
        btBatal.addActionListener(this::btBatalActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btCatat, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCatat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jLabel10.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(202, 196, 212));
        jLabel10.setText("Catatan(Opsional)");

        jScrollPane1.setBorder(null);

        jTextArea1.setBackground(new java.awt.Color(32, 32, 41));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(202, 196, 212));
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 108, 120)));
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                    .addComponent(jLabel10))
                .addGap(37, 37, 37))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                .addGap(32, 32, 32)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbListingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbListingActionPerformed
        // TODO add your handling code here:
        if (cbListing.getSelectedIndex() <= 0) {
            tfTotal.setText("");
            return;
        }
        
        try {
            String selectedItem = cbListing.getSelectedItem().toString();
            String idListing = selectedItem.split(" - ")[0]; 
            
            // Ambil harga dari DB berdasarkan ID listing terpilih
            long harga = transaksiRepo.getHargaById(idListing);
            
            if (harga > 0) {
                tfTotal.setText(String.valueOf(harga));
            } else {
                tfTotal.setText("");
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Gagal mengambil harga listing", e);
        }
    }//GEN-LAST:event_cbListingActionPerformed

    private void tfIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfIdActionPerformed

    private void btBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBatalActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btBatalActionPerformed

    private void btCatatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCatatActionPerformed
        if (cbListing.getSelectedIndex() == 0 ||
            cbMetode.getSelectedIndex() == 0 ||
            dcTanggal.getDate() == null) {

                JOptionPane.showMessageDialog(
                this,
                "Lengkapi semua data terlebih dahulu!"
            );
            return;
        }
        
        try {
            String idTransaksi = tfId.getText();
            
            String selectedListing = cbListing.getSelectedItem().toString();
            String idListing = selectedListing.split(" - ")[0];
            String idPembeli = currentUser.getIdUser();

            int totalHarga = Integer.parseInt(tfTotal.getText());

            String metodeBayar = cbMetode.getSelectedItem().toString();

            String status = "PENDING";

            String catatan = jTextArea1.getText();

            java.sql.Date tanggal = new java.sql.Date(
                dcTanggal.getDate().getTime()
            );
            
            transaksi t = new transaksi();

            t.setIdTransaksi(idTransaksi);
            t.setIdListing(idListing);
            t.setIdPembeli(idPembeli);
            t.setTotalHarga(totalHarga);
            t.setMetodePembayaran(metodeBayar);
            t.setStatus(status);
            t.setCatatan(catatan);
            t.setTanggalTransaksi(tanggal);
            
            boolean berhasil = transaksiRepo.insertTransaction(t);

            if (berhasil) {

                JOptionPane.showMessageDialog(
                    this,
                    "Transaksi berhasil disimpan!"
                );
                
                transaksiView.loadTableData("");

                this.dispose();

                tfId.setText(transaksiRepo.generateNewTransactionId());

                cbListing.setSelectedIndex(0);
                cbMetode.setSelectedIndex(0);
                tfTotal.setText("");
                jTextArea1.setText("");
                dcTanggal.setDate(new java.util.Date());
                
                cbListing.requestFocus();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Gagal menyimpan transaksi!"
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error : " + e.getMessage()
            );

            e.printStackTrace();
        }
    }//GEN-LAST:event_btCatatActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(() -> new TransaksiForm(currentUser).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBatal;
    private javax.swing.JButton btCatat;
    private javax.swing.JComboBox<String> cbListing;
    private javax.swing.JComboBox<String> cbMetode;
    private com.toedter.calendar.JDateChooser dcTanggal;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField tfId;
    private javax.swing.JTextField tfTotal;
    // End of variables declaration//GEN-END:variables
}
