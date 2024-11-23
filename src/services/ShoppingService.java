package services;

import daoimpl.*;
import model.PromoCodes;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

public class ShoppingService {
    private final CartDaoImpl cartDAO;
    private final PromoCodeDaoImpl promoCodeDAO;
    private final InventoryDaoImpl inventoryItems;
    private final OrderHistoryDaoImpl orderHistoryDAO;
    private final SavedCartsDaoImpl savedCartsDAO;

    public ShoppingService(Connection connection) {
        this.cartDAO = new CartDaoImpl(connection);
        this.promoCodeDAO = new PromoCodeDaoImpl(connection);
        this.inventoryItems = new InventoryDaoImpl(connection);
        this.orderHistoryDAO = new OrderHistoryDaoImpl(connection);
        this.savedCartsDAO = new SavedCartsDaoImpl(connection);
    }

    // validation method for userId (input)
    private boolean validateUserId(int userId) throws SQLException {
        if (!cartDAO.isUserAssociatedWithCart(userId)) {
            System.err.println("Invalid User ID. Please try again.");
            return false;
        }
        return true;
    }

    // Add item to the cart
    public void addItemToCart(int userId, int itemId, int quantity) throws SQLException {

            cartDAO.addItemToCart(userId, itemId, quantity);
            System.out.println("Item added to cart successfully!");

    }

    // Remove item from the cart
    public void removeItemFromCart(int userId, int itemId, int quantity) throws SQLException {
        if (validateUserId(userId)) {
            cartDAO.removeItemFromCart(userId, itemId, quantity);
            System.out.println("Item removed from cart successfully!");
        }
    }

    // View the cart
    public void viewTheCart(int userId) throws SQLException {
        if (validateUserId(userId)) {
            cartDAO.viewTheCart(userId);
        }
    }

    // Apply Promo Code
    public float applyPromoCode(int userId, String promoCodeInput) throws SQLException {
        if (validateUserId(userId)) {
            float cartTotal = cartDAO.getCartTotal(userId);
            PromoCodes promoCode = promoCodeDAO.getPromoCodeByCode(promoCodeInput);

            if (promoCode != null) {
                float discountAmount = (promoCode.getDiscountPercentage() / 100) * cartTotal;
                float amountAfterDiscount = cartTotal - discountAmount;
                System.out.println("Promo Code Applied: " + promoCodeInput);
                System.out.println("Discount Amount: $" + discountAmount);
                System.out.println("Amount After Discount: $" + amountAfterDiscount);
                return amountAfterDiscount; // Return the updated total after discount
            } else {
                System.err.println("Invalid or expired promo code.");
            }
        }
        return cartDAO.getCartTotal(userId); // Return the original total if promo code is invalid
    }

    // Checkout method
    public void checkout(int userId, String promoCodeInput) throws SQLException {
        Connection connection = cartDAO.getConnection();
        try {
            connection.setAutoCommit(false);  // for performing multiple functions in same method
            Savepoint savepoint = connection.setSavepoint("beforeCheckout");

            if (!validateUserId(userId)) {
                return; // exit if user ID is invalid
            }

            // Get initial cart total
            float cartTotal = cartDAO.getCartTotal(userId);

            // Apply promo code if provided
            if (promoCodeInput != null && !promoCodeInput.isEmpty()) {
                cartTotal = applyPromoCode(userId, promoCodeInput);
            }

            // Update inventory after checkout
            inventoryItems.updateInventoryAfterCheckout(userId);

            // Add order to history and get order ID
            int orderId = orderHistoryDAO.addOrderToHistory(userId, cartTotal);
            if (orderId == -1) {
                connection.rollback(savepoint); // Rollback if order creation fails
                System.out.println("Error occurred while creating the order. Transaction rolled back.");
                return;
            }

            // Export cart items to the order items table
            cartDAO.exportToOrderItems(userId, orderId);
            cartDAO.clearCart(userId);

            System.out.println("Checkout successful! Your Cart Cleared Successfully.");
            System.out.println("Total Cost of Cart: $" + cartTotal);
            System.out.println("Delivery Date: " + java.time.LocalDate.now().plusDays(3));
            connection.commit(); // Commit the transaction

        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Rollback the transaction if any error occurs
            }
            System.out.println("Error during checkout: " + e.getMessage());
            throw new RuntimeException("Error during checkout", e);
        }
    }

    // View order history
    public void viewOrderHistory(int userId) throws SQLException {

            orderHistoryDAO.getOrderHistory(userId);

    }

    // Save cart items
    public void saveCartItems(int userId) throws SQLException {
        if (validateUserId(userId)) {
            savedCartsDAO.saveCartItems(userId);
            System.out.println("Items are saved to saved cart. Thank you for shopping!");
        }
    }
}
