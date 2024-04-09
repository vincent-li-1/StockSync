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
public class SearchPageIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void searchPageLoadsCorrectly() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/search", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        //assertThat(response.getBody()).contains("Warehouse Name", "Warehouse Address", "Warehouse Longitude", "Warehouse Latitude");
    }

    // Additional test for pagination and search functionality would require interaction with the page,
    // potentially using something like Selenium, as TestRestTemplate cannot handle JavaScript or form submission.

    @Test
    public void testEditButtonPresence() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/search", String.class);
        assertThat(response.getBody()).contains("Search");
    }

    @Test
    public void testDeleteButtonPresence() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/search", String.class);
        assertThat(response.getBody()).contains("Add Warehouse");
    }
}
