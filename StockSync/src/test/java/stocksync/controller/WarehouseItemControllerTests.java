package stocksync.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import stocksync.DisabledSecurityConfig;
import stocksync.StockSyncApplication;
import stocksync.model.WarehouseItem;
import stocksync.service.WarehouseItemService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.test.context.ContextConfiguration;
import stocksync.StockSyncApplication;


/**
 * Test class for controllers.
 */
@WebMvcTest(WarehouseItemController.class)
@ContextConfiguration(classes = {StockSyncApplication.class, DisabledSecurityConfig.class})
@ActiveProfiles("test")

public class WarehouseItemControllerTests {
    /*
     * Set up test environment:
     *
     * mockMvc: a mvc used to send HTTP request for testing
     * mockService: a mock service class used for testing
     */
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WarehouseItemService mockService;
    //setting up a standard test warehouse object
    public WarehouseItem setupWarehouseItem(){
        WarehouseItem testWarehouseItem = new WarehouseItem();
        testWarehouseItem.setWarehouseItemId(10000);
        testWarehouseItem.setWarehouseId(20000);
        testWarehouseItem.setItemId(30000);
        testWarehouseItem.setQuantity(4);
        return testWarehouseItem;
    }

    /**
     * Test if the search endpoint return the correct template to render.
     * @throws Exception if the test failed
     */
    /*@Test
    public void searchTest() throws Exception {
        this.mockMvc.perform(get("/warehouseItem/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("warehouseItemSearch"));
    }*/

    /**
     * Test if the insertWarehouse endpoint return the correct template to render.
     * @throws Exception if the test failed
     */
    @Test
    public void insertWarehouseItemTest() throws Exception {
        WarehouseItem testWarehouseItem = setupWarehouseItem();
        MockHttpServletRequestBuilder request = post("/insertWarehouseItem")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("warehouseItemId",String.valueOf(testWarehouseItem.getWarehouseItemId()))
                .param("warehouseId",String.valueOf(testWarehouseItem.getWarehouseId()))
                .param("itemId",String.valueOf(testWarehouseItem.getItemId()))
                .param("quantity","5");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection()) // Expect a redirect status
                .andExpect(redirectedUrl("/warehouseItemSearchResults?page=1")); // Expect redirection to the specified URL
    }
    /**
     * Test if updateWarehouse redirects to the correct URL after updating
     * @throws Exception
     */
    @Test
    public void updateWarehouseItemTest() throws Exception {
        WarehouseItem testWarehouseItem = setupWarehouseItem();
        MockHttpServletRequestBuilder request = post("/warehouseItem/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("warehouseItemId",String.valueOf(testWarehouseItem.getWarehouseItemId()))
                .param("warehouseId",String.valueOf(testWarehouseItem.getWarehouseId()))
                .param("itemId",String.valueOf(testWarehouseItem.getItemId()))
                .param("quantity","6");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection()) // Expect a redirect status
                .andExpect(redirectedUrl("/warehouseItemSearchResults?page=1")); // Expect redirection to the specified URL

    }
}
