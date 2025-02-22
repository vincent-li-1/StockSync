package stocksync.service;

import java.util.List;
import stocksync.model.Warehouse;
import stocksync.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseService implements IWarehouseService {
    @Autowired
    private final WarehouseMapper whMapper;

    /**
     * Constructor maps mapper to the service (handled by Spring Boot)
     * @param whMapper mapper for the service
     */
    public WarehouseService(WarehouseMapper whMapper){
        this.whMapper = whMapper;
    }

    /**
     * Method to create a new warehouse in the database
     * @param newWh the new warehouse to create. Will be validated in this method
     */
    public void createWarehouse(Warehouse newWh) throws IllegalArgumentException {
        // Check the total number of existing warehouses
        int totalWarehouses = whMapper.getTotalNumEntries();
        int maxWarehouses = 500;
        // If the maximum limit is reached, prevent adding the warehouse
        if (totalWarehouses >= maxWarehouses) {
            throw new IllegalArgumentException("Maximum warehouse capacity reached. Cannot add more warehouses.");
        }
        // Otherwise, proceed with adding the warehouse
        if (Math.abs(newWh.getWarehouseLong()) > 180) {
            throw new IllegalArgumentException("Longitude out of bounds. Must be between -180 and 180");
        }
        if (Math.abs(newWh.getWarehouseLat()) > 180) {
            throw new IllegalArgumentException("Latitude out of bounds. Must be between -180 and 180");
        }
        this.whMapper.insertWarehouse(newWh);
    }

    /*
     * Method for getting a singular warehouse given the id
     * @param id is the id of the warehouse to get
     * @return the warehouse that matches given id
     */
    public Warehouse getWarehouseById(int warehouseId) {
        Warehouse warehouse = whMapper.findWarehouseById(warehouseId);
        // Make sure the warehouse exists
        if (warehouse == null) {
            throw new IllegalArgumentException("No warehouse found with ID: " + warehouseId);
        }
        return warehouse;
    }

    /**
     * Helper method to convert a human-friendly attribute name to SQL query column name
     * @param stringToConvert the string to convert
     */
    private String convertKeyToSqlColumn(String stringToConvert) {
        switch (stringToConvert) {
            case "name": 
                return "warehouse_name";
            case "address": 
                return "warehouse_address";
            case "longitude": 
                return "warehouse_long";
            case "latitude": 
                return "warehouse_lat";
            default: 
                return "warehouse_id";
        }
    }
    /**
     * Method to get a paginated list of warehouses based on sorting and search parameters
     * @param page is the current page to get
     * @param sortBy is the column/attribute that the request wants to results sorted by
     * @param sortMethod is the method, ascending or descending, to sort results by
     * @param searchKey is the column/attribute that the request wants to search by
     * @param searchValue is the value that the request wants to search for
     * @return List of warehouse objects
     */
    public List<Warehouse> getWarehouses(int page, String sortBy, String sortMethod, String searchKey, String searchValue) {
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
        if (searchKey.equals("") || searchValue.equals("")) {
            return whMapper.findAll(limit, offset, sortByAsColumnName, sortMethod);
        }

        // Get the right table column name for searchKey
        String searchKeyAsColumnName = convertKeyToSqlColumn(searchKey);

        // Convert searchValue to have search wildcard if the search is by name or address (we don't want wildcards for long/lat)
        String searchValueWithWildcard = (searchKey.equals("name") || searchKey.equals("address")) ? "%" + searchValue + "%" : searchValue;

        return whMapper.findBySearch(limit, offset, sortByAsColumnName, sortMethod, searchKeyAsColumnName, searchValueWithWildcard);
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
            return whMapper.getTotalNumEntries();
        }

        // Get the right table column name for searchKey
        String searchKeyAsColumnName = convertKeyToSqlColumn(searchKey);
         // Convert searchValue to have search wildcard if the search is by name or address (we don't want to wildcard for long/lat)
        String searchValueWithWildcard = (searchKey.equals("name") || searchKey.equals("address")) ? "%" + searchValue + "%" : searchValue;
        
        return whMapper.getSearchNumEntries(searchKeyAsColumnName, searchValueWithWildcard);
    }

    /**
     * Method to get a count of the total number of pages for a get request
     * @param searchKey is the column/attribute that the get is searched by
     * @param searchValue is the value that the get searched for
     * @return number of pages based on the parameters (if any)
     */
    public int getTotalNumPages(String searchKey, String searchValue) {
        int numEntries;
        if (searchKey.equals("") || searchValue.equals("")) {
            numEntries = whMapper.getTotalNumEntries();
        } 
        else {
             // Get the right table column name for searchKey
            String searchKeyAsColumnName = convertKeyToSqlColumn(searchKey);
             // Convert searchValue to have search wildcard if the search is by name or address (we don't want to wildcard for long/lat)
            String searchValueWithWildcard = (searchKey.equals("name") || searchKey.equals("address")) ? "%" + searchValue + "%" : searchValue;

            numEntries = whMapper.getSearchNumEntries(searchKeyAsColumnName, searchValueWithWildcard);
        }
        double numEntriesDouble = numEntries;
        return (int) Math.ceil(numEntriesDouble/10.0);
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
     * Method to delete one warehouse from the database
     * @param deleteWh the warehouse to delete from the database
     */
    public void deleteWarehouse(Warehouse deleteWh) {
        whMapper.deleteWarehouse(deleteWh);
    }

    /**
     * Method to delete multiple warehouses from the database
     * @param warehouseIdList the list of warehouses to delete from the database
     */
    public void deleteWarehouseButton(List<Integer> warehouseIdList) {
        for (int warehouseId : warehouseIdList) {
            whMapper.deleteWarehouseButton(warehouseId);
        }
    }


    /**
     * Method to update a warehouse in the database
     * @param updateWh the warehouse to update and the information to update with
     */
    public void updateWarehouse(Warehouse updateWh) throws IllegalArgumentException {
        if (Math.abs(updateWh.getWarehouseLong()) > 180) {
            throw new IllegalArgumentException("Longitude out of bounds. Must be between -180 and 180");
        }
        if (Math.abs(updateWh.getWarehouseLat()) > 180) {
            throw new IllegalArgumentException("Latitude out of bounds. Must be between -180 and 180");
        }
        try {
            whMapper.updateWarehouse(updateWh);
        } catch(Exception e) {
            throw e;
        }
    }
}
