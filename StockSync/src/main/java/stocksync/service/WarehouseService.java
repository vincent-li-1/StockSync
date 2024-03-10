package stocksync.service;

import java.util.List;
import stocksync.model.Warehouse;
import stocksync.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseService implements IWarehouseService {
    @Autowired
    private final WarehouseMapper whMapper;
    public WarehouseService(WarehouseMapper whMapper){
        this.whMapper = whMapper;
    }
    public void createWarehouse(Warehouse newWh) {
        this.whMapper.insertWarehouse(newWh);
    }
    public List<Warehouse> getWarehouses(int page) {
        int limit = 25;
        int offset = limit * (page - 1);
        return whMapper.find(limit, offset);
    }
    
}