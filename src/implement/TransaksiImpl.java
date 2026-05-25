/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implement;

import Connection.Koneksi;
import entity.transaksi;
import interfc.TransaksiInterfc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Asus
 */
public class TransaksiImpl implements TransaksiInterfc {
    
    @Override
    public List<String[]> getTransactionList(String role, String userId) throws SQLException {
        List<String[]> list = new ArrayList<>();
        String sql = "";

        // TAMBAHKAN t.notes DI SETIAP SELECT QUERY
        if (role.equals("admin")) {
            sql = "SELECT t.transaction_id, g.title, u.username AS buyer, t.total_price, t.payment_method, t.status, t.transaction_date, t.notes " +
                  "FROM transactions t " +
                  "JOIN listings l ON t.listing_id = l.listing_id " +
                  "JOIN games g ON l.game_id = g.game_id " +
                  "JOIN users u ON t.buyer_id = u.user_id";
        } else if (role.equals("buyer")) {
            sql = "SELECT t.transaction_id, g.title, u.username AS buyer, t.total_price, t.payment_method, t.status, t.transaction_date, t.notes " +
                  "FROM transactions t " +
                  "JOIN listings l ON t.listing_id = l.listing_id " +
                  "JOIN games g ON l.game_id = g.game_id " +
                  "JOIN users u ON t.buyer_id = u.user_id " +
                  "WHERE t.buyer_id = ?";
        } else if (role.equals("seller")) {
            sql = "SELECT t.transaction_id, g.title, u.username AS buyer, t.total_price, t.payment_method, t.status, t.transaction_date, t.notes " +
                  "FROM transactions t " +
                  "JOIN listings l ON t.listing_id = l.listing_id " +
                  "JOIN games g ON l.game_id = g.game_id " +
                  "JOIN users u ON t.buyer_id = u.user_id " +
                  "WHERE l.seller_id = ?";
        }

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!role.equals("admin")) {
                ps.setString(1, userId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // MASUKKAN rs.getString("notes") PADA INDEKS KE-7 ARRAY (Urutan ke-8)
                    list.add(new String[]{
                        rs.getString("transaction_id"),
                        rs.getString("title"),
                        rs.getString("buyer"),
                        String.valueOf(rs.getInt("total_price")),
                        rs.getString("payment_method"),
                        rs.getString("status"),
                        rs.getString("transaction_date"),
                        rs.getString("notes") // <--- Data Notes dimasukkan di sini
                    });
                }
            }
        }
        return list;
    }
    
    @Override
    public boolean deleteTransaction(String transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, transactionId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException ex) {
            System.out.println("=== ERROR SAAT DELETE TRANSAKSI ===");
            ex.printStackTrace();
            throw ex;
        }
    }
    
    @Override
    public String generateNewTransactionId() throws SQLException {

        String sql = "SELECT transaction_id FROM transactions ORDER BY transaction_id DESC LIMIT 1";

        try (
            Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {

                String lastId = rs.getString("transaction_id");

                int number = Integer.parseInt(lastId.substring(1));

                number++;

                return String.format("T%03d", number);
            }
        }

        return "T001";
    }
    
    @Override
    public boolean insertTransaction(transaksi t) throws SQLException {
        Connection conn = Koneksi.getConnection();
        conn.setAutoCommit(false);

        String sqlTransaction = "INSERT INTO transactions (transaction_id, listing_id, buyer_id, total_price, payment_method, status, notes, transaction_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlUpdateListing = "UPDATE listings SET status = 'TERJUAL' WHERE listing_id = ?";

        try (PreparedStatement psT = conn.prepareStatement(sqlTransaction);
             PreparedStatement psL = conn.prepareStatement(sqlUpdateListing)) {
            
            psT.setString(1, t.getIdTransaksi());
            psT.setString(2, t.getIdListing());
            psT.setString(3, t.getIdPembeli());
            psT.setInt(4, t.getTotalHarga());
            psT.setString(5, t.getMetodePembayaran());
            psT.setString(6, t.getStatus());
            psT.setString(7, t.getCatatan());
            psT.setDate(8, t.getTanggalTransaksi());
            psT.executeUpdate();

            psL.setString(1, t.getIdListing());
            psL.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public String getSellerNameByTransactionId(String transactionId) throws SQLException {
        String sellerName = "-";
        
        // Menggunakan alias u2 khusus untuk menarik username seller dari tabel users
        String sql = "SELECT u2.username AS seller_name " +
                     "FROM transactions t " +
                     "JOIN listings l ON t.listing_id = l.listing_id " +
                     "JOIN users u2 ON l.seller_id = u2.user_id " +
                     "WHERE t.transaction_id = ?";
        
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, transactionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sellerName = rs.getString("seller_name");
                }
            }
        } catch (SQLException ex) {
            System.out.println("=== LOG ERROR MYSQL ===");
            ex.printStackTrace();
            throw ex;
        }
        return sellerName;
    }
    
    @Override
    public boolean updateTransactionStatus(String idTransaksi, String statusBaru) throws SQLException {
        String sql = "UPDATE transactions SET status = ? WHERE transaction_id = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statusBaru);
            ps.setString(2, idTransaksi);

            return ps.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<String[]> getAvailableListing() {

        List<String[]> listListing = new ArrayList<>();

        String query =
            "SELECT l.listing_id, g.title " +
            "FROM listings l " +
            "JOIN games g ON l.game_id = g.game_id " +
            "WHERE l.status = 'TERSEDIA'";

        try (
            Connection conn = Koneksi.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                listListing.add(new String[]{
                    rs.getString("listing_id"),
                    rs.getString("title")
                });
            }

        } catch (SQLException e) {
            System.err.println("Error getAvailableListing: " + e.getMessage());
        }

        return listListing;
    }

    @Override
    public long getHargaById(String idListing) {

        String query = "SELECT price FROM listings WHERE listing_id = ?";

        try (
            Connection conn = Koneksi.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)
        ) {

            stmt.setString(1, idListing);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getLong("price");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getHargaById: " + e.getMessage());
        }

        return 0;
    }
}
