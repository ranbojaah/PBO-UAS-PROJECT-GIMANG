/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import Connection.Koneksi;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 *
 * @author Bintang K
 */
public class user {
    private String user_id;
    private String username;
    private String full_name;
    private String email;
    private String password;
    private String role;
    String idUser = String.valueOf(user_id);
    public String getIdUser(){
        return idUser;
    }
    public String createIdUser() throws SQLException {
        Statement st = Koneksi.getConnection().createStatement();
        ResultSet rs = st.executeQuery("select count(*) from users");
        rs.next();
        int jumlah = rs.getInt(1) + 1;
        return String.format("U%03d", jumlah);
    }
    public void setIdUser(String user_id) {
        
        this.idUser = user_id;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getFullname(){
        return full_name;
    }
    public void setFullname(String full_name) {
        this.full_name = full_name;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getpassword(){
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getrole(){
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

}
