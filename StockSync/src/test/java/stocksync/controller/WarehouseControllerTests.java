package stocksync.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.*;
import stocksync.service.WarehouseService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

}
