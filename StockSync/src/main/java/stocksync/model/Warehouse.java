package stocksync.model;

import org.springframework.beans.factory.annotation.Autowired;

public class Warehouse {
    private int warehouseId;
    private String warehouseName;
    private String warehouseAddress;
    private double warehouseLong, warehouseLat;

    @Override
    public boolean equals(Object otherObject){
        //check for null and compare types
        if (otherObject == null || this.getClass() != otherObject.getClass()) return false;

        //compare the warehouseId
        Warehouse otherWarehouse = (Warehouse) otherObject;
        return otherWarehouse.getWarehouseId() == this.getWarehouseId();
    }
    public int getWarehouseId() {
        return warehouseId;
    }
    @Autowired
    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public double getWarehouseLong() {
    	return warehouseLong;
    }

    public double getWarehouseLat() {
    	return warehouseLat;
    }

    public void setWarehouseLong(double warehouseLong) {
    	this.warehouseLong = warehouseLong;
    }

    public void setWarehouseLat(double warehouseLat) {
    	this.warehouseLat = warehouseLat;
    }
}
