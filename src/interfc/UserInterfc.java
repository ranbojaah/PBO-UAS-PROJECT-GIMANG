/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfc;

import entity.user;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author Bintang K
 */
public interface UserInterfc {
    boolean resetPassword(String email, String fullname, String passwordBaru) throws SQLException;
    user register (user o) throws SQLException;
    user login(String username, String password) throws SQLException;
    user getById(String user_id) throws SQLException;
    void update (user o) throws SQLException;
    void delete (String user_id) throws SQLException;
    List getAll(String keyword) throws SQLException;
}
