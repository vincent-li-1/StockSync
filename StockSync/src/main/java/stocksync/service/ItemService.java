package stocksync.service;

import java.util.List;
import stocksync.model.Item;
import stocksync.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService implements IItemService {
    @Autowired
    private final ItemMapper itMapper;
    public ItemService(ItemMapper itMapper){
        this.itMapper = itMapper;
    }
    public void createItem(Item newIt) {
        // Check the total number of existing items
        int totalItems = itMapper.getTotalNumEntries();
        int maxItems = 500;
        // If the maximum limit is reached, prevent adding the item
        if (totalItems >= maxItems) {
            throw new IllegalArgumentException("Maximum item capacity reached. Cannot add more items.");
        }
        // Otherwise, proceed with adding the item
        this.itMapper.insertItem(newIt);
    }

    // Helper method to convert human-friendly attribute names to SQL query column names
    private String convertKeyToSqlColumn(String stringToConvert) {
        switch (stringToConvert) {
            case "name":
                return "item_name";
            case "size":
                return "item_size";
            case "price":
                return "item_price";
            default:
                return "item_id";
        }
    }
    /**
     * Method to get a paginated list of items based on sorting and search parameters
     * @param page is the current page to get
     * @param sortBy is the column/attribute that the request wants to results sorted by
     * @param sortMethod is the method, ascending or descending, to sort results by
     * @param searchKey is the column/attribute that the request wants to search by
     * @param searchValue is the value that the request wants to search for
     * @return List of item objects
     */
    public List<Item> getItems(int page, String sortBy, String sortMethod, String searchKey, String searchValue) {
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
            return itMapper.findAll(limit, offset, sortByAsColumnName, sortMethod);
        }

        // Get the right table column name for searchKey
        String searchKeyAsColumnName = convertKeyToSqlColumn(searchKey);


        // Convert searchValue to have search wildcard if the search is by name (we don't want wildcards for size or price)
        String searchValueWithWildcard = (searchKey.equals("name") ? "%" + searchValue + "%" : searchValue);

        return itMapper.findBySearch(limit, offset, sortByAsColumnName, sortMethod, searchKeyAsColumnName, searchValueWithWildcard);
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
            return itMapper.getTotalNumEntries();
        }

        // Get the right table column name for searchKey
        String searchKeyAsColumnName = convertKeyToSqlColumn(searchKey);

         // Convert searchValue to have search wildcard if the search is by name or address (we don't want to wildcard for long/lat)
        String searchValueWithWildcard = (searchKey.equals("name") || searchKey.equals("size") || searchKey.equals("price")) ? "%" + searchValue + "%" : searchValue;

        return itMapper.getSearchNumEntries(searchKeyAsColumnName, searchValueWithWildcard);
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
            numEntries = itMapper.getTotalNumEntries();
        } 
        else {
             // Get the right table column name for searchKey
            String searchKeyAsColumnName = convertKeyToSqlColumn(searchKey);
            // TODO: Check that searchKeyAsColumnName is not id, if it is throw error

             // Convert searchValue to have search wildcard if the search is by name or address (we don't want to wildcard for long/lat)
            String searchValueWithWildcard = searchKey.equals("name") ? "%" + searchValue + "%" : searchValue;

            numEntries = itMapper.getSearchNumEntries(searchKeyAsColumnName, searchValueWithWildcard);
        }
        return numEntries/10 + 1;
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

    public void deleteItem(Item deleteIt) {
        this.itMapper.deleteItem(deleteIt);
    }

    public void updateItem(Item updateIt) {
        this.itMapper.updateItem(updateIt);
    }
}
