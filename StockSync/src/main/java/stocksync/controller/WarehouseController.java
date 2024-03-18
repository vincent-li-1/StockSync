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
import java.lang.Math;

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

        int totalNumEntries = this.warehouseService.getTotalNumEntries();

        model.addAttribute("warehouses", this.warehouseService.getWarehouses(page));
        model.addAttribute("pagesArray", this.warehouseService.getPagesArray(page));
        model.addAttribute("totalNumEntries", totalNumEntries);
        model.addAttribute("currentPage", page);

        // Compute what number the first warehouse listed on the page is of the total list
        model.addAttribute("pageStartingNum", (page - 1) * 10 + 1);
        // Last warehouse on page is either multiple of 10 or the last entry
        model.addAttribute("pageEndingNum", Math.min(page * 10, totalNumEntries));
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

    @PostMapping(value = "/deleteWarehouse", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String deleteWarehouse(@ModelAttribute Warehouse deleteWh){
        this.warehouseService.deleteWarehouse(deleteWh);
        return "redirect:/warehouseSearchResults?page=1";
    }

    @PostMapping(value = "/updateWarehouse", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String updateWarehouse(@ModelAttribute Warehouse updateWh){
        this.warehouseService.updateWarehouse(updateWh);
        return "redirect:/warehouseSearchResults?page=1";
    }
}
