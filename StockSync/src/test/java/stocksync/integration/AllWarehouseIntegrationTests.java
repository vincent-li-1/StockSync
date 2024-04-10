package stocksync.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllWarehouseIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void testAllWarehouses() throws Exception {

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/warehouseSearchResults?page=1", String.class);
        // Check if the response status code is OK (200)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody()).contains("Warehouse Name");
        assertThat(response.getBody()).contains("Warehouse Address");
        assertThat(response.getBody()).contains("Warehouse Longitude");
        assertThat(response.getBody()).contains("Warehouse Latitude");
        assertThat(response.getBody()).contains("Warehouse Longitude");

        assertThat(response.getBody()).contains("123 Main Street");
        assertThat(response.getBody()).contains("Showing 1 - 10 of 66 total warehouses");
        assertThat(response.getBody()).contains("25.0");
      
    }

    @Test
    public void testAllWarehousesPage3() throws Exception {

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/warehouseSearchResults?page=3", String.class);
        // Check if the response status code is OK (200)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Fixed information presense
        assertThat(response.getBody()).contains("Warehouse Name");
        assertThat(response.getBody()).contains("Warehouse Address");
        assertThat(response.getBody()).contains("Warehouse Longitude");
        assertThat(response.getBody()).contains("Warehouse Latitude");
        assertThat(response.getBody()).contains("Warehouse Longitude");
        // Psedo data stored in the database
        assertThat(response.getBody()).contains("3 Main Street, Madison WI");
        assertThat(response.getBody()).contains("Showing 21 - 30 of 66 total warehouses");
        assertThat(response.getBody()).contains("25.0");
      
    }

    
}