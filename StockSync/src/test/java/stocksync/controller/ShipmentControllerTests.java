package stocksync.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import stocksync.model.Shipment;
import stocksync.service.ShipmentService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for controllers.
 */
@WebMvcTest(ShipmentController.class)
public class ShipmentControllerTests {
    /*
     * Set up test environment:
     *
     * mockMvc: a mvc used to send HTTP request for testing
     * mockService: a mock service class used for testing
     */
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ShipmentService mockService;
    //setting up a standard test warehouse object
    public Shipment setupShipment(){
        Shipment testShipment = new Shipment();
        testShipment.setShipmentId(10000);
        testShipment.setWarehouseFromId(1);
        testShipment.setWarehouseToId(2);
        return testShipment;
    }

    /**
     * Test if the search endpoint return the correct template to render.
     * @throws Exception if the test failed
     */
    @Test
    public void searchTest() throws Exception {
        this.mockMvc.perform(get("/shipment/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("shipmentSearch"));
    }

    /**
     * Test if the insertWarehouse endpoint return the correct template to render.
     * @throws Exception if the test failed
     */
    @Test
    public void insertShipmentTest() throws Exception {
        Shipment testShipment = setupShipment();
        MockHttpServletRequestBuilder request = post("/insertShipment")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("shipmentId",String.valueOf(testShipment.getShipmentId()))
                .param("warehouseFromId","1")
                .param("warehouseToId","2");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection()) // Expect a redirect status
                .andExpect(redirectedUrl("/shipmentSearchResults?page=1")); // Expect redirection to the specified URL
    }
    /**
     * Test if updateWarehouse redirects to the correct URL after updating
     * @throws Exception
     */
    @Test
    public void updateShipmentTest() throws Exception {
        Shipment testShipment = setupShipment();
        MockHttpServletRequestBuilder request = post("/shipment/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("shipmentId",String.valueOf(testShipment.getShipmentId()))
                .param("warehouseFromId","1")
                .param("warehouseToId","2");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection()) // Expect a redirect status
                .andExpect(redirectedUrl("/shipmentSearchResults?page=1")); // Expect redirection to the specified URL

    }
}