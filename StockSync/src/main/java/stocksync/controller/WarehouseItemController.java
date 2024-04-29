package stocksync.controller;

import stocksync.model.WarehouseItem;
import stocksync.service.WarehouseItemService;
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
public class WarehouseItemController {

    private final WarehouseItemService warehouseItemService;

    @Autowired
    public WarehouseItemController(WarehouseItemService warehouseItemService) {
        this.warehouseItemService = warehouseItemService;
    }

    @GetMapping("/warehouseItem/search")
    public String getSearchPage() {
        return "warehouseItemSearch";
    }

    @GetMapping("/warehouseItem/SearchResults")
    public String getAllWarehouseItems(Model model,
                                   @RequestParam(value = "page") int page,
                                   @RequestParam(value = "sortBy") Optional<String> sortBy,
                                   @RequestParam(value = "sortMethod") Optional<String> sortMethod,
                                   @RequestParam(value = "searchKey") Optional<String> searchKey,
                                   @RequestParam(value = "searchValue") Optional<String> searchValue) {

        // Make sure string params exist for warehouseItem service get method
        String sortByExist = sortBy.isPresent() ? sortBy.get() : "";
        String sortMethodExist = sortMethod.isPresent() ? sortMethod.get() : "";
        String searchKeyExist = searchKey.isPresent() ? searchKey.get() : "";
        String searchValueExist = searchValue.isPresent() ? searchValue.get() : "";

        int totalNumEntries = this.warehouseItemService.getTotalNumEntries(searchKeyExist, searchValueExist);

        model.addAttribute("warehouseItems", this.warehouseItemService.getWarehouseItems(page, sortByExist, sortMethodExist, searchKeyExist, searchValueExist));
        model.addAttribute("pagesArray", this.warehouseItemService.getPagesArray(page, totalNumEntries));
        model.addAttribute("totalNumEntries", totalNumEntries);
        model.addAttribute("currentPage", page);

        // Pass sort params back to frontend for page links to use
        model.addAttribute("sortBy", sortByExist);
        model.addAttribute("sortMethod", sortMethodExist);

        // Pass search params back to frontend for page links to use
        model.addAttribute("searchKey", searchKeyExist);
        model.addAttribute("searchValue", searchValueExist);

        // Compute what number the first warehouseItem listed on the page is of the total list
        model.addAttribute("pageStartingNum", (page - 1) * 10 + 1);

        // Last warehouseItem on page is either multiple of 10 or the last entry
        model.addAttribute("pageEndingNum", Math.min(page * 10, totalNumEntries));

        return "warehouseItemSearchResults";
    }

    /*@GetMapping("/warehouse/{warehouseId}")
    public String getWarehouseDetails(Model model, @PathVariable("warehouseId") int warehouseId) {
        // Retrieve warehouse details
        Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);
        if (warehouse == null) {
            // Handle the case where the warehouse does not exist
            // This could be redirecting to an error page or listing page
            return "redirect:/warehouses";
        }
    
        // Retrieve the list of items in the warehouse
        List<WarehouseItem> warehouseItems = warehouseItemService.getItemsByWarehouseId(warehouseId);
    
        // Add the retrieved data to the model
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("warehouseItems", warehouseItems);
    
        // The name of the Thymeleaf template to render (without the .html extension)
        return "warehouseInfo";
    }*/
    

    @GetMapping("/warehouseItem/add")
    public String getAddWarehouseItemPage(Model model) {
        return "addWarehouseItem";
    }

    @PostMapping(value = "/insertWarehouseItem", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> insertSipment(@ModelAttribute WarehouseItem newWarehouseItem){
        try {
            this.warehouseItemService.createWarehouseItem(newWarehouseItem);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/warehouseItemSearchResults?page=1");
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>(
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Delete a warehouseItem with DELETE request trigger by a button in the frontend
     * @param warehouseItemIdList a list of id of warehouseItems to delete
     * @return url back to the search page
     */
    @PostMapping("/warehouseItem/delete")
    public String deleteWarehouseItem(@RequestBody List<Integer> warehouseItemIdList){
        this.warehouseItemService.deleteWarehouseItem(warehouseItemIdList);
        return "redirect:/warehouseItemSearchResults?page=1";
    }

    @PostMapping(value = "/warehouseItem/update", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> updateWarehouseItem(@ModelAttribute WarehouseItem updateWarehouseItem) {
        try {
            this.warehouseItemService.updateWarehouseItem(updateWarehouseItem);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/warehouseItemSearchResults?page=1");
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>(
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
