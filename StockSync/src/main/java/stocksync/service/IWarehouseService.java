package stocksync.service;

import java.util.List;
import stocksync.model.Warehouse;

public interface IWarehouseService {
    public abstract void createWarehouse(Warehouse newWh);
    public abstract List<Warehouse> getWarehouses(int page, String sortBy, String sortMethod);
}