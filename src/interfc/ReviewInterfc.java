/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfc;

import entity.Review;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Asus Tuf
 */
public interface ReviewInterfc {
    
    // Melakukan input data review baru ke database
    void insert(Review o) throws SQLException;
    
    // Mengubah data review (rating, komentar, tanggal) yang sudah ada
    void update(Review o) throws SQLException;
    
    // Menghapus data review berdasarkan ID-nya
    void delete(String review_id) throws SQLException;
    
    // Mengambil satu data review spesifik berdasarkan ID
    Review getById(String review_id) throws SQLException;
    
    // Mengambil semua data review yang ada di database
    List<Review> getAll() throws SQLException;
    
    // UBAH: Mengambil daftar review khusus untuk satu transaksi tertentu
    List<Review> getByTransaction(String transaction_id) throws SQLException;
    
    // Melakukan pencarian review berdasarkan kata kunci tertentu
    List<Review> search(String keyword) throws SQLException;
    
    List<Review> searchBySeller(String sellerId, String keyword) throws SQLException;
    List<Review> searchByBuyer(String buyerId, String keyword) throws SQLException;
    List<Review> getByBuyer(String buyerId) throws SQLException;
    List<Review> getBySeller(String seller_id) throws SQLException;
}