package fr.depix.bulb_manager;

import fr.depix.bulb_manager.bulb.domain.BulbService;
import fr.depix.bulb_manager.bulb.infra.InMemoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BulbTest {

    InMemoryRepository bulbRepository = new InMemoryRepository();
    BulbService bulbService = new BulbService(bulbRepository);

    @Test
    @DisplayName("Given a new bulb, when I check its state, then it should be off")
    void newBulbIsOff() {
        // Arrange
        bulbService.newBulb();

        // Act & Assert
        Assertions.assertThat(bulbService.isTurnOn()).isFalse();
    }

    @Test
    @DisplayName("Given a turn off bulb, when I switch on, then I have a turn on bulb")
    void turnOn() {
        bulbService.newBulb();
        bulbService.switchOn();
        Assertions.assertThat(bulbService.isTurnOn()).isTrue();
    }

    @Test
    @DisplayName("Given a turn off bulb, when I switch on and I switch off, then I have a turn off bulb")
    void turnOn_turnOff() {
        bulbService.newBulb();
        bulbService.switchOn();
        bulbService.switchOff();
        Assertions.assertThat(bulbService.isTurnOn()).isFalse();
    }

    @Test
    @DisplayName("Given a turn on bulb, when I switch on, then I have a turn on bulb")
    void turnOn_alreadyOn() {
        bulbService.newBulb();
        bulbService.switchOn();
        bulbService.switchOn();
        Assertions.assertThat(bulbService.isTurnOn()).isTrue();
    }

    @Test
    @DisplayName("Given a turn off bulb, when I switch off, then I have a turn off bulb")
    void turnOff_alreadyOff() {
        bulbService.newBulb();
        bulbService.switchOff();
        Assertions.assertThat(bulbService.isTurnOn()).isFalse();
    }

    @Test
    @DisplayName("Given a new bulb, when I switch on thrice, then I have a went out bulb")
    void turnOff() {
        bulbService.newBulb();
        bulbService.switchOn();
        bulbService.switchOn();
        bulbService.switchOn();
        Assertions.assertThat(bulbService.isTurnOn()).isFalse();
    }
}
