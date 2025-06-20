package fr.depix.bulb_manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class BulbManagerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void modulith() {
        ApplicationModules.of(BulbManagerApplication.class).verify();
    }
}
