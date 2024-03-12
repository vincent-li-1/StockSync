package stocksync.model;

import org.springframework.beans.factory.annotation.Autowired;

public class Warehouse {
    private int warehouseId;
    private String warehouseName;
    private String warehouseAddress;
    private double lng, lat;

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

    public double getLong() {
    	return lng;
    }

    public double getLat() {
    	return lat;
    }

    public void setLong(double lng) {
    	this.lng = lng;
    }

    public void setLat(double lat) {
    	this.lat = lat;
    }
}
