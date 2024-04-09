package stocksync.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import stocksync.model.Warehouse;
import stocksync.service.WarehouseService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;;
/**
 * Test class for controllers.
 */
@WebMvcTest(WarehouseController.class)
public class WarehouseControllerTests {
    /*
     * Set up test environment:
     *
     * mockMvc: a mvc used to send HTTP request for testing
     * mockService: a mock service class used for testing
     */
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WarehouseService mockService;

    public Warehouse setupWarehouse(){
        Warehouse testWH = new Warehouse();
        testWH.setWarehouseId(1);
        testWH.setWarehouseAddress("testAddress");
        testWH.setWarehouseLat(1);
        testWH.setWarehouseLong(1);
        testWH.setWarehouseName("testName");
        return testWH;
    }

    private Warehouse invalidLatWarehouse() {
        Warehouse testWH = new Warehouse();
        testWH.setWarehouseId(1);
        testWH.setWarehouseAddress("testAddress");
        testWH.setWarehouseLat(200);
        testWH.setWarehouseLong(1);
        testWH.setWarehouseName("testName");
        return testWH;
    }

    private Warehouse invalidLongWarehouse() {
        Warehouse testWH = new Warehouse();
        testWH.setWarehouseId(1);
        testWH.setWarehouseAddress("testAddress");
        testWH.setWarehouseLat(1);
        testWH.setWarehouseLong(-200);
        testWH.setWarehouseName("testName");
        return testWH;
    }
    /**
     * Test if the search endpoint return the correct template to render.
     * @throws Exception if the test failed
     */
    @Test
    public void searchTest() throws Exception {
        this.mockMvc.perform(get("/search"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("search"));
    }
    /**
     * Test if the deleteWarehouseButton method redirects to the correct URL after deletion.
     * @throws Exception if the test fails
     */
    @Test
    public void deleteWarehouseButtonRedirectTest() throws Exception {
        int testWarehouseId = 1;

        // Perform DELETE request and expect redirection
        mockMvc.perform(delete("/deleteWarehouse/{warehouseId}", testWarehouseId))
                .andExpect(status().is3xxRedirection()) // Expect a redirect status
                .andExpect(redirectedUrl("/warehouseSearchResults?page=1")); // Expect redirection to the specified URL

        // Verify that the service method was called with the correct warehouseId
        verify(mockService).deleteWarehouseButton(eq(testWarehouseId));
    }

    /**
     * Test if updateWarehouse redirects to the correct URL after updating
     * @throws Exception
     */
    @Test
    public void updateWarehouseRedirectTest() throws Exception {
        Warehouse testWarehouse = setupWarehouse();
        MockHttpServletRequestBuilder request = post("/updateWarehouse")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("warehouseId",String.valueOf(testWarehouse.getWarehouseId()))
                .param("warehouseName","newName")
                .param("warehouseAddress","newAddress")
                .param("warehouseLong","1.1")
                .param("warehouseLat","1.2");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection()) // Expect a redirect status
                .andExpect(redirectedUrl("/warehouseSearchResults?page=1")); // Expect redirection to the specified URL

    }
}