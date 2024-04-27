package stocksync.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.*;
import stocksync.service.WarehouseService;
import stocksync.mapper.WarehouseMapper;
import stocksync.model.Warehouse;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)

/**
 * Test class for service
 */
public class WarehouseServiceTests {
    /*
     * Set up test environment:
     *
     * mockMapper: a mock mapper class used for testing
     * 
     * mockWh: a mock warehouse class used for testing
     * 
     * otherWh: a mock warehouse class that should not be invoked during testing
     * 
     * inject mock mapper into service being tested
     * 
     */
    @Mock
    private WarehouseMapper mockMapper;
    
    @InjectMocks
    private WarehouseService warehouseService;

    @Mock
    private Warehouse mockWh;

    @Mock
    private Warehouse otherWh;

    /**
     * Test if the createWarehouse method in the service calls the corresponding method 
     * in the mapper with only the provided warehouse parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testCreateWarehouse() throws Exception {
        warehouseService.createWarehouse(mockWh);
        verify(mockMapper, never()).insertWarehouse(otherWh);
        verify(mockMapper).insertWarehouse(mockWh);
    }

    /**
     * Test get warehouse from the service calls the corresponding
     * methods in the mapper based on a variety of parameters.
     * @throws Exception if the test failed
     */
    @Test
    public void testGetWarehouses() throws Exception {
        warehouseService.getWarehouses(1, "name", "desc", "address", "search term");
        warehouseService.getWarehouses(2, "longitude", "asc", "longitude", "search longitude");
        warehouseService.getWarehouses(3, "someOther", "someOther", "", "");
        verify(mockMapper).findBySearch(10, 0, "warehouse_name", "desc", "warehouse_address", "%search term%");
        verify(mockMapper).findBySearch(10, 10, "warehouse_long", "asc", "warehouse_long", "search longitude");
        verify(mockMapper).findAll(10, 20, "warehouse_id", "asc");
    }

    // /**
    //  * Test if the delete warehouse method in the service calls the corresponding method 
    //  * in the mapper with only the provided warehouse parameter.
    //  * @throws Exception if the test failed
    //  */
    // @Test
    // public void testDeleteWarehouse() throws Exception {
    //     warehouseService.deleteWarehouse(mockWh);
    //     verify(mockMapper, never()).deleteWarehouse(otherWh);
    //     verify(mockMapper).deleteWarehouse(mockWh);
    // }

    /**
     * Test if the update warehouse method in the service calls the corresponding method 
     * in the mapper with only the provided warehouse parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testUpdateWarehouse() throws Exception {
        warehouseService.updateWarehouse(mockWh);
        verify(mockMapper, never()).updateWarehouse(otherWh);
        verify(mockMapper).updateWarehouse(mockWh);
    }

    /**
     * Test if the get total num entries returns the right number
     * @throws Exception if the test failed
     */
    @Test
    public void testGetTotalNumEntries() throws Exception {
        when(mockMapper.getTotalNumEntries()).thenReturn(55);
        assertEquals(warehouseService.getTotalNumEntries("", ""), 55);
    }

    /**
     * Test if the get total num entries with search params returns the right number
     * @throws Exception if the test failed
     */
    @Test
    public void testGetSearchNumEntries() throws Exception {
        when(mockMapper.getSearchNumEntries("warehouse_address", "%test%")).thenReturn(123);
        assertEquals(warehouseService.getTotalNumEntries("address", "test"), 123);
    }


    /**
     * Test various scenarios of getting pages array depending on which page
     * the call is from and how many total entries are expected
     */
    @Test
    public void testGetPagesArrayFor40Entries() throws Exception {
        int[] pagesArray = new int[] {1, 2, 3, 4};
        assertArrayEquals(warehouseService.getPagesArray(1, 40), pagesArray);
    }

    @Test
    public void testGetPagesArrayOnFirstTwoPages() throws Exception {
        int[] pagesArray = new int[] {1, 2, 3, 4, 5};
        assertArrayEquals(warehouseService.getPagesArray(1, 100), pagesArray);
        assertArrayEquals(warehouseService.getPagesArray(2, 100), pagesArray);
    }

