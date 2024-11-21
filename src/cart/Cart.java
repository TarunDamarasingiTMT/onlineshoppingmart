package cart;
import java.time.LocalDateTime;

public class Cart {
    private int cartId;
    private int itemId;
    private int quantity;
    private int userId;
    private LocalDateTime lastUpdated;

    //Getters and Setters
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    //constructor


    public Cart(int cartId, int itemId, int quantity, int userId, LocalDateTime lastUpdated) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.userId = userId;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", userId=" + userId +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
