package model;

public class Inventory {

    private int itemId;
    private String name;
    private float price;
    private int stock;


    //Getters and Setters


    //constructor
    public Inventory(int itemId, String name, float price, int stock) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "itemId=" + itemId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
