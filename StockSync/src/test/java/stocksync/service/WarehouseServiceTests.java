package stocksync.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;;

@ExtendWith(MockitoExtension.class)

/**
 * Test class for service
 */
public class WarehouseServiceTests {
    /*
     * Set up test environment:
     *
     * mockMapper: a mock mapper class used for testing
     */
    @Mock
    private WarehouseMapper mockMapper;
    
    @InjectMocks
    private WarehouseService warehouseService;

    @Mock
    private Warehouse mockWh;

    @Mock
    private Warehouse otherWh;

    @Test
    public void testCreateWarehouse() throws Exception {
        warehouseService.createWarehouse(mockWh);
        verify(mockMapper, never()).insertWarehouse(otherWh);
        verify(mockMapper).insertWarehouse(mockWh);
    }

    @Test
    public void testGetWarehouses() throws Exception {
        warehouseService.getWarehouses(1, "name", "desc");
        warehouseService.getWarehouses(2, "longitude", "asc");
        warehouseService.getWarehouses(3, "someOther", "someOther");
        verify(mockMapper).find(10, 0, "warehouse_name", "desc");
        verify(mockMapper).find(10, 10, "warehouse_long", "asc");
        verify(mockMapper).find(10, 20, "warehouse_id", "asc");
    }

    @Test
    public void testDeleteWarehouse() throws Exception {
        warehouseService.deleteWarehouse(mockWh);
        verify(mockMapper, never()).deleteWarehouse(otherWh);
        verify(mockMapper).deleteWarehouse(mockWh);
    }

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
        assertEquals(warehouseService.getTotalNumEntries(), 55);
    }

    @Test
    public void testGetPagesArrayFor40Entries() throws Exception {
        when(mockMapper.getTotalNumEntries()).thenReturn(40);
        int[] pagesArray = new int[] {1, 2, 3, 4};
        assertArrayEquals(warehouseService.getPagesArray(1), pagesArray);
    }

    @Test
    public void testGetPagesArrayOnFirstTwoPages() throws Exception {
        when(mockMapper.getTotalNumEntries()).thenReturn(100);
        int[] pagesArray = new int[] {1, 2, 3, 4, 5};
        assertArrayEquals(warehouseService.getPagesArray(1), pagesArray);
        assertArrayEquals(warehouseService.getPagesArray(2), pagesArray);
    }

    @Test
    public void testGetPagesArrayOnLastTwoPages() throws Exception {
        when(mockMapper.getTotalNumEntries()).thenReturn(100);
        int[] pagesArray = new int[] {6, 7, 8, 9, 10};
        assertArrayEquals(warehouseService.getPagesArray(10), pagesArray);
        assertArrayEquals(warehouseService.getPagesArray(9), pagesArray);
    }

    @Test
        public void testGetPagesArrayInMiddlePage() throws Exception {
            when(mockMapper.getTotalNumEntries()).thenReturn(100);
            int[] pagesArray1 = new int[] {4, 5, 6, 7, 8};
            int[] pagesArray2 = new int[] {2, 3, 4, 5, 6};
            assertArrayEquals(warehouseService.getPagesArray(6), pagesArray1);
            assertArrayEquals(warehouseService.getPagesArray(4), pagesArray2);
        }
}
