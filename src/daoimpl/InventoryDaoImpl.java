package daoimpl;

import dao.InventoryDao;
import model.Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDaoImpl implements InventoryDao {

    private final Connection connection;

    public InventoryDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Inventory getItemById(int itemId) {
        String query = "SELECT * FROM Inventory WHERE item_id=?";
        try (PreparedStatement psmt = connection.prepareStatement(query)) {
            psmt.setInt(1, itemId);
            try (ResultSet res = psmt.executeQuery()) {
                if (res.next()) {
                    int id = res.getInt("item_id");
                    String name = res.getString("name");
                    float price = res.getFloat("price");
                    int stock = res.getInt("stock");
                    return new Inventory(id, name, price, stock);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching item by ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateInventoryAfterCheckout(int userId) {
        String query = "SELECT item_id, quantity FROM Cart WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    int quantity = rs.getInt("quantity");

                    String updateQuery = "UPDATE Inventory SET stock = stock - ? WHERE item_id = ?";
                    try (PreparedStatement updatePs = connection.prepareStatement(updateQuery)) {
                        updatePs.setInt(1, quantity);
                        updatePs.setInt(2, itemId);
                        updatePs.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating inventory after checkout: " + e.getMessage());
        }
    }
}
