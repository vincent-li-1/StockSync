package stocksync.model;

public class Shipment {
    int shipmentId;
    int warehouseFromId;
    int warehouseToId;
    String shipmentDate;
    String shipmentStatus;

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

    public String getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(String shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }
}
