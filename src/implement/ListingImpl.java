/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implement;

import Connection.Koneksi;
import entity.listing;
import interfc.ListingInterfc;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Asus Tuf
 */
public class ListingImpl implements ListingInterfc {
    private listing mapResultSet(ResultSet rs) throws SQLException {
        listing l = new listing();

        l.setListingId(rs.getString("listing_id"));
        l.setGameId(rs.getString("game_id"));
        l.setSellerId(rs.getString("seller_id"));
        l.setPrice(rs.getInt("price"));
        l.setCondition(rs.getString("condition"));
        l.setStatus(rs.getString("status"));
        l.setDescription(rs.getString("description"));
        l.setListedDate(rs.getDate("listed_date"));

        l.setGameTitle(rs.getString("game_title"));
        l.setSellerUsername(rs.getString("seller_username"));

        return l;
    }

    private String baseQuery() {
        return """
               SELECT 
                   l.listing_id,
                   l.game_id,
                   l.seller_id,
                   l.price,
                   l.`condition`,
                   l.status,
                   l.description,
                   l.listed_date,
                   g.title AS game_title,
                   u.username AS seller_username
               FROM listings l
               JOIN games g ON l.game_id = g.game_id
               JOIN users u ON l.seller_id = u.user_id
               """;
    }

    @Override
    public void insert(listing o) throws SQLException {
        String sql = """
                     INSERT INTO listings
                     (listing_id, game_id, seller_id, price, `condition`, status, description, listed_date, created_at, updated_at)
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
                     """;

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);

        st.setString(1, o.createIdListing());
        st.setString(2, o.getGameId());
        st.setString(3, o.getSellerId());
        st.setInt(4, o.getPrice());
        st.setString(5, o.getCondition());
        st.setString(6, o.getStatus());
        st.setString(7, o.getDescription());
        st.setDate(8, o.getListedDate());

        st.executeUpdate();
    }

    @Override
    public void update(listing o) throws SQLException {
        String sql = """
                     UPDATE listings
                     SET game_id = ?,
                         price = ?,
                         `condition` = ?,
                         status = ?,
                         description = ?,
                         listed_date = ?,
                         updated_at = NOW()
                     WHERE listing_id = ?
                     """;

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);

        st.setString(1, o.getGameId());
        st.setInt(2, o.getPrice());
        st.setString(3, o.getCondition());
        st.setString(4, o.getStatus());
        st.setString(5, o.getDescription());
        st.setDate(6, o.getListedDate());
        st.setString(7, o.getListingId());

        st.executeUpdate();
    }

    @Override
    public void delete(String listing_id) throws SQLException {
        String sql = "DELETE FROM listings WHERE listing_id = ?";

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);
        st.setString(1, listing_id);
        st.executeUpdate();
    }

    @Override
    public void updateStatus(String listing_id, String status) throws SQLException {
        String sql = """
                     UPDATE listings
                     SET status = ?,
                         updated_at = NOW()
                     WHERE listing_id = ?
                     """;

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);

        st.setString(1, status);
        st.setString(2, listing_id);

        st.executeUpdate();
    }

    @Override
    public listing getById(String listing_id) throws SQLException {
        String sql = baseQuery() + " WHERE l.listing_id = ?";

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);
        st.setString(1, listing_id);

        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return mapResultSet(rs);
        }

        return null;
    }

    @Override
    public List<listing> getAll() throws SQLException {
        String sql = baseQuery() + " ORDER BY l.listed_date DESC";

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        List<listing> list = new ArrayList<>();

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }

        return list;
    }

    @Override
    public List<listing> getBySeller(String seller_id) throws SQLException {
        String sql = baseQuery()
                + " WHERE l.seller_id = ?"
                + " ORDER BY l.listed_date DESC";

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);
        st.setString(1, seller_id);

        ResultSet rs = st.executeQuery();

        List<listing> list = new ArrayList<>();

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }

        return list;
    }

    @Override
    public List<listing> search(String keyword, String status, String role, String seller_id) throws SQLException {
        StringBuilder sql = new StringBuilder(baseQuery());
        List<Object> params = new ArrayList<>();

        sql.append(" WHERE 1 = 1 ");

        if (role.equalsIgnoreCase("seller")) {
            sql.append(" AND l.seller_id = ? ");
            params.add(seller_id);
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("""
                       AND (
                           l.listing_id LIKE ?
                           OR g.title LIKE ?
                           OR u.username LIKE ?
                           OR l.`condition` LIKE ?
                       )
                       """);

            String like = "%" + keyword.trim() + "%";

            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
        }

        if (status != null
                && !status.trim().isEmpty()
                && !status.equalsIgnoreCase("SEMUA")) {

            sql.append(" AND l.status = ? ");
            params.add(status.toUpperCase());
        }

        sql.append(" ORDER BY l.listed_date DESC");

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql.toString());

        for (int i = 0; i < params.size(); i++) {
            st.setObject(i + 1, params.get(i));
        }

        ResultSet rs = st.executeQuery();

        List<listing> list = new ArrayList<>();

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }

        return list;
    }
}
