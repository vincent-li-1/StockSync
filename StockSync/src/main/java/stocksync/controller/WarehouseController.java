package stocksync.controller;

import org.apache.ibatis.annotations.Delete;
import stocksync.mapper.WarehouseMapper;
import stocksync.model.Warehouse;
import stocksync.model.ItemDetailsDTO;
import stocksync.service.WarehouseService;
import stocksync.model.WarehouseItem;
import stocksync.service.WarehouseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.lang.Math;

@Controller
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final WarehouseItemService warehouseItemService;
    

    @Autowired
    public WarehouseController(WarehouseService warehouseService, WarehouseItemService warehouseItemService) {
        this.warehouseService = warehouseService;
        this.warehouseItemService = warehouseItemService;
    }

    /*
     * GET endpoint for the warehouse info page 
     * It returns the name of the view to be rendered for the warehuoseInfo url
     */
    @GetMapping("/warehouseInfo")
    public String getWarehouseInfo(Model model, @RequestParam(value = "WarehouseId") int id) {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        // Add attributes to the model so it can be rendered in the view
        model.addAttribute("warehouse", warehouse);
        List<ItemDetailsDTO> warehouseItems = warehouseItemService.getItemDetailsByWarehouseId(id);
        model.addAttribute("warehouseItems", warehouseItems);
        return "warehouseInfo";
    }

    @GetMapping("/search")
    public String getSearchPage() {
        return "search";
    }

    @GetMapping("/warehouseSearchResults")
    public String getAllWarehouses(Model model,
                    @RequestParam(value = "page") int page,
                    @RequestParam(value = "sortBy") Optional<String> sortBy,
                    @RequestParam(value = "sortMethod") Optional<String> sortMethod,
                    @RequestParam(value = "searchKey") Optional<String> searchKey,
                    @RequestParam(value = "searchValue") Optional<String> searchValue) {

        // Make sure string params exist for warehouse service get method
        String sortByExist = sortBy.isPresent() ? sortBy.get() : "";
        String sortMethodExist = sortMethod.isPresent() ? sortMethod.get() : "";
        String searchKeyExist = searchKey.isPresent() ? searchKey.get() : "";
        String searchValueExist = searchValue.isPresent() ? searchValue.get() : "";

        int totalNumEntries = this.warehouseService.getTotalNumEntries(searchKeyExist, searchValueExist);
        int totalNumPages = this.warehouseService.getTotalNumPages(searchKeyExist, searchValueExist);

        model.addAttribute("warehouses", this.warehouseService.getWarehouses(page, sortByExist, sortMethodExist, searchKeyExist, searchValueExist));
        model.addAttribute("pagesArray", this.warehouseService.getPagesArray(page, totalNumEntries));
        model.addAttribute("totalNumEntries", totalNumEntries);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalNumPages", totalNumPages);

        // Pass sort params back to frontend for page links to use
        model.addAttribute("sortBy", sortByExist);
        model.addAttribute("sortMethod", sortMethodExist);

        // Pass search params back to frontend for page links to use
        model.addAttribute("searchKey", searchKeyExist);
        model.addAttribute("searchValue", searchValueExist);

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

    /*
     * GET endpoint for the edit warehouse page
     * It returns the name of the view to be rendered for the editWarehouse url
     */
    @GetMapping("/editWarehouse")
    public String getEditWarehousePage(Model model, @RequestParam(value = "WarehouseId") int id) {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        // Add attributes to the model so it can be rendered in the view
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("isWarehouse", true);
        return "editEntity";
    }

    @PostMapping(value = "/insertWarehouse", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> insertWarehouse(@ModelAttribute Warehouse newWh) {
        try {
            this.warehouseService.createWarehouse(newWh);
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "Warehouse added successfully");
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }

    }

    /**
     * Delete a warehouse with DELETE request trigger by a button in the frontend
     * @param warehouseIdList a list of id of the warehouse to delete
     * @return url back to the search page
     */
    @PostMapping("/deleteWarehouse")
    public String deleteWarehouseButton(@RequestBody List<Integer> warehouseIdList){
        this.warehouseService.deleteWarehouseButton(warehouseIdList);
        return "redirect:/warehouseSearchResults?page=1";
    }

    @PostMapping(value = "/updateWarehouse", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> updateWarehouse(@ModelAttribute Warehouse updateWh) {
        try {
            this.warehouseService.updateWarehouse(updateWh);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/warehouseSearchResults?page=1");
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
