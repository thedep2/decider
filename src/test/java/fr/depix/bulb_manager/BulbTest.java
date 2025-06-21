package fr.depix.bulb_manager;

import fr.depix.bulb_manager.bulb.domain.command.BulbTurnOff;
import fr.depix.bulb_manager.bulb.domain.command.BulbTurnOn;
import fr.depix.bulb_manager.bulb.domain.service.BulbService;
import fr.depix.bulb_manager.bulb.infra.InMemoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BulbTest {

    final InMemoryRepository bulbRepository = new InMemoryRepository();
    final BulbService bulbService = new BulbService(bulbRepository);

    @AfterEach
    void afterEach() {
        bulbRepository.reset();
    }

    @Test
    @DisplayName("Given a new bulb, when I check its state, then it should be off")
    void newBulbIsOff() {
        givenNewBulb();

        thenIHaveATurnOffBulb();
    }

    @Test
    @DisplayName("Given a turn off bulb, when I switch on, then I have a turn on bulb")
    void turnOn() {
        givenNewBulb();

        whenSwitchOn();

        thenIHaveATurnOnBulb();
    }

    @Test
    @DisplayName("Given a turn off bulb, when I switch on and I switch off, then I have a turn off bulb")
    void turnOn_turnOff() {
        givenNewBulb();

        whenSwitchOn();
        whenSwitchOff();

        thenIHaveATurnOffBulb();
    }

    @Test
    @DisplayName("Given a turn on bulb, when I switch on, then I have a turn on bulb")
    void turnOn_alreadyOn() {
        givenNewBulb();

        whenSwitchOn();
        whenSwitchOn();

        thenIHaveATurnOnBulb();
    }

    @Test
    @DisplayName("Given a turn off bulb, when I switch off, then I have a turn off bulb")
    void turnOff_alreadyOff() {
        givenNewBulb();

        whenSwitchOff();

        thenIHaveATurnOffBulb();
    }

    @Test
    @DisplayName("Given a new bulb, when I switch on thrice, then I have a went out bulb")
    void turnOff() {
        givenNewBulb();

        whenSwitchOn();
        whenSwitchOff();
        whenSwitchOn();
        whenSwitchOff();
        whenSwitchOn();

        thenIHaveATurnOffBulb();
    }

    private void givenNewBulb() {
        bulbService.newBulb();
    }

    private void thenIHaveATurnOffBulb() {
        Assertions.assertThat(bulbService.isTurnOn()).isFalse();
    }

    private void whenSwitchOn() {
        bulbService.handleCommand(new BulbTurnOn());
    }

    private void whenSwitchOff() {
        bulbService.handleCommand(new BulbTurnOff());
    }

    private void thenIHaveATurnOnBulb() {
        Assertions.assertThat(bulbService.isTurnOn()).isTrue();
    }
}
