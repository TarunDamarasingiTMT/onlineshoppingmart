package orderhistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrderHistoryDAO implements OrderHistoryInterface {
    private final Connection connection;

    public OrderHistoryDAO(Connection connection) {
        this.connection = connection;
    }

    // Add order to the OrderHistory table
    public int addOrderToHistory(int userId, float totalAmount) {
        String query = "INSERT INTO OrderHistory (userId, totalAmount, timestamp) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setFloat(2, totalAmount);
            ps.setObject(3, LocalDate.now()); // Use LocalDate for the current date
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }


    public void getOrderHistory(int userId) {
        String query = "SELECT orderId, totalCost, timestamp FROM OrderHistory WHERE userId = ? ORDER BY timestamp DESC";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No orders found for userId: " + userId);
                return;
            }

            System.out.println("Order History for User ID: " + userId);
            while (rs.next()) {
                int orderId = rs.getInt("orderId");
                float totalCost = rs.getFloat("totalCost");
                LocalDate timestamp = rs.getObject("timestamp", LocalDate.class);

                System.out.println("Order ID: " + orderId);
                System.out.println("Total Cost: $" + totalCost);
                System.out.println("Order Date: " + timestamp);
                System.out.println("-------------------------------");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}

