package stocksync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stocksync.mapper.WarehouseItemMapper;
import stocksync.model.WarehouseItem;
import stocksync.model.Warehouse;
import stocksync.model.Item;
import stocksync.model.ItemDetailsDTO;

import java.util.List;

@Service
public class WarehouseItemService {
    @Autowired
    private final WarehouseItemMapper warehouseItemMapper;

    public WarehouseItemService(WarehouseItemMapper mapper){
        this.warehouseItemMapper = mapper;
    }

    public void createWarehouseItem(WarehouseItem wi){
        // Check the total number of existing warehouse items
        int totalWarehouseItems = warehouseItemMapper.getTotalNumEntries();
        int maxWarehouseItems = 500;
        // If the maximum limit is reached, prevent adding the warehouse item
        if (totalWarehouseItems >= maxWarehouseItems) {
            throw new IllegalArgumentException("Maximum warehouse item capacity reached. Cannot add more warehouse items.");
        }
        // Otherwise, proceed with adding the warehouse item
        this.warehouseItemMapper.insertWarehouseItem(wi);
    }

    // Helper method to convert human-friendly attribute names to SQL query column names
    private String convertKeyToSqlColumn(String stringToConvert) {
        switch (stringToConvert) {
            case "warehouseId":
                return "warehouse_id";
            case "itemId":
                return "item_id";
            case "quantity":
                return "quantity";
            default:
                return "ware_item_id";
        }
    }

    /**
     * Method to get a paginated list of warehouse items based on sorting and search parameters
     * @param page is the current page to get
     * @param sortBy is the column/attribute that the request wants to results sorted by
     * @param sortMethod is the method, ascending or descending, to sort results by
     * @param searchKey is the column/attribute that the request wants to search by (origin/destination/id)
     * @param searchValue is the value that the request wants to search for
     * @return List of shipment objects
     */
    public List<WarehouseItem> getWarehouseItems(int page, String sortBy, String sortMethod, String searchKey, String searchValue) {
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
            return warehouseItemMapper.findAll(limit, offset, sortByAsColumnName, sortMethod);
        }

        // Get the right table column name for searchKey
        String searchKeyAsColumnName = convertKeyToSqlColumn(searchKey);

        // Convert searchValue to have search wildcard if the search is by name or address (we don't want wildcards for long/lat)
        String searchValueWithWildcard = searchValue;

        return warehouseItemMapper.findBySearch(limit, offset, sortByAsColumnName, sortMethod, searchKeyAsColumnName, searchValueWithWildcard);
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
            return warehouseItemMapper.getTotalNumEntries();
        }

        // Get the right table column name for searchKey
        String searchKeyAsColumnName = convertKeyToSqlColumn(searchKey);
        // Convert searchValue to have search wildcard if the search is by name or address (we don't want to wildcard for long/lat)
        String searchValueWithWildcard = (searchKey.equals("origin") || searchKey.equals("destination")) ? "%" + searchValue + "%" : searchValue;

        return warehouseItemMapper.getSearchNumEntries(searchKeyAsColumnName, searchValueWithWildcard);
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

    /**
     * Serice method for deleting warehouse
     * @param wiIdList list of warehouse ids to delete
     */
    public void deleteWarehouseItem(List<Integer> wiIdList) {
        for (int wiId : wiIdList) {
            warehouseItemMapper.deleteWarehouseItem(wiId);
        }
    }

    /**
     * Service method for updating warehouse item
     * @param updateWi instance of warehouse item
     */
    public void updateWarehouseItem(WarehouseItem updateWi) {
        warehouseItemMapper.updateWarehouseItem(updateWi);
    }

    /**
     * Find warehouse by specific id
     * @param warehouseId warehouse id
     * @return warehouse by id
     */
    public List<ItemDetailsDTO> getItemDetailsByWarehouseId(int warehouseId) {
        return warehouseItemMapper.findItemDetailsByWarehouseId(warehouseId);
    }

    
}

