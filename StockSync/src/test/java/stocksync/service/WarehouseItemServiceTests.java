package stocksync.service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.extension.ExtendWith;
import stocksync.mapper.WarehouseItemMapper;
import stocksync.model.WarehouseItem;

import static org.mockito.BDDMockito.verify;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;


@ExtendWith(MockitoExtension.class)

/**
 * Test class for service
 */
public class WarehouseItemServiceTests {
    /*
     * Set up test environment:
     *
     * mockMapper: a mock mapper class used for testing
     *
     * mockWh: a mock warehouse item class used for testing
     *
     * otherWh: a mock warehouse item class that should not be invoked during testing
     *
     * inject mock mapper into service being tested
     *
     */
    @Mock
    private WarehouseItemMapper mockMapper;

    @InjectMocks
    private WarehouseItemService warehouseItemService;

    @Mock
    private WarehouseItem mockWarehouseItem;

    @Mock
    private WarehouseItem otherWarehouseItem;

    /**
     * Test if the createWarehouse method in the service calls the corresponding method
     * in the mapper with only the provided warehouse item parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testCreateWarehouseItem() throws Exception {
        warehouseItemService.createWarehouseItem(mockWarehouseItem);
        verify(mockMapper, never()).insertWarehouseItem(otherWarehouseItem);
        verify(mockMapper).insertWarehouseItem(mockWarehouseItem);
    }

    /**
     * Test get warehouse item from the service calls the corresponding
     * methods in the mapper based on a variety of parameters.
     * @throws Exception if the test failed
     */
    @Test
    public void testGetWarehouseItems() throws Exception {
        warehouseItemService.getWarehouseItems(1, "warehouseId", "desc", "warehouseId", "search term");
        warehouseItemService.getWarehouseItems(2, "itemId", "asc", "itemId", "search term");
        warehouseItemService.getWarehouseItems(3, "quantity", "asc", "", "");
        verify(mockMapper).findBySearch(10, 0, "warehouse_id", "desc", "warehouse_id", "%search term%");
        verify(mockMapper).findBySearch(10, 10, "item_id", "asc", "item_id", "%search term%");
        verify(mockMapper).findAll(10, 20, "quantity", "asc");
    }

    /**
     * Test if the delete warehouse item method in the service calls the corresponding method
     * in the mapper with only the provided warehouse item parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testDeleteWarehouseItem() throws Exception {
        ArrayList<Integer> warehouseItemIDs = new ArrayList<Integer>();
        warehouseItemIDs.add(mockWarehouseItem.getWarehouseItemId());
        warehouseItemService.deleteWarehouseItem(warehouseItemIDs);
        verify(mockMapper).deleteWarehouseItem(warehouseItemIDs.get(0));
    }

    /**
     * Test if the update warehouse item method in the service calls the corresponding method
     * in the mapper with only the provided warehouse item parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testUpdateWarehouseItem() throws Exception {
        warehouseItemService.updateWarehouseItem(mockWarehouseItem);
        verify(mockMapper, never()).updateWarehouseItem(otherWarehouseItem);
        verify(mockMapper).updateWarehouseItem(mockWarehouseItem);
    }

    /**
     * Test if the get total num entries returns the right number
     * @throws Exception if the test failed
     */
    @Test
    public void testGetTotalNumEntries() throws Exception {
        when(mockMapper.getTotalNumEntries()).thenReturn(55);
        assertEquals(warehouseItemService.getTotalNumEntries("", ""), 55);
    }

    /**
     * Test if the get total num entries with search params returns the right number
     * @throws Exception if the test failed
     */
    @Test
    public void testGetSearchNumEntries() throws Exception {
        when(mockMapper.getSearchNumEntries("ware_items_id", "1")).thenReturn(123);
        assertEquals(warehouseItemService.getTotalNumEntries("ware_items_id", "1"), 123);
    }


    /**
     * Test various scenarios of getting pages array depending on which page
     * the call is from and how many total entries are expected
     */
    @Test
    public void testGetPagesArrayFor40Entries() throws Exception {
        int[] pagesArray = new int[] {1, 2, 3, 4};
        assertArrayEquals(warehouseItemService.getPagesArray(1, 40), pagesArray);
    }

    @Test
    public void testGetPagesArrayOnFirstTwoPages() throws Exception {
        int[] pagesArray = new int[] {1, 2, 3, 4, 5};
        assertArrayEquals(warehouseItemService.getPagesArray(1, 100), pagesArray);
        assertArrayEquals(warehouseItemService.getPagesArray(2, 100), pagesArray);
    }

    @Test
    public void testGetPagesArrayOnLastTwoPages() throws Exception {
        int[] pagesArray = new int[] {6, 7, 8, 9, 10};
        assertArrayEquals(warehouseItemService.getPagesArray(10, 100), pagesArray);
        assertArrayEquals(warehouseItemService.getPagesArray(9, 100), pagesArray);
    }

    @Test
    public void testGetPagesArrayInMiddlePage() throws Exception {
        int[] pagesArray1 = new int[] {4, 5, 6, 7, 8};
        int[] pagesArray2 = new int[] {2, 3, 4, 5, 6};
        assertArrayEquals(warehouseItemService.getPagesArray(6, 100), pagesArray1);
        assertArrayEquals(warehouseItemService.getPagesArray(4, 100), pagesArray2);
    }
}