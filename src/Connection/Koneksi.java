/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Asus
 */
public class Koneksi {
    private static Connection conn;
    public static Connection getConnection() throws SQLException {
        if(conn == null || conn.isClosed()) {
            
            String id, pw, driver, url;
            id = "root";
            pw ="";
            driver = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://localhost:3306/db_uas_pbo?userTimezone=true&server=UTC";

            try {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url, id, pw);


            } catch (Exception e) {
                System.out.println("Gagal pakcik: " + e.getMessage());
            }
        }
        
        return conn;
    }
}
