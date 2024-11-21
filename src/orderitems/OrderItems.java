package orderitems;

public class OrderItems {
    private int orderItemId;
    private int orderId;
    private int itemId;
    private int quantity;
    private float subtotal;

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }


    public OrderItems(int orderItemId, int orderId, int itemId, int quantity, float subtotal) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "OrderItems{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + orderId +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                '}';
    }
}
