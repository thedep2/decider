package fr.depix.bulb_manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class BulbManagerApplicationTests {

    @Test
    public void applicationStarts() {
        assertThatCode(() -> BulbManagerApplication.main(new String[]{})).doesNotThrowAnyException();
    }

    @Test
    void modulith() {
        ApplicationModules.of(BulbManagerApplication.class).verify();
    }
}
