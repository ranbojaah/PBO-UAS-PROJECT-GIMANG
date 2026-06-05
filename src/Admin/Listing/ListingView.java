/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin.Listing;

import Dashboard.DashboardView;
import Seller.Listing.ListingForm;
import entity.listing;
import entity.user;
import implement.ListingImpl;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;
import javax.swing.ImageIcon;

/**
 *
 * @author Asus
 */
public class ListingView extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(ListingView.class.getName());

    private user currentUser;
    private ListingImpl listingImpl = new ListingImpl();
    private String selectedListingId = null;
    private String selectedStatus = "SEMUA";

    /**
     * Creates new form ListingView
     */
    public ListingView(user usr) {
        initComponents();
        this.setLocationRelativeTo(this);
        this.currentUser = usr;

        styleTable();
        styleToggleButton();
        setupActionPanel();
        setupRole();
        loadListing();

        tfDeskripsi.setLineWrap(true);
        tfDeskripsi.setWrapStyleWord(true);
        tfDeskripsi.setEditable(false);
        tfDeskripsi.setOpaque(false);
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
        tbListing.setRowHeight(48);

        tbListing.setBackground(new Color(10, 10, 20));
        tbListing.setForeground(Color.WHITE);

        tbListing.setSelectionBackground(new Color(24, 24, 32));

        tbListing.setSelectionForeground(Color.WHITE);

        tbListing.setSelectionForeground(Color.WHITE);

        tbListing.setGridColor(new Color(30, 30, 40));

        tbListing.setShowGrid(false);

        tbListing.setIntercellSpacing(
                new Dimension(0, 0));

        JTableHeader header = tbListing.getTableHeader();

        header.setOpaque(false);

        header.setDefaultRenderer(
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column) {

                        JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                                table,
                                value,
                                isSelected,
                                hasFocus,
                                row,
                                column);

                        lbl.setBackground(
                                new Color(24, 24, 32));

                        lbl.setForeground(
                                new Color(180, 180, 200));

                        lbl.setBorder(
                                BorderFactory.createCompoundBorder(

                                        BorderFactory.createMatteBorder(
                                                0, 0, 1, 1,
                                                new Color(45, 45, 60)),

                                        BorderFactory.createEmptyBorder(
                                                12, 12, 12, 12)));

                        return lbl;
                    }
                });
    }

    private void styleToggleButton() {

        ButtonGroup group = new ButtonGroup();

        JToggleButton[] buttons = {
                btnSemua,
                btnTersedia,
                btnTerjual,
                btnProses
        };

        for (JToggleButton btn : buttons) {

            group.add(btn);

            Color defaultBg = btn.getBackground();
            Color defaultFg = btn.getForeground();

            // BORDER TETAP UNTUK SEMUA STATE
            Border fixedBorder = BorderFactory.createCompoundBorder(

                    BorderFactory.createLineBorder(
                            new Color(55, 55, 70),
                            1,
                            true),

                    BorderFactory.createEmptyBorder(
                            6, 16, 6, 16));

            btn.setBorder(fixedBorder);

            btn.setFocusPainted(false);

            btn.addItemListener(e -> {

                if (btn.isSelected()) {

                    btn.setBackground(
                            new Color(167, 139, 250));

                    btn.setForeground(
                            new Color(17, 17, 29));

                } else {

                    btn.setBackground(defaultBg);

                    btn.setForeground(defaultFg);

                }

            });
        }

        btnSemua.setSelected(true);
    }

    private void setupActionPanel() {

        panelEdit.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 0, 10, 0));

        panelVerifikasi.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 0, 10, 0));

    }

    private void fillTable(List<listing> data) {

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("GAME");
        model.addColumn("SELLER");
        model.addColumn("HARGA");
        model.addColumn("KONDISI");
        model.addColumn("STATUS");

        for (listing l : data) {
            model.addRow(new Object[] {
                    l.getListingId(),
                    l.getGameTitle(),
                    l.getSellerUsername(),
                    l.getPrice(),
                    l.getCondition(),
                    l.getStatus()
            });
        }

        tbListing.setModel(model);

        lbTotalListing.setText(data.size() + " Listing Tercatat");
        DefaultTableCellRenderer bodyRenderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column);

                lbl.setBorder(
                        BorderFactory.createEmptyBorder(
                                12, 12, 12, 12));

                return lbl;
            }
        };
        
        

        for (int i = 0; i < tbListing.getColumnCount(); i++) {
            String columnName = tbListing.getColumnName(i).toUpperCase();

            if (columnName.equals("STATUS")) {
                tbListing.getColumnModel().getColumn(i).setCellRenderer(new StatusTableCellRenderer());
            } else if (columnName.equals("KONDISI")) {
                tbListing.getColumnModel().getColumn(i).setCellRenderer(new ConditionTableCellRenderer());
            } else {
                tbListing.getColumnModel().getColumn(i).setCellRenderer(bodyRenderer);
            }
        }
    }

    private void setupRole() {
        String role = currentUser.getRole();

        if (role.equalsIgnoreCase("seller")) {
            btTambah.setVisible(true);
            btnEdit.setVisible(true);

            btnVerifikasi.setVisible(false);
            btnHapus.setVisible(false);
        } else if (role.equalsIgnoreCase("admin")) {
            btTambah.setVisible(false);
            btnEdit.setVisible(false);

            btnVerifikasi.setVisible(true);
            btnHapus.setVisible(true);
        }
    }

    public void loadListing() {
        try {
            List<listing> data;

            String role = currentUser.getRole();

            if (role.equalsIgnoreCase("admin")) {
                data = listingImpl.getAll();
            } else {
                data = listingImpl.getBySeller(currentUser.getIdUser());
            }

            fillTable(data);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal memuat data listing: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applySearchAndFilter() {
        try {
            String keyword = tfCari.getText();
            String role = currentUser.getRole();
            String sellerId = currentUser.getIdUser();

            List<listing> data = listingImpl.search(
                    keyword,
                    selectedStatus,
                    role,
                    sellerId);

            fillTable(data);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal mencari data listing: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showDetail(String listingId) {
        try {
            listing l = listingImpl.getById(listingId);

            if (l == null) {
                return;
            }

            detGame.setText(l.getGameTitle());
            lbId.setText(l.getListingId());
            lbStatus.setText(l.getStatus());
            lbTanggal.setText(String.valueOf(l.getListedDate()));
            lbHarga.setText("Rp " + l.getPrice());
            lbUser.setText(l.getSellerUsername());
            lbKondisi.setText(l.getCondition());
            tfDeskripsi.setText(l.getDescription());

            selectedListingId = l.getListingId();
            
            String status = l.getStatus().toUpperCase();
            lbStatus.setText(status);
            
            switch (status) {
                case "TERSEDIA":
                    lbStatus.setForeground(new Color(46, 204, 113));
                    break;
                case "TERJUAL":
                    lbStatus.setForeground(new Color(231, 76, 60));
                    break;
                case "PROSES":
                    lbStatus.setForeground(new Color(241, 196, 15));
                    break;
                default:
                    lbStatus.setForeground(Color.WHITE);
                    break;
            }
            
            String kondisi = l.getCondition().toUpperCase();
            lbKondisi.setText(kondisi);
            
            switch (kondisi) {
                case "BARU":
                    lbKondisi.setForeground(new Color(46, 204, 113));
                    break;
                case "BEKAS":
                    lbKondisi.setForeground(new Color(241, 196, 15));
                    break;
                default:
                    lbKondisi.setForeground(Color.WHITE);
                    break;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal menampilkan detail listing: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private class StatusTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            
            lbl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            
            if (value != null) {
                String status = value.toString().toUpperCase();
                
                switch (status) {
                    case "TERSEDIA":
                        lbl.setForeground(new Color(46, 204, 113)); 
                        break;
                    case "TERJUAL":
                        lbl.setForeground(new Color(231, 76, 60));
                        break;
                    case "PROSES":
                        lbl.setForeground(new Color(241, 196, 15)); 
                        break;
                    default:
                        lbl.setForeground(Color.WHITE);
                        break;
                }
            }
            
            if (isSelected) {
                lbl.setBackground(table.getSelectionBackground());
            } else {
                lbl.setBackground(table.getBackground());
            }
            
            return lbl;
        }
    }
    
    private class ConditionTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            
            lbl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            
            if (value != null) {
                String kondisi = value.toString().toUpperCase();
                
                switch (kondisi) {
                    case "BARU":
                        lbl.setForeground(new Color(46, 204, 113));
                        break;
                    case "BEKAS":
                        lbl.setForeground(new Color(241, 196, 15));
                        break;
                    default:
                        lbl.setForeground(Color.WHITE);
                        break;
                }
            }
            
            if (isSelected) {
                lbl.setBackground(table.getSelectionBackground());
            } else {
                lbl.setBackground(table.getBackground());
            }
            
            return lbl;
        }
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbTotalListing = new javax.swing.JLabel();
        tfCari = new javax.swing.JTextField();
        btTambah = new javax.swing.JButton();
        btDashboard = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnSemua = new javax.swing.JToggleButton();
        btnTersedia = new javax.swing.JToggleButton();
        btnTerjual = new javax.swing.JToggleButton();
        btnProses = new javax.swing.JToggleButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbListing = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        panelActionContainer = new javax.swing.JPanel();
        panelEdit = new javax.swing.JPanel();
        btnEdit = new javax.swing.JButton();
        panelVerifikasi = new javax.swing.JPanel();
        btnVerifikasi = new javax.swing.JButton();
        panelHapus = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        detGame = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lbId = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lbStatus = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbTanggal = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lbHarga = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lbKondisi = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tfDeskripsi = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();
        lbUser = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Listing");

        jPanel1.setBackground(new java.awt.Color(41, 41, 52));

        jPanel2.setBackground(new java.awt.Color(41, 41, 52));

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Listing");

        lbTotalListing.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lbTotalListing.setForeground(new java.awt.Color(148, 163, 184));
        lbTotalListing.setText("0 Listing Tercatat");

        tfCari.setBackground(new java.awt.Color(30, 30, 48));
        tfCari.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        tfCari.setForeground(new java.awt.Color(148, 163, 184));
        tfCari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 108, 120)));
        tfCari.setMargin(new java.awt.Insets(2, 10, 2, 6));
        tfCari.addActionListener(this::tfCariActionPerformed);
        tfCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfCariKeyReleased(evt);
            }
        });

        btTambah.setBackground(new java.awt.Color(167, 139, 250));
        btTambah.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btTambah.setForeground(new java.awt.Color(17, 17, 29));
        btTambah.setText("Tambah Listing");
        btTambah.setBorder(null);
        btTambah.addActionListener(this::btTambahActionPerformed);

        btDashboard.setBackground(new java.awt.Color(167, 139, 250));
        btDashboard.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btDashboard.setForeground(new java.awt.Color(17, 17, 29));
        btDashboard.setText("Dashboard");
        btDashboard.setBorder(null);
        btDashboard.addActionListener(this::btDashboardActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(38, 38, 38)
                .addComponent(lbTotalListing)
                .addGap(88, 88, 88)
                .addComponent(tfCari, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lbTotalListing)
                    .addComponent(tfCari, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8))
        );

        jPanel3.setBackground(new java.awt.Color(18, 18, 28));

        jPanel5.setBackground(new java.awt.Color(18, 18, 28));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 8));

        btnSemua.setBackground(new java.awt.Color(18, 18, 28));
        btnSemua.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btnSemua.setForeground(new java.awt.Color(148, 163, 184));
        btnSemua.setText("Semua Listing");
        btnSemua.setBorder(null);
        btnSemua.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSemua.addActionListener(this::btnSemuaActionPerformed);
        jPanel5.add(btnSemua);

        btnTersedia.setBackground(new java.awt.Color(18, 18, 28));
        btnTersedia.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btnTersedia.setForeground(new java.awt.Color(148, 163, 184));
        btnTersedia.setText("Tersedia");
        btnTersedia.setBorder(null);
        btnTersedia.addActionListener(this::btnTersediaActionPerformed);
        jPanel5.add(btnTersedia);

        btnTerjual.setBackground(new java.awt.Color(18, 18, 28));
        btnTerjual.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btnTerjual.setForeground(new java.awt.Color(148, 163, 184));
        btnTerjual.setText("Terjual");
        btnTerjual.setBorder(null);
        btnTerjual.addActionListener(this::btnTerjualActionPerformed);
        jPanel5.add(btnTerjual);

        btnProses.setBackground(new java.awt.Color(18, 18, 28));
        btnProses.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btnProses.setForeground(new java.awt.Color(148, 163, 184));
        btnProses.setText("Proses");
        btnProses.setBorder(null);
        btnProses.addActionListener(this::btnProsesActionPerformed);
        jPanel5.add(btnProses);

        jScrollPane2.setBackground(new java.awt.Color(18, 18, 28));
        jScrollPane2.setBorder(null);

        tbListing.setBackground(new java.awt.Color(18, 18, 28));
        tbListing.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "GAME", "SELLER", "HARGA", "KONDISI", "STATUS"
            }
        ));
        tbListing.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListingMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbListing);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(41, 41, 52));

        jPanel4.setBackground(new java.awt.Color(31, 31, 41));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(73, 69, 82)));

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(148, 163, 184));
        jLabel3.setText("AKSI LISTING");

        panelActionContainer.setBackground(new java.awt.Color(31, 31, 41));
        panelActionContainer.setLayout(new javax.swing.BoxLayout(panelActionContainer, javax.swing.BoxLayout.Y_AXIS));

        panelEdit.setBackground(new java.awt.Color(31, 31, 41));

        btnEdit.setBackground(new java.awt.Color(31, 31, 41));
        btnEdit.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit Lisiting");
        btnEdit.setToolTipText("");
        btnEdit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(73, 69, 82)));
        btnEdit.addActionListener(this::btnEditActionPerformed);

        javax.swing.GroupLayout panelEditLayout = new javax.swing.GroupLayout(panelEdit);
        panelEdit.setLayout(panelEditLayout);
        panelEditLayout.setHorizontalGroup(
            panelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
        );
        panelEditLayout.setVerticalGroup(
            panelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );

        panelActionContainer.add(panelEdit);

        panelVerifikasi.setBackground(new java.awt.Color(31, 31, 41));

        btnVerifikasi.setBackground(new java.awt.Color(167, 139, 250));
        btnVerifikasi.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btnVerifikasi.setForeground(new java.awt.Color(17, 17, 29));
        btnVerifikasi.setText("Verifikasi");
        btnVerifikasi.setBorder(null);
        btnVerifikasi.addActionListener(this::btnVerifikasiActionPerformed);

        javax.swing.GroupLayout panelVerifikasiLayout = new javax.swing.GroupLayout(panelVerifikasi);
        panelVerifikasi.setLayout(panelVerifikasiLayout);
        panelVerifikasiLayout.setHorizontalGroup(
            panelVerifikasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnVerifikasi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelVerifikasiLayout.setVerticalGroup(
            panelVerifikasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnVerifikasi, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );

        panelActionContainer.add(panelVerifikasi);

        panelHapus.setBackground(new java.awt.Color(31, 31, 41));

        btnHapus.setBackground(new java.awt.Color(31, 31, 41));
        btnHapus.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(248, 113, 113));
        btnHapus.setText("Hapus Listing");
        btnHapus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(248, 113, 113)));
        btnHapus.addActionListener(this::btnHapusActionPerformed);

        javax.swing.GroupLayout panelHapusLayout = new javax.swing.GroupLayout(panelHapus);
        panelHapus.setLayout(panelHapusLayout);
        panelHapusLayout.setHorizontalGroup(
            panelHapusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
        );
        panelHapusLayout.setVerticalGroup(
            panelHapusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );

        panelActionContainer.add(panelHapus);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelActionContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel3)
                .addGap(26, 26, 26)
                .addComponent(panelActionContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jScrollPane3.setBorder(null);

        jPanel7.setBackground(new java.awt.Color(41, 41, 52));

        detGame.setFont(new java.awt.Font("Verdana", 1, 16)); // NOI18N
        detGame.setForeground(new java.awt.Color(226, 232, 240));
        detGame.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        detGame.setText("Detail - Nama Game");

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(148, 163, 184));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Id Listing");

        lbId.setBackground(new java.awt.Color(167, 139, 250));
        lbId.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lbId.setForeground(new java.awt.Color(167, 139, 250));
        lbId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel7.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(148, 163, 184));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Status");

        lbStatus.setBackground(new java.awt.Color(167, 139, 250));
        lbStatus.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lbStatus.setForeground(new java.awt.Color(16, 185, 129));
        lbStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel9.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(148, 163, 184));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("INFORMASI LISTING");

        jLabel10.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(148, 163, 184));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Dibuat Pada");

        lbTanggal.setBackground(new java.awt.Color(167, 139, 250));
        lbTanggal.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lbTanggal.setForeground(new java.awt.Color(226, 232, 240));
        lbTanggal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel12.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(148, 163, 184));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("HARGA");

        jLabel13.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(148, 163, 184));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Harga Satuan");

        lbHarga.setBackground(new java.awt.Color(167, 139, 250));
        lbHarga.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lbHarga.setForeground(new java.awt.Color(16, 185, 129));
        lbHarga.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel17.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(148, 163, 184));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("INFORMASI PENJUAL");

        lbKondisi.setBackground(new java.awt.Color(167, 139, 250));
        lbKondisi.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lbKondisi.setForeground(new java.awt.Color(226, 232, 240));
        lbKondisi.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbKondisi.setText(" ");

        jLabel15.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(148, 163, 184));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("Kondisi");

        jLabel14.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(148, 163, 184));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("DESKRIPSI");

        jScrollPane1.setBackground(new java.awt.Color(41, 41, 52));
        jScrollPane1.setBorder(null);

        tfDeskripsi.setBackground(new java.awt.Color(41, 41, 52));
        tfDeskripsi.setColumns(20);
        tfDeskripsi.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        tfDeskripsi.setForeground(new java.awt.Color(226, 232, 240));
        tfDeskripsi.setRows(5);
        tfDeskripsi.setBorder(null);
        jScrollPane1.setViewportView(tfDeskripsi);

        jLabel16.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(148, 163, 184));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("Username");

        lbUser.setBackground(new java.awt.Color(167, 139, 250));
        lbUser.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lbUser.setForeground(new java.awt.Color(16, 185, 129));
        lbUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(detGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbKondisi, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbId, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lbStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)))))
                .addGap(36, 36, 36)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbUser, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(detGame)
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(lbId))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(lbTanggal)))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lbStatus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel16)
                                        .addComponent(lbUser))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addGap(28, 28, 28)))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(lbKondisi)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(lbHarga))
                                .addGap(133, 133, 133)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(jPanel7);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(50, 50, 50))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDashboardActionPerformed
        // TODO add your handling code here:
        new DashboardView().setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_btDashboardActionPerformed

    private void tfCariActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tfCariActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_tfCariActionPerformed

    private void btnTerjualActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTerjualActionPerformed
        // TODO add your handling code here:
        selectedStatus = "TERJUAL";
        applySearchAndFilter();
    }// GEN-LAST:event_btnTerjualActionPerformed

    private void btnProsesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnProsesActionPerformed
        // TODO add your handling code here:
        selectedStatus = "PROSES";
        applySearchAndFilter();

    }// GEN-LAST:event_btnProsesActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        if (selectedListingId == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih listing terlebih dahulu!");
            return;
        }

        try {
            listing l = listingImpl.getById(selectedListingId);
            if (l == null) {
                JOptionPane.showMessageDialog(this, "Listing tidak ditemukan!");
                return;
            }

            // Cek status TERJUAL di ListingView
            if (l.getStatus().equalsIgnoreCase("TERJUAL")) {
                JOptionPane.showMessageDialog(this, "Listing TERJUAL tidak bisa diedit!");
                return; // jangan panggil ListingForm
            }

            // Kalau bukan TERJUAL, buka form edit
            ListingForm form = new ListingForm(currentUser, this, l);
            form.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load listing: " + e.getMessage());
        }
    }// GEN-LAST:event_btnEditActionPerformed

    private void tbListingMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tbListingMouseClicked
        // TODO add your handling code here:
        int row = tbListing.getSelectedRow();
        if (row >= 0) {
            selectedListingId = tbListing.getValueAt(row, 0).toString();

            showDetail(selectedListingId);
        }
    }// GEN-LAST:event_tbListingMouseClicked

    private void tfCariKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_tfCariKeyReleased
        // TODO add your handling code here:
        applySearchAndFilter();
    }// GEN-LAST:event_tfCariKeyReleased

    private void btnSemuaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSemuaActionPerformed
        // TODO add your handling code here:
        selectedStatus = "SEMUA";
        applySearchAndFilter();
    }// GEN-LAST:event_btnSemuaActionPerformed

    private void btnTersediaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTersediaActionPerformed
        // TODO add your handling code here:
        selectedStatus = "TERSEDIA";
        applySearchAndFilter();
    }// GEN-LAST:event_btnTersediaActionPerformed

    private void btnVerifikasiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnVerifikasiActionPerformed
        // TODO add your handling code here:
        if (selectedListingId == null) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih listing terlebih dahulu",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            // Ambil data listing
            listing l = listingImpl.getById(selectedListingId);

            if (l == null) {
                JOptionPane.showMessageDialog(this,
                        "Listing tidak ditemukan",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cek status, hanya ubah PROSES menjadi TERSEDIA
            if (l.getStatus().equalsIgnoreCase("PROSES")) {
                listingImpl.updateStatus(selectedListingId, "TERSEDIA");
                JOptionPane.showMessageDialog(this,
                        "Listing berhasil diverifikasi",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Hanya listing dengan status PROSES yang bisa diverifikasi",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // Reload tabel
            applySearchAndFilter();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memperbarui status: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_btnVerifikasiActionPerformed

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btTambahActionPerformed
        // TODO add your handling code here:
        ListingForm form = new ListingForm(currentUser, this, null);
        form.setVisible(true);
    }// GEN-LAST:event_btTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        if (selectedListingId == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih listing terlebih dahulu!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus listing ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return; // batal
        }

        try {
            listing l = listingImpl.getById(selectedListingId);
            if (l == null) {
                JOptionPane.showMessageDialog(this, "Listing tidak ditemukan!");
                return;
            }

            // opsional: cek status TERJUAL, tidak boleh dihapus
            if (l.getStatus().equalsIgnoreCase("TERJUAL")) {
                JOptionPane.showMessageDialog(this, "Listing TERJUAL tidak bisa dihapus!");
                return;
            }

            listingImpl.delete(selectedListingId);
            JOptionPane.showMessageDialog(this, "Listing berhasil dihapus!");

            // reload tabel
            loadListing();
            selectedListingId = null; // reset selection

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus listing: " + e.getMessage());
        }
    }// GEN-LAST:event_btnHapusActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        // java.awt.EventQueue.invokeLater(() -> new ListingView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDashboard;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JToggleButton btnProses;
    private javax.swing.JToggleButton btnSemua;
    private javax.swing.JToggleButton btnTerjual;
    private javax.swing.JToggleButton btnTersedia;
    private javax.swing.JButton btnVerifikasi;
    private javax.swing.JLabel detGame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbHarga;
    private javax.swing.JLabel lbId;
    private javax.swing.JLabel lbKondisi;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JLabel lbTanggal;
    private javax.swing.JLabel lbTotalListing;
    private javax.swing.JLabel lbUser;
    private javax.swing.JPanel panelActionContainer;
    private javax.swing.JPanel panelEdit;
    private javax.swing.JPanel panelHapus;
    private javax.swing.JPanel panelVerifikasi;
    private javax.swing.JTable tbListing;
    private javax.swing.JTextField tfCari;
    private javax.swing.JTextArea tfDeskripsi;
    // End of variables declaration//GEN-END:variables
}