package cart;

import java.sql.SQLException;

public interface CartInterface {

    void addItemToCart(int userId,int itemId, int quantity) throws SQLException;

    void removeItemFromCart(int userId, int itemid,int quantity);

    void viewTheCart(int userId) throws SQLException;

    void getCartTotal();
}
