package stocksync.controller;

import stocksync.mapper.WarehouseMapper;
import stocksync.model.Warehouse;
import stocksync.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

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
        model.addAttribute("pagesArray", this.warehouseService.getPagesArray(page));
        model.addAttribute("currentPage", page);
        return "warehouseSearchResults";
    }

    @GetMapping("/addWarehouse")
    public String getAddWarehousePage(Model model) {
        model.addAttribute("isWarehouse", true);
        return "addEntity";
    }

    @PostMapping(value = "/insertWarehouse", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String insertWarehouse(@ModelAttribute Warehouse newWh){
        this.warehouseService.createWarehouse(newWh);
        return "redirect:/warehouseSearchResults?page=1";
    }
}
