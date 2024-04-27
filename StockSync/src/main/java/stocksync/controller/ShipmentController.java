package stocksync.controller;

import stocksync.model.Shipment;
import stocksync.model.ShipmentRequest;
import stocksync.service.ShipmentService;
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
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/shipment/search")
    public String getSearchPage() {
        return "shipmentSearch";
    }

    @GetMapping("/shipment/SearchResults")
    public String getAllShipments(Model model,
                                   @RequestParam(value = "page") int page,
                                   @RequestParam(value = "sortBy") Optional<String> sortBy,
                                   @RequestParam(value = "sortMethod") Optional<String> sortMethod,
                                   @RequestParam(value = "searchKey") Optional<String> searchKey,
                                   @RequestParam(value = "searchValue") Optional<String> searchValue) {

        // Make sure string params exist for shipment service get method
        String sortByExist = sortBy.isPresent() ? sortBy.get() : "";
        String sortMethodExist = sortMethod.isPresent() ? sortMethod.get() : "";
        String searchKeyExist = searchKey.isPresent() ? searchKey.get() : "";
        String searchValueExist = searchValue.isPresent() ? searchValue.get() : "";

        int totalNumEntries = this.shipmentService.getTotalNumEntries(searchKeyExist, searchValueExist);

        model.addAttribute("shipments", this.shipmentService.getShipments(page, sortByExist, sortMethodExist, searchKeyExist, searchValueExist));
        model.addAttribute("pagesArray", this.shipmentService.getPagesArray(page, totalNumEntries));
        model.addAttribute("totalNumEntries", totalNumEntries);
        model.addAttribute("currentPage", page);

        // Pass sort params back to frontend for page links to use
        model.addAttribute("sortBy", sortByExist);
        model.addAttribute("sortMethod", sortMethodExist);

        // Pass search params back to frontend for page links to use
        model.addAttribute("searchKey", searchKeyExist);
        model.addAttribute("searchValue", searchValueExist);

        // Compute what number the first shipment listed on the page is of the total list
        model.addAttribute("pageStartingNum", (page - 1) * 10 + 1);

        // Last shipment on page is either multiple of 10 or the last entry
        model.addAttribute("pageEndingNum", Math.min(page * 10, totalNumEntries));

        return "shipmentSearchResults";
    }

    @GetMapping("/shipment/add")
    public String getAddShipmentPage(Model model) {
        return "addShipment";
    }

    @PostMapping("/shipment/create")
    public ResponseEntity<String> createShipment(@RequestBody ShipmentRequest body){
        try{
            this.shipmentService.newCreateShipment(body);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<String>(headers,HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<String>(
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(value = "/insertShipment", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> insertShipment(@ModelAttribute Shipment newShipment){
        try {
            this.shipmentService.createShipment(newShipment);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/shipmentSearchResults?page=1");
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>(
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Delete a shipment with DELETE request trigger by a button in the frontend
     * @param shipmentIdList a list of id of Shipments to delete
     * @return url back to the search page
     */
    @PostMapping("/shipment/delete")
    public String deleteShipment(@RequestBody List<Integer> shipmentIdList){
        this.shipmentService.deleteShipment(shipmentIdList);
        return "redirect:/shipmentSearchResults?page=1";
    }

    @PostMapping(value = "/shipment/update", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> updateShipment(@ModelAttribute Shipment updateShipment) {
        try {
            this.shipmentService.updateShipment(updateShipment);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/shipmentSearchResults?page=1");
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>(
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
