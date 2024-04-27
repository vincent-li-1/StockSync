package stocksync.model;

public class Shipment {
    int shipmentId;
    int warehouseFromId;
    int warehouseToId;

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

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

}
