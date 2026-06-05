/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Asus
 */
public class GenreItem {
    private String id;
    private String name;

    public GenreItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    @Override
    public String toString() { return name; }
}