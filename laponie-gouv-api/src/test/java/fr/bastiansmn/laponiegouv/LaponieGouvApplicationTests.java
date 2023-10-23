package fr.bastiansmn.laponiegouv;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Pour utiliser le fichier application-test.properties
class LaponieGouvApplicationTests {

	@Test
	void contextLoads() {
	}

}
