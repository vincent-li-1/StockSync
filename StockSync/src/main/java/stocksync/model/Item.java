package stocksync.model;

import org.springframework.beans.factory.annotation.Autowired;

public class Item {
    private int itemId;
    private String itemName;
    private double itemSize, itemPrice;

    public int getItemId() {
        return itemId;
    }
    @Autowired
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemSize() {
        return itemSize;
    }

    public void setItemSize(double itemSize) {
        this.itemSize = itemSize;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

}
