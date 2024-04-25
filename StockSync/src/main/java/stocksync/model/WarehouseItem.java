package stocksync.model;

public class WarehouseItem {
    int warehouseItemId;
    int warehouseId;
    int itemId;
    int quantity;

    @Override
    public boolean equals(Object otherObject){
        //check for null and compare types
        if (otherObject == null || this.getClass() != otherObject.getClass()) return false;
        //compare the warehouseId
        WarehouseItem otherWarehouseItem = (WarehouseItem) otherObject;
        return otherWarehouseItem.getWarehouseItemId() == this.getWarehouseItemId();
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getWarehouseItemId() {
        return warehouseItemId;
    }

    public void setWarehouseItemId(int warehouseItemId) {
        this.warehouseItemId = warehouseItemId;
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
}
