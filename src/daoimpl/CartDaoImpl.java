package daoimpl;

import dao.CartDao;
import model.Inventory;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.*;

public class CartDaoImpl implements CartDao {
    private final Connection connection;
    private final InventoryDaoImpl inventoryDAO;
    private static final Logger logger = Logger.getLogger(CartDaoImpl.class.getName());

    public CartDaoImpl(Connection connection) {
        this.connection = connection;
        this.inventoryDAO = new InventoryDaoImpl(connection);
    }

    @Override
    public void addItemToCart(int userId, int itemId, int quantity) throws SQLException {
        Inventory item = inventoryDAO.getItemById(itemId);

        // Validation
        if (item == null) {
            logger.warning("Item not found. User ID: " + userId + ", Item ID: " + itemId);
            return;
        }

        int stock = item.getStock();
        if (stock >= quantity) {
            String query = "INSERT INTO Cart (user_id, item_id, quantity, last_updated) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, userId);
                ps.setInt(2, itemId);
                ps.setInt(3, quantity);
                ps.setDate(4, Date.valueOf(LocalDate.now()));
                ps.executeUpdate();
                // Log once after successful addition
                logger.info("Item added to cart. User ID: " + userId + ", Item ID: " + itemId + ", Quantity: " + quantity);
            } catch (SQLException e) {
                logger.severe("Error adding item to cart. User ID: " + userId + ", Item ID: " + itemId + ", Exception: " + e.getMessage());
                throw new SQLException(e);
            }
        } else {
            logger.warning("Not enough stock available. User ID: " + userId + ", Item ID: " + itemId + ", Requested Quantity: " + quantity);
        }
    }

    @Override
    public void removeItemFromCart(int userId, int itemId, int quantity) throws SQLException {
        String query = "UPDATE Cart SET quantity = quantity - ? WHERE user_id = ? AND item_id = ? AND quantity >= ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, itemId);
            ps.setInt(4, quantity);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated == 0) {
                logger.warning("Error: Item not found or insufficient quantity in cart. User ID: " + userId + ", Item ID: " + itemId);
            } else {
                // Log once after successful removal
                logger.info("Item removed from cart. User ID: " + userId + ", Item ID: " + itemId + ", Quantity Removed: " + quantity);
            }
        } catch (SQLException e) {
            logger.severe("Error removing item from cart. User ID: " + userId + ", Item ID: " + itemId + ", Exception: " + e.getMessage());
            throw new SQLException(e);
        }
    }

    @Override
    public void viewTheCart(int userId) throws SQLException {
        String query = "SELECT Cart.item_id, Inventory.name, Cart.quantity, Inventory.price " +
                "FROM Cart INNER JOIN Inventory ON Cart.item_id = Inventory.item_id WHERE Cart.user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<CartItem> cartItems = new ArrayList<>();
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    String itemName = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    float price = rs.getFloat("price");
                    cartItems.add(new CartItem(itemId, itemName, quantity, price));
                }

                double total = cartItems.stream()
                        .mapToDouble(item -> item.getQuantity() * item.getPrice())
                        .sum();

                System.out.println("Items in your cart:");
                cartItems.forEach(item -> {
                    double subtotal = item.getQuantity() * item.getPrice();
                    System.out.println(item.getItemId() + ". " + item.getItemName() + " - Quantity: " + item.getQuantity() +
                            " - Price: $" + item.getPrice() + " - Subtotal: $" + subtotal);
                });
                System.out.println("Total cost of cart: $" + total);

                // Log once after cart is viewed, avoid duplicate logging
                logger.info("User viewed cart. User ID: " + userId + ", Total Cart Value: $" + total);
            }
        } catch (SQLException e) {
            logger.severe("Error viewing cart. User ID: " + userId + ", Exception: " + e.getMessage());
            throw new SQLException(e);
        }
    }

    @Override
    public float getCartTotal(int userId) throws SQLException {
        String query = "SELECT SUM(Inventory.price * Cart.quantity) AS total_amount " +
                "FROM Cart INNER JOIN Inventory ON Cart.item_id = Inventory.item_id WHERE Cart.user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    float totalAmount = rs.getFloat("total_amount");
                    // Log once when total is fetched
                    logger.info("Fetched total cart value. User ID: " + userId + ", Total Amount: $" + totalAmount);
                    return totalAmount;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting cart total. User ID: " + userId + ", Exception: " + e.getMessage());
            throw new SQLException(e);
        }
        return 0.0F;
    }

    public void exportToOrderItems(int userId, int orderId) throws SQLException {
        // Export items from cart to order_items
        String query = "INSERT INTO order_items (order_id, item_id, quantity) " +
                "SELECT ?, item_id, quantity FROM cart WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            // Log once after items are exported to order_items
            logger.info("Items exported to order items successfully. User ID: " + userId + ", Order ID: " + orderId);
        } catch (SQLException e) {
            logger.severe("Error exporting items to order items. User ID: " + userId + ", Order ID: " + orderId + ", Exception: " + e.getMessage());
            throw new RuntimeException(e);
        }

        // Clear cart after exporting
        clearCart(userId);
    }

    public boolean isUserAssociatedWithCart(int userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM cart WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error checking if user is associated with cart. User ID: " + userId + ", Exception: " + e.getMessage());
            throw new SQLException(e);
        }
        return false;
    }

    @Override
    public void clearCart(int userId) throws SQLException {
        String query = "DELETE FROM Cart WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            // Log once after cart is cleared
            logger.info("Cart cleared successfully. User ID: " + userId);
        } catch (SQLException e) {
            logger.severe("Error clearing cart. User ID: " + userId + ", Exception: " + e.getMessage());
            throw new SQLException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    // Inner class to represent a CartItem
    private static class CartItem {
        private final int itemId;
        private final String itemName;
        private final int quantity;
        private final float price;

        public CartItem(int itemId, String itemName, int quantity, float price) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.quantity = quantity;
            this.price = price;
        }

        public int getItemId() {
            return itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public int getQuantity() {
            return quantity;
        }

        public float getPrice() {
            return price;
        }
    }
}
