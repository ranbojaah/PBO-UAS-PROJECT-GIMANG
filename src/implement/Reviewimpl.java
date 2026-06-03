package implement;

import Connection.Koneksi;
import entity.Review;
import interfc.ReviewInterfc; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Asus Tuf
 */
public class Reviewimpl implements ReviewInterfc {

    private Review mapResultSet(ResultSet rs) throws SQLException {
        Review r = new Review();

        r.setReviewId(rs.getString("review_id"));
        r.setTransactionId(rs.getString("transaction_id")); 
        r.setReviewerId(rs.getString("reviewer_id"));
        r.setRating(rs.getInt("rating"));
        r.setComment(rs.getString("comment"));
        r.setReviewDate(rs.getDate("review_date"));

        r.setGameTitle(rs.getString("game_title"));
        r.setReviewerUsername(rs.getString("reviewer_username"));

        return r;
    }

    private String baseQuery() {
        return """
               SELECT 
                   r.review_id,
                   r.transaction_id,
                   r.reviewer_id,
                   r.rating,
                   r.comment,
                   r.review_date,
                   g.title AS game_title,
                   u.username AS reviewer_username
               FROM reviews r
               JOIN transactions t ON r.transaction_id = t.transaction_id
               JOIN listings l ON t.listing_id = l.listing_id
               JOIN games g ON l.game_id = g.game_id
               JOIN users u ON r.reviewer_id = u.user_id
               """;
    }

    @Override
    public void insert(Review o) throws SQLException {
        String sql = """
                     INSERT INTO reviews
                     (review_id, transaction_id, reviewer_id, rating, comment, review_date, created_at, updated_at)
                     VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())
                     """;

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);

        st.setString(1, o.createIdReview());
        st.setString(2, o.getTransactionId()); 
        st.setString(3, o.getReviewerId());
        st.setInt(4, o.getRating());
        st.setString(5, o.getComment());
        st.setDate(6, o.getReviewDate());

        st.executeUpdate();
    }

    @Override
    public void update(Review o) throws SQLException {
        String sql = """
                     UPDATE reviews
                     SET rating = ?,
                         comment = ?,
                         review_date = ?
                     WHERE review_id = ?
                     """;

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);

        st.setInt(1, o.getRating());
        st.setString(2, o.getComment());
        st.setDate(3, o.getReviewDate());
        st.setString(4, o.getReviewId());

        st.executeUpdate();
    }

    @Override
    public void delete(String review_id) throws SQLException {
        String sql = "DELETE FROM reviews WHERE review_id = ?";

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);
        st.setString(1, review_id);
        
        st.executeUpdate();
    }

    @Override
    public Review getById(String review_id) throws SQLException {
        String sql = baseQuery() + " WHERE r.review_id = ?";

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);
        st.setString(1, review_id);

        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return mapResultSet(rs);
        }

        return null;
    }

    @Override
    public List<Review> getAll() throws SQLException {
        String sql = baseQuery() + " ORDER BY r.review_date DESC";

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        List<Review> list = new ArrayList<>();

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }

        return list;
    }

    // PERBAIKAN: Mengubah nama method dari getByListing menjadi getByTransaction agar sesuai dengan Interface
    @Override
    public List<Review> getByTransaction(String transaction_id) throws SQLException {
        String sql = baseQuery() 
                + " WHERE r.transaction_id = ?" 
                + " ORDER BY r.review_date DESC";

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);
        st.setString(1, transaction_id);

        ResultSet rs = st.executeQuery();

        List<Review> list = new ArrayList<>();

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }

        return list;
    }
    
    @Override
    public List<Review> getBySeller(String sellerId) throws SQLException {
        String sql = baseQuery() 
                   + " WHERE l.seller_id = ?" 
                   + " ORDER BY r.review_date DESC";

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);
        st.setString(1, sellerId);

        ResultSet rs = st.executeQuery();
        List<Review> list = new ArrayList<>();

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }

        return list;
    }
    
    @Override
    public List<Review> search(String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder(baseQuery());
        List<Object> params = new ArrayList<>();

        sql.append(" WHERE 1 = 1 ");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("""
                       AND (
                           r.review_id LIKE ?
                           OR g.title LIKE ?
                           OR u.username LIKE ?
                           OR r.comment LIKE ?
                       )
                       """);

            String like = "%" + keyword.trim() + "%";

            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
        }

        sql.append(" ORDER BY r.review_date DESC");

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql.toString());

        for (int i = 0; i < params.size(); i++) {
            st.setObject(i + 1, params.get(i));
        }

        ResultSet rs = st.executeQuery();

        List<Review> list = new ArrayList<>();

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }

        return list;
    }
    
    @Override
    public List<Review> searchBySeller(String sellerId, String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder(baseQuery());
        List<String> params = new ArrayList<>();

        sql.append(" WHERE l.seller_id = ? ");
        params.add(sellerId);

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("""
                       AND (
                           r.review_id LIKE ?
                           OR g.title LIKE ?
                           OR u.username LIKE ?
                           OR r.comment LIKE ?
                       )
                       """);

            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
        }

        sql.append(" ORDER BY r.review_date DESC");

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql.toString());

        for (int i = 0; i < params.size(); i++) {
            st.setString(i + 1, params.get(i));
        }

        ResultSet rs = st.executeQuery();
        List<Review> list = new ArrayList<>();

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }

        return list;
    }

    @Override
    public List<Review> getByBuyer(String buyerId) throws SQLException {
        String sql = baseQuery() 
                   + " WHERE r.reviewer_id = ?" 
                   + " ORDER BY r.review_date DESC";

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql);
        st.setString(1, buyerId);

        ResultSet rs = st.executeQuery();
        List<Review> list = new ArrayList<>();

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }

        return list;
    }

    @Override
    public List<Review> searchByBuyer(String buyerId, String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder(baseQuery());
        List<String> params = new ArrayList<>();

        sql.append(" WHERE r.reviewer_id = ? ");
        params.add(buyerId);

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("""
                       AND (
                           r.review_id LIKE ?
                           OR g.title LIKE ?
                           OR u.username LIKE ?
                           OR r.comment LIKE ?
                       )
                       """);

            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
        }

        sql.append(" ORDER BY r.review_date DESC");

        PreparedStatement st = Koneksi.getConnection().prepareStatement(sql.toString());

        for (int i = 0; i < params.size(); i++) {
            st.setString(i + 1, params.get(i));
        }

        ResultSet rs = st.executeQuery();
        List<Review> list = new ArrayList<>();

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }

        return list;
    }
}