package stocksync.controller;

import stocksync.mapper.ItemMapper;
import stocksync.model.Item;
import stocksync.model.Warehouse;
import stocksync.service.ItemService;
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
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/searchItem")
    public String getSearchPage() {
        return "searchItem";
    }

    

    @GetMapping("/itemSearchResults")
    public String getAllItems(Model model,
                    @RequestParam(value = "page") int page,
                    @RequestParam(value = "sortBy") Optional<String> sortBy,
                    @RequestParam(value = "sortMethod") Optional<String> sortMethod,
                    @RequestParam(value = "searchKey") Optional<String> searchKey,
                    @RequestParam(value = "searchValue") Optional<String> searchValue) {

        // Make sure string params exist for item service get method
        String sortByExist = sortBy.isPresent() ? sortBy.get() : "";
        String sortMethodExist = sortMethod.isPresent() ? sortMethod.get() : "";
        String searchKeyExist = searchKey.isPresent() ? searchKey.get() : "";
        String searchValueExist = searchValue.isPresent() ? searchValue.get() : "";

        int totalNumEntries = this.itemService.getTotalNumEntries(searchKeyExist, searchValueExist);
        int totalNumPages = this.itemService.getTotalNumPages(searchKeyExist, searchValueExist);

        model.addAttribute("items", this.itemService.getItems(page, sortByExist, sortMethodExist, searchKeyExist, searchValueExist));
        model.addAttribute("pagesArray", this.itemService.getPagesArray(page, totalNumEntries));
        model.addAttribute("totalNumEntries", totalNumEntries);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalNumPages", totalNumPages);

        // Pass sort params back to frontend for page links to use
        model.addAttribute("sortBy", sortByExist);
        model.addAttribute("sortMethod", sortMethodExist);

        model.addAttribute("searchKey", searchKeyExist);
        model.addAttribute("searchValue", searchValueExist);

        // Compute what number the first item listed on the page is of the total list
        model.addAttribute("pageStartingNum", (page - 1) * 10 + 1);

        // Last item on page is either multiple of 10 or the last entry
        model.addAttribute("pageEndingNum", Math.min(page * 10, totalNumEntries));

        return "itemSearchResults";
    }

    @GetMapping("/addItem")
    public String getAddItemPage(Model model) {
        //model.addAttribute("isItem", true);
        return "addItem";
    }

    @PostMapping(value = "/insertItem", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> insertItem(@ModelAttribute Item newIt){
        try {
            this.itemService.createItem(newIt);
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "Item added successfully");
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /*
     * GET endpoint for the edit Item page
     * It returns the name of the view to be rendered at the editItem url
     */
    @GetMapping("/editItem")
    public String getEditItemPage(Model model, @RequestParam(value = "ItemId") int id) {
        Item item = itemService.getItemById(id);
        // Add attributes to the model so it can be rendered in the view
        model.addAttribute("item", item);
        model.addAttribute("isItem", true);
        return "editItem";
    }

    /**
     * Delete a item with DELETE request trigger by a button in the frontend
     * @param itemIdList a list of id of the items to delete
     * @return url back to the search page
     */
    @PostMapping("/deleteItem")
    public String deleteItem(@RequestBody List<Integer> itemIdList){
        this.itemService.deleteItem(itemIdList);
        return "redirect:/itemSearchResults?page=1";
    }

    @PostMapping(value = "/updateItem", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> updateItem(@ModelAttribute Item updateIt){
        try {
            this.itemService.updateItem(updateIt);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/itemSearchResults?page=1");
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
