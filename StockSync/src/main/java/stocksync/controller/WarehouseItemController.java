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

    /**
     * Delete a warehouseItem with UPDATE request trigger by a button in the frontend
     * @return url back to the search page
     */
    @GetMapping("/warehouseItem/search")
    public String getSearchPage() {
        return "warehouseItemSearch";
    }

    /**
     * search for a warehouseItem with SEARCH request trigger by a button in the frontend
     * @param model a list of id of warehouseItems to search
     * @param page page number currently on
     * @param sortBy sort by
     * @param sortMethod sort method
     * @param searchKey search key
     * @param searchValue search val
     * @return url back to the search page
     */
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
    
    /**
     * Add a warehouseItem with ADD request trigger by a button in the frontend
     * @param model a list of id of warehouseItems to add
     * @return url to add warehouse item
     */
    @GetMapping("/warehouseItem/add")
    public String getAddWarehouseItemPage(Model model) {
        return "addWarehouseItem";
    }

    /**
     * Insert a warehouseItem with INSERT request trigger by a button in the frontend
     * @param newWarehouseItem a list of id of warehouseItems to insert
     * @return response entity
     */
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

    /**
     * Delete a warehouseItem with UPDATE request trigger by a button in the frontend
     * @param updateWarehouseItem a list of id of warehouseItems to update
     * @return url back to the search page
     */
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
