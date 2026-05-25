/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfc;

import entity.listing;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Asus Tuf
 */
public interface ListingInterfc {
    void insert(listing o) throws SQLException;

    void update(listing o) throws SQLException;

    void delete(String listing_id) throws SQLException;

    void updateStatus(String listing_id, String status) throws SQLException;

    listing getById(String listing_id) throws SQLException;

    List<listing> getAll() throws SQLException;

    List<listing> getBySeller(String seller_id) throws SQLException;

    List<listing> search(String keyword, String status, String role, String seller_id) throws SQLException;
}
