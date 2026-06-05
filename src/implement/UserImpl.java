/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implement;
import entity.user;
import interfc.UserInterfc;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Connection.Koneksi;
import at.favre.lib.crypto.bcrypt.BCrypt;
/**
 *
 * @author Bintang K
 */
public class UserImpl implements UserInterfc{

    @Override
    public user register(user o) throws SQLException {
        PreparedStatement cek = Koneksi.getConnection().prepareStatement(
        "select count(*) from users where username=?");
        cek.setString(1, o.getUsername());
        ResultSet rs = cek.executeQuery();
        rs.next();
        if (rs.getInt(1) > 0) {
            throw new SQLException("Username sudah digunakan, coba username lain.");
        }
        String hashed = BCrypt.withDefaults().hashToString(12, o.getpassword().toCharArray());

        
        PreparedStatement st = Koneksi.getConnection().prepareStatement("insert into users values(?,?,?,?,?,?,NOW(),NOw())");
        st.setString(1, o.createIdUser());
        st.setString(2, o.getUsername());
        st.setString(3, o.getFullname());
        st.setString(4, o.getEmail());
        st.setString(5, hashed);
        st.setString(6, o.getRole());
        st.executeUpdate();
        return o;
    }

    @Override
    public void update(user o) throws SQLException {
        PreparedStatement st = Koneksi.getConnection().prepareStatement("update users set username=?,full_name=?,email=?,role=? where user_id=?");
        st.setString(1, o.getUsername());
        st.setString(2, o.getFullname());
        st.setString(3, o.getEmail());
        st.setString(4, o.getRole());
        st.setString(5, o.getIdUser());
        st.executeUpdate();
    }

    @Override
    public void delete(String user_id) throws SQLException {
        PreparedStatement st = Koneksi.getConnection().prepareStatement("delete from users where user_id=?");
        st.setString(1, user_id);
        st.executeUpdate();
    }

    @Override
    public List<user> getAll(String keyword) throws SQLException {
        String query = "select * from users where user_id like ? or username like ? or full_name like ? or email like ? or role like ?";
        PreparedStatement st = Koneksi.getConnection().prepareStatement(query);
        String k = "%" + keyword + "%";
        st.setString(1, k);
        st.setString(2, k);
        st.setString(3, k);
        st.setString(4, k);
        st.setString(5, k);
        ResultSet rs = st.executeQuery();
        List<user>list= new ArrayList<>();
        while(rs.next()) {
            user usr = new user();
            usr.setIdUser(rs.getString("user_id"));
            usr.setUsername(rs.getString("username"));
            usr.setFullname(rs.getString("full_name"));
            usr.setEmail(rs.getString("email"));
            usr.setRole(rs.getString("role"));
            list.add(usr);
        }
        return list;
    }

    @Override
    public user login(String username, String password) throws SQLException {
        PreparedStatement st = Koneksi.getConnection().prepareStatement(
               "select * from users where username=?");
           st.setString(1, username);
           ResultSet rs = st.executeQuery();
           if (rs.next()) {
               String hashedPassword = rs.getString("password");
               BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
               if(result.verified) {
                   
               user usr = new user();
               usr.setIdUser(rs.getString("user_id"));
               usr.setUsername(rs.getString("username"));
               usr.setFullname(rs.getString("full_name"));
               usr.setEmail(rs.getString("email"));
               usr.setRole(rs.getString("role"));
               return usr;
               }
           }
           return null;
    }

    @Override
    public user getById(String user_id) throws SQLException {
        PreparedStatement st = Koneksi.getConnection().prepareStatement(
                "select * from users where user_id=?");
            st.setString(1, user_id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                user usr = new user();
                usr.setIdUser(rs.getString("user_id"));
                usr.setUsername(rs.getString("username"));
                usr.setFullname(rs.getString("full_name"));
                usr.setEmail(rs.getString("email"));
                usr.setRole(rs.getString("role"));
                return usr;
            }
            return null;
    }


    @Override
    public boolean resetPassword(String email, String fullname, String passwordBaru) throws SQLException {
        PreparedStatement cek = Koneksi.getConnection().prepareStatement(
        "select count(*) from users where email=? and full_name=?");
        cek.setString(1, email);
        cek.setString(2, fullname);
        ResultSet rs = cek.executeQuery();
        rs.next();
        if (rs.getInt(1) == 0) {
            return false; // email atau fullname tidak cocok
        }
        String hashed = BCrypt.withDefaults().hashToString(12, passwordBaru.toCharArray());
        PreparedStatement update = Koneksi.getConnection().prepareStatement(
            "update users set password=? where email=?");
        update.setString(1, hashed);
        update.setString(2, email);
        update.executeUpdate();
        return true;
    }
    
    
}
