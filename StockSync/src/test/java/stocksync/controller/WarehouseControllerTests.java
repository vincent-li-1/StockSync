package stocksync.controller;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import stocksync.DisabledSecurityConfig;
import stocksync.StockSyncApplication;
import stocksync.model.Warehouse;
import stocksync.service.WarehouseService;
import stocksync.model.WarehouseItem;
import stocksync.service.WarehouseItemService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import stocksync.service.WarehouseService;
import stocksync.controller.WarehouseController;

import org.junit.jupiter.api.Test;
/**
 * Test class for controllers.
 */
@WebMvcTest(WarehouseController.class)
@ContextConfiguration(classes = {StockSyncApplication.class, DisabledSecurityConfig.class})
@ActiveProfiles("test")
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
    @MockBean
    private WarehouseItemService warehouseItemService;
    
    //setting up a standard test warehouse object
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
    @WithUserDetails(value = "USER")
    public void searchTest() throws Exception {
        this.mockMvc.perform(get("/search"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("search"));
    }
//     /**
//      * Test if the deleteWarehouseButton method redirects to the correct URL after deletion.
//      * @throws Exception if the test fails
//      */
//     @Test
//     public void deleteWarehouseButtonRedirectTest() throws Exception {
//         int testWarehouseId = 1;
// // Perform DELETE request and expect redirection
//         mockMvc.perform(delete("/deleteWarehouse/{warehouseId}", testWarehouseId))
//                 .andExpect(status().is3xxRedirection()) // Expect a redirect status
//                 .andExpect(redirectedUrl("/warehouseSearchResults?page=1")); // Expect redirection to the specified URL

//         // Verify that the service method was called with the correct warehouseId
//         verify(mockService).deleteWarehouseButton(eq(testWarehouseId));
//     }
//      @Test
//     public void testGetAllWarehouses() throws Exception{
//         // Mocking service method response
//         when(mockService.getTotalNumEntries(anyString(), anyString())).thenReturn(1);

//         // Assume getWarehouses returns an empty list
//         when(mockService.getWarehouses(anyInt(), anyString(), anyString(), anyString(), anyString())).thenReturn(List.of());

//         mockMvc.perform(get("/warehouseSearchResults").param("page", "1"))
//                 .andExpect(status().isOk())
//                 .andExpect(view().name("warehouseSearchResults"));
          
        
//         // Verify interactionsc
//         verify(mockService, times(1)).getTotalNumEntries(anyString(), anyString());
//         verify(mockService, times(1)).getWarehouses(anyInt(), anyString(), anyString(), anyString(), anyString());
//     }

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
//    @Test
//    public void updateWarehouseCorrectTest() throws Exception {
//        Warehouse testWarehouse = setupWarehouse();
//        MockHttpServletRequestBuilder request = post("/updateWarehouse")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("warehouseId",String.valueOf(testWarehouse.getWarehouseId()))
//                .param("warehouseName","newName")
//                .param("warehouseAddress","newAddress")
//                .param("warehouseLong","1.1")
//                .param("warehouseLat","1.2");
//
//        mockMvc.perform(request);
//        verify(mockService).updateWarehouse(refEq(testWarehouse));
//    }
}
