package cart;
import inventory.Inventory;
import inventory.InventoryDAO;

import java.sql.*;
import java.time.LocalDate;

public class CartDAO {
    private final Connection connection;

    public CartDAO(Connection connection) {
        this.connection = connection;
    }

    // Add item to cart
    public void addItemToCart(int userId, int itemId, int quantity) throws SQLException {

        InventoryDAO inventoryDAO = new InventoryDAO(connection);
        Inventory item = inventoryDAO.getItemById(itemId);
        int stock = item.getStock();

        if (stock < quantity){
            System.out.println("Invalid Stock for Item, available Stock <stock>");
        }


        String query = "INSERT INTO Cart (userId, itemId, quantity,lastUpdated) VALUES (?, ?, ?,?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, itemId);
            ps.setInt(3, quantity);
            LocalDate today = LocalDate.now();
            ps.setDate(4, Date.valueOf(today));
            ps.executeUpdate();
            System.out.println("Item added to the cart successfully.");
        }
    }


    // Remove item from cart
    public void removeItemFromCart(int userId, int itemId, int quantity) throws SQLException {
        String query = "UPDATE Cart SET quantity = quantity - ? WHERE userId = ? AND itemId = ? AND quantity >= ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, itemId);
            ps.setInt(4, quantity);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("Either the item was not found, or the quantity was insufficient.");
            } else {
                System.out.println("Item removed from the cart successfully.");
            }
        }
    }

    // View the cart items
    public void viewTheCart(int userId) throws SQLException {
        String query = "SELECT Cart.itemId, Inventory.name, Cart.quantity, Inventory.price " +
                "FROM Cart INNER JOIN Inventory ON Cart.itemId = Inventory.itemId WHERE Cart.userId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            double total = 0.0;
            System.out.println("Items in your cart:");
            while (rs.next()) {
                int itemId = rs.getInt("itemId");
                String itemName = rs.getString("name");
                int quantity = rs.getInt("quantity");
                float price = rs.getFloat("price");
                double subtotal = quantity * price;
                total += subtotal;

                System.out.println(itemId + ". " + itemName + " - Quantity: " + quantity + " - Price: $" + price + " - Subtotal: $" + subtotal);
            }
            System.out.println("Total cost of cart: $" + total);
        }
    }

    // Get total cart cost for a user
    public double getCartTotal(int userId) throws SQLException {
        String query = "SELECT SUM(Inventory.price * Cart.quantity) AS totalAmount " +
                "FROM Cart INNER JOIN Inventory ON Cart.itemId = Inventory.itemId WHERE Cart.userId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("totalAmount");
            }
        }
        return 0.0;
    }

    // View cart items & insert them into OrderItems

    public void viewCartItems(int userId, int orderId) throws SQLException {
        String query = "SELECT itemId, quantity FROM Cart WHERE userId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int itemId = rs.getInt("itemId");
                int quantity = rs.getInt("quantity");


                String insertOrderItemsQuery = "INSERT INTO OrderItems (orderId, itemId, quantity, subtotal) " +
                        "SELECT ?, ?, ?, (SELECT price * ? FROM Inventory WHERE itemId = ?)";
                try (PreparedStatement psInsert = connection.prepareStatement(insertOrderItemsQuery)) {
                    psInsert.setInt(1, orderId);
                    psInsert.setInt(2, itemId);
                    psInsert.setInt(3, quantity);
                    psInsert.setInt(4, quantity);
                    psInsert.setInt(5, itemId);
                    psInsert.executeUpdate();
                }
            }
        }
    }

    // Clear the cart after checkout
    public void clearCart(int userId) throws SQLException {
        String query = "DELETE FROM Cart WHERE userId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            System.out.println("Cart cleared successfully after checkout.");
        }
    }
}









