/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import entity.user;

/**
 *
 * @author Bintang K
 */
public class Session {
    private static user currentUser;
    
    public static void setUser(user usr) {
        currentUser = usr;
    }

    public static user getUser() {
        return currentUser;
    }

    public static String getRole() {
        return currentUser.getRole();
    }

    public static void clear() {
        currentUser = null;
    }
    
}
