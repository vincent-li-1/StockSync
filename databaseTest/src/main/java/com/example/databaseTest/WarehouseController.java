package com.example.databaseTest;

import com.example.mapper.WarehouseMapper;
import com.example.model.Warehouse;
import org.springframework.web.bind.annotation.GetMapping;
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

}
