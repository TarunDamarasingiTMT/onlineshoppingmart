package dao;

import java.sql.SQLException;

public interface OrderHistoryDao {

    void getOrderHistory(int userId) throws SQLException;

    int addOrderToHistory(int userId, float totalCost) throws SQLException;

}
