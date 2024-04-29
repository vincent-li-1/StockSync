package stocksync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stocksync.mapper.ShipmentMapper;
import stocksync.mapper.WarehouseItemMapper;
import stocksync.model.Shipment;
import stocksync.model.ShipmentRequest;
import stocksync.model.Warehouse;
import stocksync.model.WarehouseItem;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShipmentService {
    private final ShipmentMapper shipmentMapper;

    private  final WarehouseItemMapper warehouseItemMapper;
    @Autowired
    public ShipmentService(ShipmentMapper mapper, WarehouseItemMapper whMapper){
        this.shipmentMapper = mapper;
        this.warehouseItemMapper = whMapper;
    }

    public void createShipment(Shipment shipment){
        // Check the total number of existing shipments
        int totalShipments = shipmentMapper.getTotalNumEntries();
        int maxShipments = 500;
        // If the maximum limit is reached, prevent adding the shipment
        if (totalShipments >= maxShipments) {
            throw new IllegalArgumentException("Maximum shipment capacity reached. Cannot add more shipments.");
        }
        // Otherwise, proceed with adding the shipment
        this.shipmentMapper.insertShipment(shipment);
    }

    public void newCreateShipment(ShipmentRequest body){
        int warehouseFromId = body.getWarehouseFromId();
        int warehouseToId = body.getWarehouseToId();
        ArrayList<Integer> itemIdList = body.getItemIdList();
        ArrayList<Integer> itemQuantityList = body.getItemQuantityList();
        Shipment newShipment = new Shipment();
        newShipment.setWarehouseFromId(warehouseFromId);
        newShipment.setWarehouseToId(warehouseToId);
        this.shipmentMapper.insertShipment(newShipment);

        int shipmentId = newShipment.getShipmentId();
        for(int i = 0; i < itemIdList.size(); i++){
            int currentId = itemIdList.get(i);
            int currentQuantity = itemQuantityList.get(i);
            this.warehouseItemMapper.subtractQuantity(currentQuantity,warehouseFromId,currentId);
            if(!this.warehouseItemMapper.hasItem(warehouseToId,currentId)){
                WarehouseItem newWI = new WarehouseItem();
                newWI.setItemId(currentId);
                newWI.setQuantity(0);
                newWI.setWarehouseId(warehouseToId);
                this.warehouseItemMapper.insertWarehouseItem(newWI);
            }
            this.warehouseItemMapper.addQuantity(currentQuantity,warehouseToId,currentId);
        }
    }
    public void factoryShipment(ShipmentRequest body){
        int warehouseToId = body.getWarehouseToId();
        ArrayList<Integer> itemIdList = body.getItemIdList();
        ArrayList<Integer> itemQuantityList = body.getItemQuantityList();
        for(int i = 0; i < itemIdList.size(); i++){
            int currentId = itemIdList.get(i);
            int currentQuantity = itemQuantityList.get(i);
            if(!this.warehouseItemMapper.hasItem(warehouseToId,currentId)){
                WarehouseItem newWI = new WarehouseItem();
                newWI.setItemId(currentId);
                newWI.setQuantity(0);
                newWI.setWarehouseId(warehouseToId);
                this.warehouseItemMapper.insertWarehouseItem(newWI);
            }
            this.warehouseItemMapper.addQuantity(currentQuantity,warehouseToId,currentId);
        }
    }

    public void customerShipment(ShipmentRequest body){
        int warehouseFromId = body.getWarehouseFromId();
        ArrayList<Integer> itemIdList = body.getItemIdList();
        ArrayList<Integer> itemQuantityList = body.getItemQuantityList();
        for(int i = 0; i < itemIdList.size(); i++){
            int currentId = itemIdList.get(i);
            int currentQuantity = itemQuantityList.get(i);
            if(this.warehouseItemMapper.hasItem(warehouseFromId,currentId)){
                this.warehouseItemMapper.subtractQuantity(currentQuantity,warehouseFromId,currentId);
            }
        }
    }

    // Helper method to convert human-friendly attribute names to SQL query column names
    private String convertKeyToSqlColumn(String stringToConvert) {
        switch (stringToConvert) {
            case "origin":
                return "warehouse_from_id";
            case "destination":
                return "warehouse_to_id";
            default:
                return "shipment_id";
        }
    }

    /**
     * Method to get a paginated list of warehouses based on sorting and search parameters
     * @param page is the current page to get
     * @param sortBy is the column/attribute that the request wants to results sorted by
     * @param sortMethod is the method, ascending or descending, to sort results by
     * @param searchKey is the column/attribute that the request wants to search by (origin/destination/id)
     * @param searchValue is the value that the request wants to search for
     * @return List of shipment objects
     */
    public List<Shipment> getShipments(int page, String sortBy, String sortMethod, String searchKey, String searchValue) {
        // Limit is hardcoded to be 10 per page, offset is calculated based off that limit. This is the only line that needs
        // to be changed to change limit per page.
        int limit = 10;
        int offset = limit * (page - 1);

        // Get the right table column name for sortBy
        String sortByAsColumnName = convertKeyToSqlColumn(sortBy);

        // Set a default for sortMethod
        if (!sortMethod.equals("desc")) {
            sortMethod = "asc";
        }

        // If no search key/value, get all
        if (searchKey.isEmpty() || searchValue.isEmpty()) {
            return shipmentMapper.findAll(limit, offset, sortByAsColumnName, sortMethod);
        }

        // Get the right table column name for searchKey
        String searchKeyAsColumnName = convertKeyToSqlColumn(searchKey);

        // Convert searchValue to have search wildcard if the search is by name or address (we don't want wildcards for long/lat)
        String searchValueWithWildcard = (searchKey.equals("origin") || searchKey.equals("destination")) ? "%" + searchValue + "%" : searchValue;

        return shipmentMapper.findBySearch(limit, offset, sortByAsColumnName, sortMethod, searchKeyAsColumnName, searchValueWithWildcard);
    }

    /**
     * Method to get a count of the total number of entries for a get request
     * @param searchKey is the column/attribute that the get is searched by
     * @param searchValue is the value that the get searched for
     * @return number of entries based on the parameters (if any)
     */
    public int getTotalNumEntries(String searchKey, String searchValue) {
        // Get total number of all entries if there are no search params
        if (searchKey.equals("") || searchValue.equals("")) {
            return shipmentMapper.getTotalNumEntries();
        }

        // Get the right table column name for searchKey
        String searchKeyAsColumnName = convertKeyToSqlColumn(searchKey);
        // TODO: Check that searchKeyAsColumnName is not id, if it is throw error

        // Convert searchValue to have search wildcard if the search is by name or address (we don't want to wildcard for long/lat)
        String searchValueWithWildcard = (searchKey.equals("origin") || searchKey.equals("destination")) ? "%" + searchValue + "%" : searchValue;

        return shipmentMapper.getSearchNumEntries(searchKeyAsColumnName, searchValueWithWildcard);
    }

    /**
     * Method to get an array of the 5 page numbers to display across the bottom
     * of the page depending on which current page the user is on. The user's page
     * should be "centered" between the 5 options, unless there are fewer than 2 other
     * options in either direction
     * @param currentPage that the user is on
     * @param totalEntries that the given search yielded
     * @return array of integers representing page numbers
     */
    public int[] getPagesArray(int currentPage, int totalEntries) {
        int numPages = (int) Math.ceil((double) totalEntries/10);
        // Display all of pages if there are less than 5 pages
        if (numPages < 5) {
            int[] pagesArray = new int[numPages];
            for (int i = 1; i <= numPages; i++) {
                pagesArray[i-1] = i;
            }
            return pagesArray;
        }
        // If there are more than 5 pages, only display the 5 pages centered around current page
        int[] pagesArray = new int[5];
        // Should display first 5 pages if on the first 3 pages
        if (currentPage <= 3) {
            pagesArray = new int[] {1, 2, 3, 4, 5};
        }
        // Should display last 5 pages if on the last 3 pages
        else if (currentPage >= numPages - 2) {
            for (int i = 1; i <= 5; i++) {
                pagesArray[i-1] = numPages - (5 - i);
            }
        }
        // Should display 5 pages centered around
        else {
            for (int i = 1; i <= 5; i++) {
                pagesArray[i-1] = currentPage - 3 + i;
            }
        }
        return pagesArray;
    }

    public void deleteShipment(List<Integer> shipmentIdList) {
        for (int shipmentId : shipmentIdList) {
            shipmentMapper.deleteShipment(shipmentId);
        }
    }

    public void updateShipment(Shipment updateShipment) {
        shipmentMapper.updateShipment(updateShipment);
    }
}

