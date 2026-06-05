/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfc;
import entity.genre;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Asus
 */
public interface GenreInterfc {
    List<genre> getAllGenre() throws SQLException;
    boolean insertGenre(genre g) throws SQLException;
    boolean updateGenre(genre g) throws SQLException;
    boolean deleteGenre(String idGenre) throws SQLException;
    String generateNewGenreId() throws SQLException;
}
