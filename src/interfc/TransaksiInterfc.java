/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfc;

import entity.transaksi;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Asus
 */
public interface TransaksiInterfc {
    List<String[]> getTransactionList(String role, String userId) throws SQLException;
    boolean deleteTransaction(String transactionId) throws SQLException;
    String generateNewTransactionId() throws SQLException;
    boolean insertTransaction(transaksi t) throws SQLException;
    boolean updateTransactionStatus(String idTransaksi, String statusBaru) throws SQLException;

    String getSellerNameByTransactionId(String transactionId) throws SQLException;
    
    List<String[]> getAvailableListing();
    long getHargaById(String idListing);
    
}
