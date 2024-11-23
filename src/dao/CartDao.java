package dao;

import java.sql.SQLException;

public interface CartDao {

    void addItemToCart(int userId, int itemId, int quantity) throws SQLException;

    void removeItemFromCart(int userId, int itemId, int quantity) throws SQLException;

    void viewTheCart(int userId) throws SQLException;

    float getCartTotal(int userId) throws SQLException;

    void clearCart(int userId) throws SQLException;
}
