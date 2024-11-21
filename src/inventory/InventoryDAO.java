package inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class InventoryDAO implements InventoryInterface{

    private final Connection connection;

    public InventoryDAO(Connection connection) {
        this.connection = connection;
    }

    public Inventory getItemById(int itemId) {
        String query = "SELECT * FROM Inventory WHERE itemId=?";
        try (PreparedStatement psmt = connection.prepareStatement(query)) {
            psmt.setInt(1, itemId);
            ResultSet res = psmt.executeQuery();
            if (res.next()) {
                int id = res.getInt("itemId");
                String name = res.getString("name");
                float price = res.getFloat("price");
                int stock = res.getInt("stock");
                return new Inventory(id, name, price, stock);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateInventoryAfterCheckout(int userId) {
        String query = "SELECT itemId, quantity FROM Cart WHERE userId = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int itemId = rs.getInt("itemId");
                int quantity = rs.getInt("quantity");

                // Update inventory

                String updateQuery = "UPDATE Inventory SET stock = stock - ? WHERE itemId = ?";
                try (PreparedStatement updatePs = connection.prepareStatement(updateQuery)) {
                    updatePs.setInt(1, quantity);
                    updatePs.setInt(2, itemId);

                    updatePs.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}


