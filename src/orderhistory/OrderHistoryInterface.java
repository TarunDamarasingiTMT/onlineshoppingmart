package orderhistory;

public interface OrderHistoryInterface {

   void getOrderHistory(int userId);
   int addOrderToHistory(int userId, float totalCost);
}
