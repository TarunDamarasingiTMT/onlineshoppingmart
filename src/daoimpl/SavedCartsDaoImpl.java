package daoimpl;

import dao.SavedCartsDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SavedCartsDaoImpl implements SavedCartsDao {
    private final Connection connection;

    public SavedCartsDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveCartItems(int userId) {
        String query = "SELECT item_id, quantity FROM Cart WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    int quantity = rs.getInt("quantity");
                    String insertQuery = "INSERT INTO SavedCarts (user_id, item_id, quantity, updated_time) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertPs = connection.prepareStatement(insertQuery)) {
                        insertPs.setInt(1, userId);
                        insertPs.setInt(2, itemId);
                        insertPs.setInt(3, quantity);
                        insertPs.setObject(4, LocalDate.now());
                        insertPs.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving cart items: " + e.getMessage());
        }
    }
}
