/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implement;
import Connection.Koneksi;
import entity.genre;
import interfc.GenreInterfc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Asus
 */
public class GenreImpl implements GenreInterfc {
    @Override
    public List<genre> getAllGenre() throws SQLException {
        List<genre> list = new ArrayList<>();

        // Jalurnya: dari genres (g) LEFT JOIN ke gamegenre (gg) berdasarkan genre_id
        String sql = "SELECT g.id, g.name, COUNT(gg.game_id) AS total_game " +
                     "FROM genres g " +
                     "LEFT JOIN gamegenre gg ON g.id = gg.genre_id " +
                     "GROUP BY g.id, g.name " +
                     "ORDER BY g.id ASC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                genre g = new genre();
                g.setIdGenre(rs.getString("id"));
                g.setNamaGenre(rs.getString("name"));
                g.setTotalGame(rs.getInt("total_game"));
                list.add(g);
            }
        }
        return list;
    }

    @Override
    public boolean insertGenre(genre g) throws SQLException {
        String sql = "INSERT INTO genres (id, name) VALUES (?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, g.getIdGenre());
            ps.setString(2, g.getNamaGenre());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateGenre(genre g) throws SQLException {
        String sql = "UPDATE genres SET name = ? WHERE id = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, g.getNamaGenre());
            ps.setString(2, g.getIdGenre());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteGenre(String idGenre) throws SQLException {
        String sql = "DELETE FROM genres WHERE id = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idGenre);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public String generateNewGenreId() throws SQLException {
        String sql = "SELECT id FROM genres ORDER BY id DESC LIMIT 1";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("id"); // Contoh: "GEN05"
                int number = Integer.parseInt(lastId.substring(3)); // Ambil angka setelah "GEN"
                number++;
                return String.format("GEN%02d", number); // Hasil: "GEN06"
            }
        }
        return "GEN01"; // Default kalau data masih kosong
    }
}
