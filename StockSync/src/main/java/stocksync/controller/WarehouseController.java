package stocksync.controller;

import stocksync.mapper.WarehouseMapper;
import stocksync.model.Warehouse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WarehouseController {
    private final WarehouseMapper whMapper;
    public WarehouseController(WarehouseMapper whMapper){
        this.whMapper = whMapper;
    }
    @GetMapping("/warehouse")
    public List<Warehouse> findALl(){
        return whMapper.findAll();
    }
    @PostMapping("/insertWarehouse")
    public ResponseEntity<?> insertWarehouse(@RequestBody Warehouse newWh){
        whMapper.insertWarehouse(newWh);
        return ResponseEntity.ok().build();
    }
}
