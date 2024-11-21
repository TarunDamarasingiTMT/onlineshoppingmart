package inventory;

import java.sql.SQLException;

public interface InventoryInterface {

    Inventory getItemById(int itemId);

    void updateInventoryAfterCheckout(int userId);

}
