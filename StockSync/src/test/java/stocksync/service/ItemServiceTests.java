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
import stocksync.service.ItemService;
import stocksync.mapper.ItemMapper;
import stocksync.model.Item;

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
public class ItemServiceTests {
    /*
     * Set up test environment:
     *
     * mockMapper: a mock mapper class used for testing
     *
     * mockIt: a mock item class used for testing
     *
     * otherIt: a mock item class that should not be invoked during testing
     *
     * inject mock mapper into service being tested
     *
     */
    @Mock
    private ItemMapper mockMapper;

    @InjectMocks
    private ItemService itemService;

    @Mock
    private Item mockIt;

    @Mock
    private Item otherIt;

    /**
     * Test if the createItem method in the service calls the corresponding method
     * in the mapper with only the provided warehouse parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testCreateItem() throws Exception {
        itemService.createItem(mockIt);
        verify(mockMapper, never()).insertItem(otherIt);
        verify(mockMapper).insertItem(mockIt);
    }

    /**
     * Test get item from the service calls the corresponding
     * methods in the mapper based on a variety of parameters.
     * @throws Exception if the test failed
     */
    @Test
    public void testGetItems() throws Exception {
        itemService.getItems(1, "name", "desc", "size", "search term");
        itemService.getItems(2, "price", "asc", "price", "search longitude");
        verify(mockMapper).findBySearch(10, 0, "item_name", "desc", "item_name", "%search name");
        verify(mockMapper).findBySearch(10, 10, "item_price", "asc", "item_price", "search price");
        verify(mockMapper).findAll(10, 20, "item_id", "asc");
    }

    /**
     * Test if the delete item method in the service calls the corresponding method
     * in the mapper with only the provided item parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testDeleteItem() throws Exception {
        itemService.deleteItem(mockIt);
        verify(mockMapper, never()).deleteItem(otherIt);
        verify(mockMapper).deleteItem(mockIt);
    }

    /**
     * Test if the update item method in the service calls the corresponding method
     * in the mapper with only the provided item parameter.
     * @throws Exception if the test failed
     */
    @Test
    public void testUpdateWarehouse() throws Exception {
        itemService.updateItem(mockIt);
        verify(mockMapper, never()).updateItem(otherIt);
        verify(mockMapper).updateItem(mockIt);
    }

    /**
     * Test if the get total num entries returns the right number
     * @throws Exception if the test failed
     */
    @Test
    public void testGetTotalNumEntries() throws Exception {
        when(mockMapper.getTotalNumEntries()).thenReturn(55);
        assertEquals(itemService.getTotalNumEntries("", ""), 55);
    }

    /**
     * Test if the get total num entries with search params returns the right number
     * @throws Exception if the test failed
     */
    @Test
    public void testGetSearchNumEntries() throws Exception {
        when(mockMapper.getSearchNumEntries("item_price", "%test%")).thenReturn(123);
        assertEquals(itemService.getTotalNumEntries("price", "test"), 123);
    }


    /**
     * Test various scenarios of getting pages array depending on which page
     * the call is from and how many total entries are expected
     */
    @Test
    public void testGetPagesArrayFor40Entries() throws Exception {
        int[] pagesArray = new int[] {1, 2, 3, 4};
        assertArrayEquals(itemService.getPagesArray(1, 40), pagesArray);
    }

    @Test
    public void testGetPagesArrayOnFirstTwoPages() throws Exception {
        int[] pagesArray = new int[] {1, 2, 3, 4, 5};
        assertArrayEquals(itemService.getPagesArray(1, 100), pagesArray);
        assertArrayEquals(itemService.getPagesArray(2, 100), pagesArray);
    }

    @Test
    public void testGetPagesArrayOnLastTwoPages() throws Exception {
        int[] pagesArray = new int[] {6, 7, 8, 9, 10};
        assertArrayEquals(itemService.getPagesArray(10, 100), pagesArray);
        assertArrayEquals(itemService.getPagesArray(9, 100), pagesArray);
    }

    @Test
        public void testGetPagesArrayInMiddlePage() throws Exception {
            int[] pagesArray1 = new int[] {4, 5, 6, 7, 8};
            int[] pagesArray2 = new int[] {2, 3, 4, 5, 6};
            assertArrayEquals(itemService.getPagesArray(6, 100), pagesArray1);
            assertArrayEquals(itemService.getPagesArray(4, 100), pagesArray2);
        }
}
