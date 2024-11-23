package dao;

import model.Inventory;

public interface InventoryDao {

    Inventory getItemById(int itemId);

    void updateInventoryAfterCheckout(int userId);

}
