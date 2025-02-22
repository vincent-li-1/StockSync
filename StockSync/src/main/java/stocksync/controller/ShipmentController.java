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

    /**
     * Endpoint for homepage of Shipment
     *
     * @return shipmentSearch is the html page for shipment home
     */
    @GetMapping("/shipmentSearch")
    public String getSearchPage() {
        return "shipmentSearch";
    }

    /**
     * Endpoint for shipment search result page
     *
     * @param model model for holding properties needed to render the search result page
     * @param page  result page number
     * @param sortBy key used to sort search results
     * @param sortMethod increasing or decreasing order
     * @param searchKey key used for searching
     * @param searchValue value used for searching
     * @return
     */
    @GetMapping("/shipmentSearchResults")
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
        int totalNumPages = this.shipmentService.getTotalNumPages(searchKeyExist, searchValueExist);

        model.addAttribute("shipments", this.shipmentService.getShipments(page, sortByExist, sortMethodExist, searchKeyExist, searchValueExist));
        model.addAttribute("pagesArray", this.shipmentService.getPagesArray(page, totalNumEntries));
        model.addAttribute("totalNumEntries", totalNumEntries);
        model.addAttribute("totalNumPages", totalNumPages);
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

    /**
     * Endpoint for add shipment page
     *
     * @param model
     * @return addShipment html page
     */
    @GetMapping("/shipment/add")
    public String getAddShipmentPage(Model model) {
        return "addShipment";
    }

    /**
     * Endpoint for creating shipment from a warehouse to a warehouse
     *
     * @param body http request body
     * @return a http response entity with a 200 if shipment is created successfully, otherwise a 400
     */
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

    /**
     * Endpoint for creating shipment from factory to a warehouse
     *
     * @param body http request body
     * @return a http response entity with a 200 if shipment is created successfully, otherwise a 400
     */
    @PostMapping("/shipment/factoryShipment")
    public ResponseEntity<String> factoryShipment(@RequestBody ShipmentRequest body){
        try{
            this.shipmentService.factoryShipment(body);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<String>(headers,HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<String>(
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint for creating shipment from a warehouse to customer
     *
     * @param body http request body
     * @return a http response entity with a 200 if shipment is created successfully, otherwise a 400
     */
    @PostMapping("/shipment/customerShipment")
    public ResponseEntity<String> customerShipment(@RequestBody ShipmentRequest body){
        try{
            this.shipmentService.customerShipment(body);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<String>(headers,HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<String>(
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint for creating shipment with a form in the frontend
     * Note this should not be used directly, for creating shipment
     * Use '/shipment/create' '/shipment/factoryShipment' '/shipment/customerShipment' instead
     *
     * @param newShipment shipment object created by the form
     * @return a http response entity with a 200 if shipment is created successfully, otherwise a 400
     */
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

    /**
     * Endpoint for updating existing shipment
     *
     * @param updateShipment
     * @return a http response entity with a 200 if shipment is edited successfully, otherwise a 400
     */
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
