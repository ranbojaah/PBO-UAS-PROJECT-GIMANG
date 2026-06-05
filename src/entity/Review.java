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
public class Review {
    private String review_id;
    private String transaction_id; 
    private String reviewer_id;
    private int rating;
    private String comment;
    private Date review_date;

    private String game_title;       
    private String reviewer_username; 

    public String getReviewId() {
        return review_id;
    }

    public void setReviewId(String review_id) {
        this.review_id = review_id;
    }

    public String getTransactionId() {
        return transaction_id;
    }

    public void setTransactionId(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getReviewerId() {
        return reviewer_id;
    }

    public void setReviewerId(String reviewer_id) {
        this.reviewer_id = reviewer_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewDate() {
        return review_date;
    }

    public void setReviewDate(Date review_date) {
        this.review_date = review_date;
    }

    public String getGameTitle() {
        return game_title;
    }

    public void setGameTitle(String game_title) {
        this.game_title = game_title;
    }

    public String getReviewerUsername() {
        return reviewer_username;
    }

    public void setReviewerUsername(String reviewer_username) {
        this.reviewer_username = reviewer_username;
    }


    public String createIdReview() throws SQLException {
        Statement st = Koneksi.getConnection().createStatement();
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM reviews"); 
        rs.next();

        int jumlah = rs.getInt(1) + 1;
        return String.format("R%03d", jumlah);
    }
}