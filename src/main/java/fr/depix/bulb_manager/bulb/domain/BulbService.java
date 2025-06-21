package fr.depix.bulb_manager.bulb.domain;

import fr.depix.bulb_manager.bulb.domain.command.Command;
import fr.depix.bulb_manager.bulb.domain.command.SwitchOff;
import fr.depix.bulb_manager.bulb.domain.command.SwitchOn;
import fr.depix.bulb_manager.bulb.domain.spi.BulbRepository;
import org.springframework.stereotype.Service;

@Service
public class BulbService {

    private BulbRepository bulbRepository;

    public BulbService(BulbRepository bulbRepository) {
        this.bulbRepository = bulbRepository;
    }

    public void newBulb() {
        var bulb = new Bulb();
        bulbRepository.save(bulb);
    }

    public boolean isTurnOn() {
        Bulb bulb = bulbRepository.get();
        return bulb.isTurnOn();
    }

    public void handleCommand(Command command) {
        switch (command) {
            case SwitchOff ignored -> switchOff();
            case SwitchOn ignored -> switchOn();
        }
    }

    private void switchOff() {
        Bulb bulb = bulbRepository.get();
        bulb.switchOff();
    }

    private void switchOn() {
        Bulb bulb = bulbRepository.get();
        bulb.switchOn();
    }

}
