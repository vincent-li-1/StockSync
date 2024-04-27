package stocksync.service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.bind.annotation.RequestBody;
import stocksync.mapper.ShipmentMapper;
import stocksync.mapper.WarehouseItemMapper;
import stocksync.model.Shipment;

import static org.mockito.BDDMockito.verify;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import stocksync.model.ShipmentRequest;

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
    private ShipmentMapper shipmentMapper;

    @Mock
    private WarehouseItemMapper warehouseItemMapper;


    @InjectMocks
    private ShipmentService shipmentService;

    @Mock
    private Shipment mockShipment;

    @Mock
    private Shipment otherShipment;


    @Test
    public void testNewCreateShipment() throws Exception {
        ShipmentRequest testBody = new ShipmentRequest();
        ArrayList<Integer> idList = new ArrayList<Integer>();
        idList.add(100);
        idList.add(101);
        ArrayList<Integer> quantityList = new ArrayList<Integer>();
        quantityList.add(5);
        quantityList.add(5);

        testBody.setShipmentId(100);
        testBody.setItemIdList(idList);
        testBody.setItemQuantityList(quantityList);
        testBody.setWarehouseFromId(10000);
        testBody.setWarehouseToId(10001);

        shipmentService.newCreateShipment(testBody);
        verify(shipmentMapper).insertShipment(any());
        verify(warehouseItemMapper).subtractQuantity(5,10000,100);
        verify(warehouseItemMapper).subtractQuantity(5,10000,101);
        verify(warehouseItemMapper,atLeastOnce()).hasItem(10001,100);
        verify(warehouseItemMapper,atLeastOnce()).hasItem(10001,101);
        verify(warehouseItemMapper).addQuantity(5,10001,100);
        verify(warehouseItemMapper).addQuantity(5,10001,101);
    }

    /**
     * Test if the createWarehouse method in the service calls the corresponding method
     * in the mapper with only the provided warehouse parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testCreateShipment() throws Exception {
        shipmentService.createShipment(mockShipment);
        verify(shipmentMapper, never()).insertShipment(otherShipment);
        verify(shipmentMapper).insertShipment(mockShipment);
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
        verify(shipmentMapper).findBySearch(10, 0, "warehouse_from_id", "desc", "warehouse_from_id", "%search term%");
        verify(shipmentMapper).findBySearch(10, 10, "warehouse_to_id", "asc", "warehouse_to_id", "%search term%");
        verify(shipmentMapper).findAll(10, 20, "shipment_id", "asc");
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
        verify(shipmentMapper).deleteShipment(shipmentIDs.get(0));
    }

    /**
     * Test if the update warehouse method in the service calls the corresponding method
     * in the mapper with only the provided warehouse parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testUpdateShipment() throws Exception {
        shipmentService.updateShipment(mockShipment);
        verify(shipmentMapper, never()).updateShipment(otherShipment);
        verify(shipmentMapper).updateShipment(mockShipment);
    }

    /**
     * Test if the get total num entries returns the right number
     * @throws Exception if the test failed
     */
    @Test
    public void testGetTotalNumEntries() throws Exception {
        when(shipmentMapper.getTotalNumEntries()).thenReturn(55);
        assertEquals(shipmentService.getTotalNumEntries("", ""), 55);
    }

    /**
     * Test if the get total num entries with search params returns the right number
     * @throws Exception if the test failed
     */
    @Test
    public void testGetSearchNumEntries() throws Exception {
        when(shipmentMapper.getSearchNumEntries("shipment_id", "1")).thenReturn(123);
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