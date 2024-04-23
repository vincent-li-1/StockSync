package stocksync.service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.extension.ExtendWith;
import stocksync.mapper.ShipmentMapper;
import stocksync.model.Shipment;

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
public class ShipmentServiceTests {
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
    private ShipmentMapper mockMapper;

    @InjectMocks
    private ShipmentService shipmentService;

    @Mock
    private Shipment mockShipment;

    @Mock
    private Shipment otherShipment;

    /**
     * Test if the createWarehouse method in the service calls the corresponding method
     * in the mapper with only the provided warehouse parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testCreateShipment() throws Exception {
        shipmentService.createShipment(mockShipment);
        verify(mockMapper, never()).insertShipment(otherShipment);
        verify(mockMapper).insertShipment(mockShipment);
    }

    /**
     * Test get warehouse from the service calls the corresponding
     * methods in the mapper based on a variety of parameters.
     * @throws Exception if the test failed
     */
    @Test
    public void testGetShipments() throws Exception {
        shipmentService.getShipments(1, "origin", "desc", "origin", "search term");
        shipmentService.getShipments(2, "destination", "asc", "destination", "search term");
        shipmentService.getShipments(3, "id", "someOther", "", "");
        verify(mockMapper).findBySearch(10, 0, "warehouse_from_id", "desc", "warehouse_from_id", "%search term%");
        verify(mockMapper).findBySearch(10, 10, "warehouse_to_id", "asc", "warehouse_to_id", "%search term%");
        verify(mockMapper).findAll(10, 20, "shipment_id", "asc");
    }

    /**
     * Test if the delete warehouse method in the service calls the corresponding method
     * in the mapper with only the provided warehouse parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testDeleteShipment() throws Exception {
        ArrayList<Integer> shipmentIDs = new ArrayList<Integer>();
        shipmentIDs.add(mockShipment.getShipmentId());
        shipmentService.deleteShipment(shipmentIDs);
        verify(mockMapper).deleteShipment(shipmentIDs.get(0));
    }

    /**
     * Test if the update warehouse method in the service calls the corresponding method
     * in the mapper with only the provided warehouse parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testUpdateShipment() throws Exception {
        shipmentService.updateShipment(mockShipment);
        verify(mockMapper, never()).updateShipment(otherShipment);
        verify(mockMapper).updateShipment(mockShipment);
    }

    /**
     * Test if the get total num entries returns the right number
     * @throws Exception if the test failed
     */
    @Test
    public void testGetTotalNumEntries() throws Exception {
        when(mockMapper.getTotalNumEntries()).thenReturn(55);
        assertEquals(shipmentService.getTotalNumEntries("", ""), 55);
    }

    /**
     * Test if the get total num entries with search params returns the right number
     * @throws Exception if the test failed
     */
    @Test
    public void testGetSearchNumEntries() throws Exception {
        when(mockMapper.getSearchNumEntries("shipment_id", "1")).thenReturn(123);
        assertEquals(shipmentService.getTotalNumEntries("shipment_id", "1"), 123);
    }


    /**
     * Test various scenarios of getting pages array depending on which page
     * the call is from and how many total entries are expected
     */
    @Test
    public void testGetPagesArrayFor40Entries() throws Exception {
        int[] pagesArray = new int[] {1, 2, 3, 4};
        assertArrayEquals(shipmentService.getPagesArray(1, 40), pagesArray);
    }

    @Test
    public void testGetPagesArrayOnFirstTwoPages() throws Exception {
        int[] pagesArray = new int[] {1, 2, 3, 4, 5};
        assertArrayEquals(shipmentService.getPagesArray(1, 100), pagesArray);
        assertArrayEquals(shipmentService.getPagesArray(2, 100), pagesArray);
    }

    @Test
    public void testGetPagesArrayOnLastTwoPages() throws Exception {
        int[] pagesArray = new int[] {6, 7, 8, 9, 10};
        assertArrayEquals(shipmentService.getPagesArray(10, 100), pagesArray);
        assertArrayEquals(shipmentService.getPagesArray(9, 100), pagesArray);
    }

    @Test
    public void testGetPagesArrayInMiddlePage() throws Exception {
        int[] pagesArray1 = new int[] {4, 5, 6, 7, 8};
        int[] pagesArray2 = new int[] {2, 3, 4, 5, 6};
        assertArrayEquals(shipmentService.getPagesArray(6, 100), pagesArray1);
        assertArrayEquals(shipmentService.getPagesArray(4, 100), pagesArray2);
    }
}