    @Test
    public void testGetPagesArrayOnLastTwoPages() throws Exception {
        int[] pagesArray = new int[] {6, 7, 8, 9, 10};
        assertArrayEquals(warehouseService.getPagesArray(10, 100), pagesArray);
        assertArrayEquals(warehouseService.getPagesArray(9, 100), pagesArray);
    }

    @Test
    public void testGetPagesArrayInMiddlePage() throws Exception {
        int[] pagesArray1 = new int[] {4, 5, 6, 7, 8};
        int[] pagesArray2 = new int[] {2, 3, 4, 5, 6};
        assertArrayEquals(warehouseService.getPagesArray(6, 100), pagesArray1);
        assertArrayEquals(warehouseService.getPagesArray(4, 100), pagesArray2);
    }


    /**
     * Test if getting the number of total pages with and without search terms returns the expected number
     * @throws Exception if the test failed
     */
    @Test
    public void testGetTotalNumPages() throws Exception {
        when(mockMapper.getTotalNumEntries()).thenReturn(55);
        assertEquals(warehouseService.getTotalNumPages("",""), 6);
    }

    @Test
    public void testGetTotalNumPagesWithSearch() throws Exception {
        when(mockMapper.getSearchNumEntries("warehouse_address", "%test%")).thenReturn(123);
        assertEquals(warehouseService.getTotalNumPages("address", "test"), 13);
    }

    /**
     * Test if creating a warehouse with an out of bounds longitude throws the expected error
     * @throws Exception if the test failed
     */
    @Test
    public void invalidLongitudeCreateWarehouse() throws Exception {
        when(mockWh.getWarehouseLong()).thenReturn(181.0);
        when(otherWh.getWarehouseLong()).thenReturn(-200.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            warehouseService.createWarehouse(mockWh);
        });
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            warehouseService.createWarehouse(otherWh);
        });
        String expectedMessage = "Longitude out of bounds. Must be between -180 and 180";
        assertTrue(exception.getMessage().contains(expectedMessage));
        assertTrue(exception2.getMessage().contains(expectedMessage));
    }

    /**
     * Test if creating a warehouse with an out of bounds latitude throws the expected error
     * @throws Exception if the test failed
     */
    @Test
    public void invalidLatitudeCreateWarehouse() throws Exception {
        when(mockWh.getWarehouseLat()).thenReturn(181.0);
        when(otherWh.getWarehouseLat()).thenReturn(-200.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            warehouseService.createWarehouse(mockWh);
        });
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            warehouseService.createWarehouse(otherWh);
        });
        String expectedMessage = "Latitude out of bounds. Must be between -180 and 180";
        assertTrue(exception.getMessage().contains(expectedMessage));
        assertTrue(exception2.getMessage().contains(expectedMessage));
    }

    /**
     * Test if updating a warehouse with an out of bounds longitude throws the expected error
     * @throws Exception if the test failed
     */
    @Test
    public void invalidLongitudeUpdateWarehouse() throws Exception {
        when(mockWh.getWarehouseLong()).thenReturn(181.0);
        when(otherWh.getWarehouseLong()).thenReturn(-200.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            warehouseService.updateWarehouse(mockWh);
        });
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            warehouseService.updateWarehouse(otherWh);
        });
        String expectedMessage = "Longitude out of bounds. Must be between -180 and 180";
        assertTrue(exception.getMessage().contains(expectedMessage));
        assertTrue(exception2.getMessage().contains(expectedMessage));
    }

    /**
     * Test if updating a warehouse with an out of bounds latitude throws the expected error
     * @throws Exception if the test failed
     */
    @Test
    public void invalidLatitudeUpdateWarehouse() throws Exception {
        when(mockWh.getWarehouseLat()).thenReturn(181.0);
        when(otherWh.getWarehouseLat()).thenReturn(-200.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            warehouseService.updateWarehouse(mockWh);
        });
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            warehouseService.updateWarehouse(otherWh);
        });
        String expectedMessage = "Latitude out of bounds. Must be between -180 and 180";
        assertTrue(exception.getMessage().contains(expectedMessage));
        assertTrue(exception2.getMessage().contains(expectedMessage));
    }
}

