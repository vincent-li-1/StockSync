package stocksync.controller;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import stocksync.DisabledSecurityConfig;
import stocksync.StockSyncApplication;



@SpringBootTest
@ContextConfiguration(classes = {StockSyncApplication.class, DisabledSecurityConfig.class})
@ActiveProfiles("test")
class StockSyncApplicationTests {

	@Test
	void contextLoads() {
	}

}
