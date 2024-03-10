package stocksync.controller;

import stocksync.mapper.WarehouseMapper;
import stocksync.model.Warehouse;
import stocksync.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/search")
    public String getSearchPage() {
        return "search";
    }

    @GetMapping("/warehouseSearchResults")
    public String getAllWarehouses(Model model, @RequestParam(value = "page") int page) {
        model.addAttribute("warehouses", this.warehouseService.getWarehouses(page));
        return "warehouseSearchResults";
    }

    @PostMapping("/insertWarehouse")
    public ResponseEntity<?> insertWarehouse(@RequestBody Warehouse newWh){
        this.warehouseService.createWarehouse(newWh);
        return ResponseEntity.ok().build();
    }
}
