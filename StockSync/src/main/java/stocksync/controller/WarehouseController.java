package stocksync.controller;

import stocksync.mapper.WarehouseMapper;
import stocksync.model.Warehouse;
import stocksync.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @RequestMapping("/warehouse")
    public String getWarehouses(Model model) {
        model.addAttribute("warehouses", this.warehouseService.getWarehouses());
        return "warehouse";
    }

    @PostMapping("/insertWarehouse")
    public ResponseEntity<?> insertWarehouse(@RequestBody Warehouse newWh){
        this.warehouseService.createWarehouse(newWh);
        return ResponseEntity.ok().build();
    }
}
