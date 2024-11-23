package model;

import java.time.LocalDate;

public class SavedCarts {

    private int savedCardId;
    private int userId;
    private int itemId;
    private int quantity;
    private LocalDate savedAt;


    public SavedCarts(int savedCardId, int userId, int itemId, int quantity, LocalDate savedAt) {
        this.savedCardId = savedCardId;
        this.userId = userId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.savedAt = savedAt;
    }

    public int getSavedCardId() {
        return savedCardId;
    }

    public void setSavedCardId(int savedCardId) {
        this.savedCardId = savedCardId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public LocalDate getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDate savedAt) {
        this.savedAt = savedAt;
    }

    @Override
    public String toString() {
        return "SavedCarts{" +
                "savedCardId=" + savedCardId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", savedAt=" + savedAt +
                '}';
    }
}
