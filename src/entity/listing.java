/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import Connection.Koneksi;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
/**
 *
 * @author Asus Tuf
 */
public class listing {
    private String listing_id;
    private String game_id;
    private String seller_id;
    private int price;
    private String condition;
    private String status;
    private String description;
    private Date listed_date;

    // tambahan untuk tampilan JTable hasil JOIN
    private String game_title;
    private String seller_username;

    public String getListingId() {
        return listing_id;
    }

    public void setListingId(String listing_id) {
        this.listing_id = listing_id;
    }

    public String getGameId() {
        return game_id;
    }

    public void setGameId(String game_id) {
        this.game_id = game_id;
    }

    public String getSellerId() {
        return seller_id;
    }

    public void setSellerId(String seller_id) {
        this.seller_id = seller_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getListedDate() {
        return listed_date;
    }

    public void setListedDate(Date listed_date) {
        this.listed_date = listed_date;
    }

    public String getGameTitle() {
        return game_title;
    }

    public void setGameTitle(String game_title) {
        this.game_title = game_title;
    }

    public String getSellerUsername() {
        return seller_username;
    }

    public void setSellerUsername(String seller_username) {
        this.seller_username = seller_username;
    }

    public String createIdListing() throws SQLException {
        Statement st = Koneksi.getConnection().createStatement();
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM listings");
        rs.next();

        int jumlah = rs.getInt(1) + 1;
        return String.format("L%03d", jumlah);
    }
}
