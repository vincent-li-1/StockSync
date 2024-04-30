package stocksync.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import stocksync.StockSyncApplication;
import stocksync.service.ItemService;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for item controller.
 */
@WebMvcTest(ItemController.class)
@ContextConfiguration(classes = StockSyncApplication.class)
public class ItemControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService mockService;

    @Test
    @WithMockUser(username="spring")
    public void searchTest() throws Exception {
        this.mockMvc.perform(get("/searchItem"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("searchItem"));
    }
}
