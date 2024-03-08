package stockcync.model;

import org.springframework.beans.factory.annotation.Autowired;

public class Warehouse {
    private int warehouseId;
    private String warehouseName;
    private String warehouseAddress;

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
}
