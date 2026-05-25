/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin.Transaksi;

import Buyyer.Transaksi.TransaksiForm;
import Dashboard.DashboardView;
import entity.user;
import implement.TransaksiImpl;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
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
public class TransaksiView extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TransaksiView.class.getName());

    private user currentUser;
    private final TransaksiImpl transImpl = new TransaksiImpl();
    
    /**
     * Creates new form TransaksiView
     */
    public TransaksiView(user usr) {
        this.currentUser = usr;
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Transaction");
        
        styleTable();
        aturHakAkses();
        loadTableData(""); 
        initTableEvent();
    }
    
    private void aturHakAkses() {
        if (currentUser != null) {
            String role = currentUser.getRole();

            // JIKA YANG LOGIN ADALAH BUYER
            if (role.equalsIgnoreCase("buyer")) {
                btTambah.setVisible(true);
                btHapus.setVisible(false);
                btKonfirmasi.setVisible(false);

            // JIKA YANG LOGIN ADALAH ADMIN
            } else if (role.equalsIgnoreCase("admin")) {
                btTambah.setVisible(false);
                btHapus.setVisible(true);   
                btKonfirmasi.setVisible(true);

            }else if (role.equalsIgnoreCase("seller")) {
                btTambah.setVisible(false);
                btHapus.setVisible(false);     
                btKonfirmasi.setVisible(true);
            }
            else {
                btTambah.setVisible(false);
                btHapus.setVisible(false);  
                btKonfirmasi.setVisible(false);
            }
        } else {
            btTambah.setVisible(false);
            btHapus.setVisible(false);
            btKonfirmasi.setVisible(false);
        }
    }
    
    public void loadTableData(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tbTransaksi.getModel();
        model.setRowCount(0); // Kosongkan tabel terlebih dahulu
        
        try {
            List<String[]> data = transImpl.getTransactionList(currentUser.getRole(), currentUser.getIdUser());
            int jumlahTransaksi = 0;
            
            for (String[] row : data) {
                if (keyword.isEmpty() || 
                    row[0].toLowerCase().contains(keyword.toLowerCase()) || 
                    row[1].toLowerCase().contains(keyword.toLowerCase()) || 
                    row[2].toLowerCase().contains(keyword.toLowerCase())) {
                    
                    model.addRow(row);
                    jumlahTransaksi++;
                }
            }
            jLabel2.setText(jumlahTransaksi + " Transaksi Tercatat");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data transaksi: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initTableEvent() {
        tbTransaksi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tbTransaksi.getSelectedRow();
                if (selectedRow != -1) {
                    // Mengambil data dari kolom tabel JTable
                    String idTransaksi = tbTransaksi.getValueAt(selectedRow, 0).toString();
                    String gameTitle   = tbTransaksi.getValueAt(selectedRow, 1).toString();
                    String buyerName   = tbTransaksi.getValueAt(selectedRow, 2).toString();
                    String totalHarga  = tbTransaksi.getValueAt(selectedRow, 3).toString();

                    // Set text ke komponen detail
                    jLabel4.setText("Detail - " + idTransaksi);
                    jLabel6.setText(gameTitle);
                    jLabel11.setText(buyerName);

                    // PANGGIL METHOD DARI INTERFACE SECARA DINAMIS
                    try {
                        String namaSeller = transImpl.getSellerNameByTransactionId(idTransaksi);
                        jLabel8.setText(namaSeller); // Set nama seller asli ke JLabel8
                    } catch (java.sql.SQLException ex) {
                        jLabel8.setText("Gagal memuat");
                        ex.printStackTrace(); 
                    }

                    // Format Tampilan Rupiah
                    try {
                        int harga = Integer.parseInt(totalHarga);
                        jLabel14.setText(String.format("Rp %,d", harga).replace(',', '.'));
                    } catch (NumberFormatException ex) {
                        jLabel14.setText("Rp " + totalHarga);
                    }
                }
            }
        });
    }
    
    private void styleTable(){
        tbTransaksi.setRowHeight(48);

        tbTransaksi.setBackground(new Color(10,10,20));
        tbTransaksi.setForeground(Color.WHITE);

        tbTransaksi.setSelectionBackground(new Color(24,24,32));
        tbTransaksi.setSelectionForeground(Color.WHITE);

        tbTransaksi.setGridColor(new Color(30,30,40));

        tbTransaksi.setShowGrid(false);

        tbTransaksi.setIntercellSpacing(
                new Dimension(0,0));

        JTableHeader header = tbTransaksi.getTableHeader();

        header.setOpaque(false);

        header.setDefaultRenderer(
            new DefaultTableCellRenderer(){

                @Override
                public Component getTableCellRendererComponent(
                        JTable table,
                        Object value,
                        boolean isSelected,
                        boolean hasFocus,
                        int row,
                        int column){

                    JLabel lbl =
                        (JLabel) super.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            column);

                    lbl.setBackground(
                        new Color(24,24,32));

                    lbl.setForeground(
                        new Color(180,180,200));

                    lbl.setBorder(
                        BorderFactory.createCompoundBorder(

                            BorderFactory.createMatteBorder(
                                0,0,1,1,
                                new Color(45,45,60)
                            ),

                            BorderFactory.createEmptyBorder(
                                12,12,12,12
                            )
                        )
                    );

                    return lbl;
                }
            });
        
        DefaultTableCellRenderer bodyRenderer =
            new DefaultTableCellRenderer(){

                @Override
                public Component getTableCellRendererComponent(
                        JTable table,
                        Object value,
                        boolean isSelected,
                        boolean hasFocus,
                        int row,
                        int column){

                    JLabel lbl =
                        (JLabel) super.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            column);

                    lbl.setBorder(
                        BorderFactory.createEmptyBorder(
                            12,12,12,12
                        )
                    );

                    return lbl;
                }
            };

        for(int i=0; i<tbTransaksi.getColumnCount(); i++){
            tbTransaksi.getColumnModel()
                .getColumn(i)
                .setCellRenderer(bodyRenderer);
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
        tfCari = new javax.swing.JTextField();
        btTambah = new javax.swing.JButton();
        btDashboard = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbTransaksi = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        btHapus = new javax.swing.JButton();
        btKonfirmasi = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(41, 41, 52));

        jPanel2.setBackground(new java.awt.Color(41, 41, 52));

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Transaksi");

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(148, 163, 184));
        jLabel2.setText("0 transaksi tercatat");

        tfCari.setBackground(new java.awt.Color(30, 30, 48));
        tfCari.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        tfCari.setForeground(new java.awt.Color(148, 163, 184));
        tfCari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 108, 120)));
        tfCari.setMargin(new java.awt.Insets(2, 10, 2, 6));
        tfCari.addActionListener(this::tfCariActionPerformed);

        btTambah.setBackground(new java.awt.Color(167, 139, 250));
        btTambah.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btTambah.setForeground(new java.awt.Color(17, 17, 29));
        btTambah.setText("+ Catat Transaksi");
        btTambah.setBorder(null);
        btTambah.addActionListener(this::btTambahActionPerformed);

        btDashboard.setBackground(new java.awt.Color(167, 139, 250));
        btDashboard.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
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
                .addComponent(jLabel2)
                .addGap(56, 56, 56)
                .addComponent(tfCari)
                .addGap(38, 38, 38)
                .addComponent(btDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(tfCari, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        jPanel3.setBackground(new java.awt.Color(18, 18, 28));

        jScrollPane2.setBackground(new java.awt.Color(18, 18, 28));
        jScrollPane2.setBorder(null);

        tbTransaksi.setBackground(new java.awt.Color(18, 18, 28));
        tbTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "LISTING", "BUYER", "TOTAL", "METODE", "STATUS", "DATE", "NOTES"
            }
        ));
        jScrollPane2.setViewportView(tbTransaksi);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(41, 41, 52));

        jPanel7.setBackground(new java.awt.Color(41, 41, 52));

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(226, 232, 240));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Detail - ID-Transaksi");

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(148, 163, 184));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Game");

        jLabel6.setBackground(new java.awt.Color(167, 139, 250));
        jLabel6.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(228, 225, 239));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("-");

        jLabel7.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(148, 163, 184));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Seller");

        jLabel8.setBackground(new java.awt.Color(167, 139, 250));
        jLabel8.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(228, 225, 239));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("-");

        jLabel9.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(148, 163, 184));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("METADATA");

        jLabel10.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(148, 163, 184));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Buyer");

        jLabel11.setBackground(new java.awt.Color(167, 139, 250));
        jLabel11.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(228, 225, 239));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("-");

        jLabel12.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(148, 163, 184));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("BILLING BREAKDOWN");

        jLabel13.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(206, 189, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Total Bayar");
        jLabel13.setToolTipText("");

        jLabel14.setBackground(new java.awt.Color(167, 139, 250));
        jLabel14.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(206, 189, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Rp0");

        jLabel17.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(148, 163, 184));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabel18.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(148, 163, 184));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(36, 36, 36)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(23, 23, 23)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14)))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel18)))
                        .addGap(68, 68, 68)))
                .addGap(0, 59, Short.MAX_VALUE))
        );

        btHapus.setBackground(new java.awt.Color(248, 113, 113));
        btHapus.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btHapus.setForeground(new java.awt.Color(17, 17, 29));
        btHapus.setText("Hapus Transaksi");
        btHapus.setBorder(null);
        btHapus.addActionListener(this::btHapusActionPerformed);

        btKonfirmasi.setBackground(new java.awt.Color(60, 221, 199));
        btKonfirmasi.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btKonfirmasi.setForeground(new java.awt.Color(17, 17, 29));
        btKonfirmasi.setText("Konfirmasi Pembayaran");
        btKonfirmasi.setBorder(null);
        btKonfirmasi.addActionListener(this::btKonfirmasiActionPerformed);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btKonfirmasi, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                    .addComponent(btHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(btKonfirmasi, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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

    private void tfCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfCariActionPerformed
        // TODO add your handling code here:
        loadTableData(tfCari.getText().trim());
    }//GEN-LAST:event_tfCariActionPerformed

    private void btHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btHapusActionPerformed
        // TODO add your handling code here:
        if (currentUser == null || !currentUser.getRole().equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this, 
                    "Akses ditolak! Hanya Admin yang dapat menghapus transaksi.", 
                    "Error Hak Akses", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int selectedRow = tbTransaksi.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                    "Silakan pilih baris transaksi yang ingin dihapus pada tabel!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idTransaksi = tbTransaksi.getValueAt(selectedRow, 0).toString();
        
        int konfirmasi = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus transaksi dengan ID: " + idTransaksi + "?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                boolean sukses = transImpl.deleteTransaction(idTransaksi);

                if (sukses) {
                    JOptionPane.showMessageDialog(this, 
                            "Transaksi " + idTransaksi + " berhasil dihapus!", 
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);

                    loadTableData(""); 

                    jLabel4.setText("Detail - ID-Transaksi");
                    jLabel6.setText("-");
                    jLabel8.setText("-");
                    jLabel11.setText("-");
                    jLabel14.setText("Rp0");
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "Gagal menghapus transaksi. Data mungkin sudah tidak ada.", 
                            "Gagal", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, 
                        "Terjadi kesalahan database: " + ex.getMessage(), 
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btHapusActionPerformed

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
        // TODO add your handling code here:
        TransaksiForm formInput = new TransaksiForm(currentUser, this);
        formInput.setVisible(true);
    }//GEN-LAST:event_btTambahActionPerformed

    private void btKonfirmasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btKonfirmasiActionPerformed
        // TODO add your handling code here:
        int selectedRow = tbTransaksi.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                    "Silakan pilih baris transaksi yang ingin dikonfirmasi!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String idTransaksi = tbTransaksi.getValueAt(selectedRow, 0).toString();
        String statusSaatIni = tbTransaksi.getValueAt(selectedRow, 5).toString();
        
        if (statusSaatIni.equalsIgnoreCase("Selesai")) {
            JOptionPane.showMessageDialog(this, 
                    "Transaksi ini sudah berstatus SELESAI!", 
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] opsiStatus = {"Proses", "Selesai", "Batal"};
        String statusBaru = (String) JOptionPane.showInputDialog(this, 
                "Pilih Status Baru untuk Transaksi " + idTransaksi + ":", 
                "Konfirmasi Pembayaran / Status", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                opsiStatus, 
                statusSaatIni);
        
        if (statusBaru == null) {
            return; 
        }
        
        try {
            boolean sukses = transImpl.updateTransactionStatus(idTransaksi, statusBaru);

            if (sukses) {
                JOptionPane.showMessageDialog(this, 
                        "Status transaksi " + idTransaksi + " berhasil diperbarui menjadi: " + statusBaru, 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);

                // 6. Refresh data tabel
                loadTableData("");

                // Reset detail panel
                jLabel4.setText("Detail - ID-Transaksi");
                jLabel6.setText("-");
                jLabel8.setText("-");
                jLabel11.setText("-");
                jLabel14.setText("Rp0");
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Gagal memperbarui status transaksi.", 
                        "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                    "Terjadi kesalahan database: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btKonfirmasiActionPerformed

    private void btDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDashboardActionPerformed
        // TODO add your handling code here:
        new DashboardView(currentUser).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btDashboardActionPerformed

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
//        java.awt.EventQueue.invokeLater(() -> new TransaksiView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDashboard;
    private javax.swing.JButton btHapus;
    private javax.swing.JButton btKonfirmasi;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEdit1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panelActionContainer;
    private javax.swing.JPanel panelActionContainer1;
    private javax.swing.JPanel panelEdit;
    private javax.swing.JPanel panelEdit1;
    private javax.swing.JTable tbTransaksi;
    private javax.swing.JTextField tfCari;
    // End of variables declaration//GEN-END:variables
}
