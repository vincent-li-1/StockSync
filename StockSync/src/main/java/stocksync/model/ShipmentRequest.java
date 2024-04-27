package stocksync.model;

import java.util.ArrayList;

public class ShipmentRequest {
    int warehouseFromId;
    int warehouseToId;
    int shipmentId;
    ArrayList<Integer> itemIdList;
    ArrayList<Integer> itemQuantityList;

    public int getWarehouseFromId() {
        return warehouseFromId;
    }

    public void setWarehouseFromId(int warehouseFromId) {
        this.warehouseFromId = warehouseFromId;
    }

    public int getWarehouseToId() {
        return warehouseToId;
    }

    public void setWarehouseToId(int warehouseToId) {
        this.warehouseToId = warehouseToId;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public ArrayList<Integer> getItemIdList() {
        return itemIdList;
    }

    public void setItemIdList(ArrayList<Integer> itemIdList) {
        this.itemIdList = itemIdList;
    }

    public ArrayList<Integer> getItemQuantityList() {
        return itemQuantityList;
    }

    public void setItemQuantityList(ArrayList<Integer> itemQuantityList) {
        this.itemQuantityList = itemQuantityList;
    }
}
