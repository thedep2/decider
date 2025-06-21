package fr.depix.bulb_manager.bulb.domain;

import fr.depix.bulb_manager.bulb.domain.aggregate.Bulb;
import fr.depix.bulb_manager.bulb.domain.command.BulbTurnOff;
import fr.depix.bulb_manager.bulb.domain.command.BulbTurnOn;
import fr.depix.bulb_manager.bulb.domain.command.Command;
import fr.depix.bulb_manager.bulb.domain.event.BulbSwitchedOff;
import fr.depix.bulb_manager.bulb.domain.event.BulbSwitchedOn;
import fr.depix.bulb_manager.bulb.domain.event.BulbWentOut;
import fr.depix.bulb_manager.bulb.domain.event.Event;
import fr.depix.bulb_manager.bulb.domain.event.NothingHappen;
import fr.depix.bulb_manager.bulb.domain.spi.BulbRepository;
import org.springframework.stereotype.Service;

@Service
public class BulbService {

    public static final int LIMIT = 2;

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
        Bulb bulb = bulbRepository.get();

        Event event = switch (command) {
            case BulbTurnOff ignored when bulb.isTurnOn() -> new BulbSwitchedOff();
            case BulbTurnOff ignored -> new NothingHappen();
            case BulbTurnOn ignored when !bulb.isTurnOn() && bulb.count() >= LIMIT -> new BulbWentOut();
            case BulbTurnOn ignored when !bulb.isTurnOn() -> new BulbSwitchedOn();
            case BulbTurnOn ignored -> new NothingHappen();
        };

        Bulb newBulb = switch (event) {
            case BulbSwitchedOff ignored -> new Bulb(false, bulb.count());
            case BulbSwitchedOn ignored -> new Bulb(true, bulb.count() + 1);
            case BulbWentOut ignored -> new Bulb(false, bulb.count());
            case NothingHappen ignored -> bulb;
        };
        bulbRepository.save(newBulb);
    }

}
