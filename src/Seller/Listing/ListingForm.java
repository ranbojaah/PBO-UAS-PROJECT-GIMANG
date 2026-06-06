/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Seller.Listing;

import Admin.Listing.ListingView;
import Connection.Koneksi;
import entity.listing;
import entity.user;
import implement.ListingImpl;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

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
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.ImageIcon;

/**
 *
 * @author Asus
 */
public class ListingForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ListingForm.class.getName());
    
    private user currentUser;
    private ListingView parentListingView;
    private boolean isEditMode = false;
    private listing editListing = null;
    /**
     * Creates new form ListingForm
     */
    public ListingForm(user usr, ListingView parent, listing l) {
        initComponents();
        this.setLocationRelativeTo(this);
        this.currentUser = usr;
        this.parentListingView = parent;
        
        styleComboBox(cbGame);
        styleComboBox(cbKondisi);
        styleComboBox(cbStatus);
        
        loadGameCombo();
        
        taDeskripsi.setLineWrap(true);
        taDeskripsi.setWrapStyleWord(true);
        
        dcTanggal.setDate(new java.util.Date());
        
        if (l != null) {
            isEditMode = true;
            editListing = l;
            populateFormForEdit(l); // pilih item game, set status, set id sesuai DB
        } else {
            // Mode tambah
            try {
                tfId.setText(generateNewListingId()); // hanya mode tambah
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal generate ID: " + e.getMessage());
            }
            tfId.setEditable(false);
            cbStatus.setModel(new DefaultComboBoxModel<>(new String[]{"PROSES"}));
            cbStatus.setSelectedIndex(0);
            cbStatus.setEnabled(false);
        }

        cbKondisi.setModel(new DefaultComboBoxModel<>(new String[]{"BARU", "BEKAS"}));
        cbKondisi.setSelectedIndex(0);

        btBatal.addActionListener(evt -> this.dispose());
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
    
    private void populateFormForEdit(listing l) {
        tfId.setText(l.getListingId());
        tfId.setEditable(false);

        dcTanggal.setDate(l.getListedDate());
        tfTotal.setText(String.valueOf(l.getPrice()));
        cbKondisi.setSelectedItem(l.getCondition());

        // Set game combo
        for (int i = 0; i < cbGame.getItemCount(); i++) {
            String item = cbGame.getItemAt(i);
            if (item.startsWith(l.getGameId() + "|")) {
                cbGame.setSelectedIndex(i);
                break;
            }
        }

        cbStatus.setModel(new DefaultComboBoxModel<>(new String[]{l.getStatus()}));
        cbStatus.setEnabled(false);

        taDeskripsi.setText(l.getDescription());
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
    
    private void loadGameCombo() {
        DefaultComboBoxModel<String> gameModel = new DefaultComboBoxModel<>();
        gameModel.addElement("Pilih game dari katalog"); // placeholder
        try {
            String sql = "SELECT game_id, title FROM games WHERE is_delete = FALSE ORDER BY title ASC";
            PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                // simpan di model: id|title
                gameModel.addElement(rs.getString("game_id") + "|" + rs.getString("title"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load game: " + e.getMessage());
        }
        cbGame.setModel(gameModel);
        cbGame.setSelectedIndex(0);
    }
    
    private String generateNewListingId() throws SQLException {
        String sql = "SELECT listing_id FROM listings ORDER BY listing_id DESC LIMIT 1";
        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            String lastId = rs.getString("listing_id");
            int num = Integer.parseInt(lastId.substring(1));
            num++;
            return String.format("L%03d", num);
        } else {
            return "L001";
        }
    }
    
    private void pasangListing() {
        try {
            String gameSel = (String) cbGame.getSelectedItem();
            if (gameSel.equals("Pilih game dari katalog")) {
                JOptionPane.showMessageDialog(this, "Silakan pilih game dari katalog");
                return;
            }

            String[] parts = gameSel.split("\\|");
            String gameId = parts[0];

            String kondisi = (String) cbKondisi.getSelectedItem();
            String deskripsi = taDeskripsi.getText();
            int harga;

            try {
                harga = Integer.parseInt(tfTotal.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Harga harus angka!");
                return;
            }

            ListingImpl li = new ListingImpl();

            if (isEditMode) {
                if (editListing.getStatus().equalsIgnoreCase("TERJUAL")) {
                    JOptionPane.showMessageDialog(this, "Listing TERJUAL tidak bisa diedit!");
                    return;
                }

                // hanya kolom yang boleh diubah
                editListing.setGameId(gameId);
                editListing.setCondition(kondisi);
                editListing.setPrice(harga);
                editListing.setDescription(deskripsi);
                editListing.setListedDate(new java.sql.Date(dcTanggal.getDate().getTime()));
                // status TIDAK dirubah

                li.update(editListing);
                JOptionPane.showMessageDialog(this, "Listing berhasil diperbarui!");
            } else {
                // mode tambah
                listing l = new listing();
                l.setListingId(tfId.getText());
                l.setGameId(gameId);
                l.setSellerId(currentUser.getIdUser());
                l.setPrice(harga);
                l.setCondition(kondisi);
                l.setStatus("PROSES"); // default status
                l.setDescription(deskripsi);
                l.setListedDate(new java.sql.Date(dcTanggal.getDate().getTime()));

                li.insert(l);
                JOptionPane.showMessageDialog(this, "Listing berhasil ditambahkan!");
            }

            this.dispose();
            if (parentListingView != null) {
                parentListingView.loadListing();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan listing: " + e.getMessage());
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btPasang = new javax.swing.JButton();
        btBatal = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tfId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        dcTanggal = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbGame = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbKondisi = new javax.swing.JComboBox<>();
        tfTotal = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taDeskripsi = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Form Listing");

        jPanel1.setBackground(new java.awt.Color(18, 18, 28));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(73, 69, 82)));

        jPanel2.setBackground(new java.awt.Color(27, 27, 37));

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(206, 189, 255));
        jLabel1.setText("Catat/Edit Listing");

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(202, 196, 212));
        jLabel2.setText("Pasang game untuk dijual");

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
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(21, 21, 21))
        );

        jPanel3.setBackground(new java.awt.Color(27, 27, 37));

        btPasang.setBackground(new java.awt.Color(167, 139, 250));
        btPasang.setFont(new java.awt.Font("Verdana", 1, 13)); // NOI18N
        btPasang.setForeground(new java.awt.Color(60, 25, 137));
        btPasang.setText("Pasang Listing");
        btPasang.setBorder(null);
        btPasang.addActionListener(this::btPasangActionPerformed);

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
                .addComponent(btPasang, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btPasang, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(18, 18, 28));

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(202, 196, 212));
        jLabel3.setText("ID Listing");

        tfId.setBackground(new java.awt.Color(32, 32, 41));
        tfId.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        tfId.setForeground(new java.awt.Color(202, 196, 212));
        tfId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 108, 120)));
        tfId.addActionListener(this::tfIdActionPerformed);

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(202, 196, 212));
        jLabel4.setText("Tanggal Listing");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(tfId, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(dcTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41))
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
                    .addComponent(tfId, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(dcTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(18, 18, 28));

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(202, 196, 212));
        jLabel5.setText("Game*");

        cbGame.setBackground(new java.awt.Color(32, 32, 41));
        cbGame.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        cbGame.setForeground(new java.awt.Color(202, 196, 212));
        cbGame.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih game dari katalog", "Item 1", "Item 2", "Item 3" }));
        cbGame.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 108, 120)));
        cbGame.addActionListener(this::cbGameActionPerformed);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(cbGame, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(cbGame, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        jLabel8.setText("Kondisi");

        cbKondisi.setBackground(new java.awt.Color(32, 32, 41));
        cbKondisi.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        cbKondisi.setForeground(new java.awt.Color(202, 196, 212));
        cbKondisi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Kondisi", "Baru", "Bekas" }));
        cbKondisi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 108, 120)));

        tfTotal.setBackground(new java.awt.Color(32, 32, 41));
        tfTotal.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
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
                        .addComponent(cbKondisi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(cbKondisi, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(jLabel9)
                    .addContainerGap(45, Short.MAX_VALUE)))
        );

        jPanel7.setBackground(new java.awt.Color(18, 18, 28));

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(202, 196, 212));
        jLabel7.setText("Status");

        cbStatus.setBackground(new java.awt.Color(32, 32, 41));
        cbStatus.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        cbStatus.setForeground(new java.awt.Color(202, 196, 212));
        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tersedia", "Proses", "Terjual" }));
        cbStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 108, 120)));
        cbStatus.addActionListener(this::cbStatusActionPerformed);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(cbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(18, 18, 28));

        jLabel10.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(202, 196, 212));
        jLabel10.setText("Deskripsi listing");

        jScrollPane1.setBorder(null);

        taDeskripsi.setBackground(new java.awt.Color(32, 32, 41));
        taDeskripsi.setColumns(20);
        taDeskripsi.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        taDeskripsi.setForeground(new java.awt.Color(202, 196, 212));
        taDeskripsi.setRows(5);
        taDeskripsi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 108, 120)));
        jScrollPane1.setViewportView(taDeskripsi);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jScrollPane1))
                .addGap(38, 38, 38))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addGap(38, 38, 38))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbStatusActionPerformed

    private void cbGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbGameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbGameActionPerformed

    private void tfIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfIdActionPerformed

    private void btPasangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPasangActionPerformed
        // TODO add your handling code here:
        pasangListing();
    }//GEN-LAST:event_btPasangActionPerformed

    private void btBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBatalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btBatalActionPerformed

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
//        java.awt.EventQueue.invokeLater(() -> new ListingForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBatal;
    private javax.swing.JButton btPasang;
    private javax.swing.JComboBox<String> cbGame;
    private javax.swing.JComboBox<String> cbKondisi;
    private javax.swing.JComboBox<String> cbStatus;
    private com.toedter.calendar.JDateChooser dcTanggal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea taDeskripsi;
    private javax.swing.JTextField tfId;
    private javax.swing.JTextField tfTotal;
    // End of variables declaration//GEN-END:variables
}
