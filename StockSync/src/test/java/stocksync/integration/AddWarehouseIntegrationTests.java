package stocksync.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import stocksync.DisabledSecurityConfig;
import stocksync.StockSyncApplication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {StockSyncApplication.class, DisabledSecurityConfig.class})
@ActiveProfiles("test")
public class AddWarehouseIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void testPresenceOfNavbar() throws Exception {

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/addWarehouse", String.class);
        // Check if the response status code is OK (200)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Navbar presense
        assertThat(response.getBody()).contains("Search");
        assertThat(response.getBody()).contains("Add Warehouse");
        assertThat(response.getBody()).contains("All Warehouses");
        // Team name presense
        assertThat(response.getBody()).contains("StockSync");
      
    }

    @Test
    public void testPresenceOfButton() throws Exception {

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/addWarehouse", String.class);
    
        // Navbar presense
        assertThat(response.getBody()).contains("Back to search");
        assertThat(response.getBody()).contains("Add Warehouse");
      
    }
   

    @Test
    public void testTexbox() throws Exception {

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/addWarehouse", String.class);
    
        // Textbox Info
        assertThat(response.getBody()).contains("Enter warehouse name");
        assertThat(response.getBody()).contains("Enter warehouse address");
        assertThat(response.getBody()).contains("Enter warehouse longitude");
        assertThat(response.getBody()).contains("Enter warehouse latitude");
      
    }
   
    
}