package fr.depix.bulb_manager.bulb.domain.service;

import fr.depix.bulb_manager.bulb.domain.aggregate.Bulb;
import fr.depix.bulb_manager.bulb.domain.command.BulbCommand;
import fr.depix.bulb_manager.bulb.domain.command.BulbTurnOff;
import fr.depix.bulb_manager.bulb.domain.command.BulbTurnOn;
import fr.depix.bulb_manager.bulb.domain.event.BulbEvent;
import fr.depix.bulb_manager.bulb.domain.event.BulbSwitchedOff;
import fr.depix.bulb_manager.bulb.domain.event.BulbSwitchedOn;
import fr.depix.bulb_manager.bulb.domain.event.BulbWentOut;
import fr.depix.bulb_manager.bulb.domain.spi.BulbRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulbService {

    public static final int LIMIT = 2;

    private static final BulbDecider DECIDER =
            (command, bulb) -> switch (command) {
                case BulbTurnOff ignored when bulb.isTurnOn() -> List.of(new BulbSwitchedOff());
                case BulbTurnOff ignored -> List.of();
                case BulbTurnOn ignored when !bulb.isTurnOn() && bulb.count() >= LIMIT -> List.of(new BulbWentOut());
                case BulbTurnOn ignored when !bulb.isTurnOn() -> List.of(new BulbSwitchedOn());
                case BulbTurnOn ignored -> List.of();
            };

    private static final BulbEvolver EVOLVE =
            (bulb, bulbEvents) -> {
                for (BulbEvent event : bulbEvents) {
                    bulb = switch (event) {
                        case BulbSwitchedOff ignored -> new Bulb(false, bulb.count());
                        case BulbSwitchedOn ignored -> new Bulb(true, bulb.count() + 1);
                        case BulbWentOut ignored -> new Bulb(false, bulb.count());
                    };
                }
                return bulb;
            };

    private final BulbRepository bulbRepository;

    public BulbService(BulbRepository bulbRepository) {
        this.bulbRepository = bulbRepository;
    }

    public void newBulb() {
        bulbRepository.save(new Bulb());
    }

    public boolean isTurnOn() {
        return bulbRepository.get().isTurnOn();
    }

    public void handleCommand(BulbCommand command) {
        final Bulb bulb = bulbRepository.get();

        final List<BulbEvent> events = DECIDER.apply(command, bulb);

        final Bulb newBulb = EVOLVE.apply(bulb, events);

        bulbRepository.save(newBulb);
    }

}
