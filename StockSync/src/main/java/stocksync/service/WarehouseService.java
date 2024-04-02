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
    public WarehouseService(WarehouseMapper whMapper){
        this.whMapper = whMapper;
    }
    public void createWarehouse(Warehouse newWh) {
        this.whMapper.insertWarehouse(newWh);
    }

    public List<Warehouse> getWarehouses(int page, String sortBy, String sortMethod) {
        int limit = 10;
        int offset = limit * (page - 1);
        // Get the right table column name for sortBy
        String sortByAsColumnName;
        switch (sortBy) {
            case "name": 
                sortByAsColumnName = "warehouse_name";
                break;
            case "address": 
                sortByAsColumnName = "warehouse_address";
                break;
            case "longitude": 
                sortByAsColumnName = "warehouse_long";
                break;
            case "latitude": 
                sortByAsColumnName = "warehouse_lat";
                break;
            default: 
                sortByAsColumnName = "warehouse_id";
                break;
        }
        // Set a default for sortMethod
        if (!sortMethod.equals("desc")) {
            sortMethod = "asc";
        }
        return whMapper.find(limit, offset, sortByAsColumnName, sortMethod);
    }

    public int getTotalNumEntries() {
        return this.whMapper.getTotalNumEntries();
    }
    
    // Get an array of 5 page numbers to display across the bottom
    // depending on what current page the user is on. The user's page
    // should be "centered" between the 5 options, unless there are no
    // other options in either direction.
    public int[] getPagesArray(int currentPage) {
        int numPages = (int) Math.ceil((double) this.getTotalNumEntries()/10);
        // Only display number of pages if there are less than 5 pages
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

    public void deleteWarehouse(Warehouse deleteWh) {
        this.whMapper.deleteWarehouse(deleteWh);
    }

    public void updateWarehouse(Warehouse updateWh) {
        this.whMapper.updateWarehouse(updateWh);
    }
}
