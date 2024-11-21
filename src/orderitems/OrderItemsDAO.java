package orderitems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderItemsDAO implements OrderItemsInterface {
    private final Connection connection;

    public OrderItemsDAO(Connection connection) {
        this.connection = connection;
    }

    public void addOrderItems(int orderId, int itemId, int quantity, float subtotal) {
        String query = "INSERT INTO OrderItems (orderId, itemId, quantity, subtotal) VALUES (?, ?, ?, ?)";


        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.setInt(2, itemId);
            ps.setInt(3, quantity);
            ps.setFloat(4, subtotal);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
