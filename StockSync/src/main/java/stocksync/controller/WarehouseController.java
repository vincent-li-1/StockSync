package stocksync.controller;

import org.apache.ibatis.annotations.Delete;
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
import java.util.Optional;
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

        model.addAttribute("warehouses", this.warehouseService.getWarehouses(page, sortByExist, sortMethodExist, searchKeyExist, searchValueExist));
        model.addAttribute("pagesArray", this.warehouseService.getPagesArray(page, totalNumEntries));
        model.addAttribute("totalNumEntries", totalNumEntries);
        model.addAttribute("currentPage", page);

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

    @PostMapping(value = "/insertWarehouse", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> insertWarehouse(@ModelAttribute Warehouse newWh){
        try {
            this.warehouseService.createWarehouse(newWh);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/warehouseSearchResults?page=1");
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>(
                e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }

    /**
     * Delete a warehouse with POST request and a form
     * @param deleteWh warehouse object that needs to be deleted
     * @return url back to the search page
     */
    @PostMapping(value = "/deleteWarehouse", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String deleteWarehouse(@ModelAttribute Warehouse deleteWh){
        this.warehouseService.deleteWarehouse(deleteWh);
        return "redirect:/warehouseSearchResults?page=1";
    }

    /**
     * Delete a warehouse with DELETE request trigger by a button in the frontend
     * @param warehouseId id of the warehouse to delete
     * @return url back to the search page
     */
    @DeleteMapping("/deleteWarehouse/{warehouseId}")
    public String deleteWarehouseButton(@PathVariable("warehouseId") int warehouseId){
        this.warehouseService.deleteWarehouseButton(warehouseId);
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
            return new ResponseEntity<String>(
                e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
