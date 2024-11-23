package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderHistory {
    private int orderId;
    private int userId;
    private float totalAmount;
    private LocalDateTime timestamp;


    public OrderHistory(int orderId, float totalAmount, LocalDate timestamp) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.timestamp = timestamp.atStartOfDay();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OrderHistory{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", timestamp=" + timestamp +
                '}';
    }
}
