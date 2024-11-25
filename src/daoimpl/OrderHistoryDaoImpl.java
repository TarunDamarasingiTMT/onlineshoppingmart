package daoimpl;

import dao.OrderHistoryDao;
import model.OrderHistory;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.*;

public class OrderHistoryDaoImpl implements OrderHistoryDao {
    private final Connection connection;
    private static final Logger logger = Logger.getLogger(OrderHistoryDaoImpl.class.getName());

    public OrderHistoryDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int addOrderToHistory(int userId, float totalAmount) throws SQLException {
        String query = "INSERT INTO OrderHistory (user_id, total_amount, timestamp) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setFloat(2, totalAmount);
            ps.setObject(3, LocalDate.now());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int orderId = rs.getInt(1);  // Return the generated order_id
                    logger.info("Order added to history successfully. User ID: " + userId + ", Order ID: " + orderId + ", Total Amount: $" + totalAmount);
                    return orderId;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error adding order to history. User ID: " + userId + ", Total Amount: $" + totalAmount + ", Exception: " + e.getMessage());
            throw new SQLException("Error adding order to history", e);
        }
        return -1;  //-1 will be returned if no ID was generated
    }

    @Override
    public void getOrderHistory(int userId) throws SQLException {
        String query = "SELECT order_id, total_amount, timestamp FROM OrderHistory WHERE user_id = ? ORDER BY timestamp DESC";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrderHistory> orderHistoryList = new ArrayList<>();
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    float totalAmount = rs.getFloat("total_amount");
                    LocalDate timestamp = rs.getObject("timestamp", LocalDate.class);
                    orderHistoryList.add(new OrderHistory(orderId, totalAmount, timestamp));
                }

                if (orderHistoryList.isEmpty()) {
                    logger.info("No orders found for User ID: " + userId);
                    //System.out.println("No orders found for userId: " + userId);
                    return;
                }

                // Using Streams to print the order history
                System.out.println("Order History for User ID: " + userId);
                orderHistoryList.stream().forEach(order -> {
                    System.out.println("Order ID: " + order.getOrderId());
                    System.out.println("Total Cost: $" + order.getTotalAmount());
                    System.out.println("Order Date: " + order.getTimestamp());
                    System.out.println("-------------------------------");
                });
            }
        } catch (SQLException e) {
            logger.severe("Error fetching order history. User ID: " + userId + ", Exception: " + e.getMessage());
            System.err.println("Error fetching order history: " + e.getMessage());
            throw new SQLException(e);
        }
    }
}
