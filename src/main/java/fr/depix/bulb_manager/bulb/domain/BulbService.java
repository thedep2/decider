package fr.depix.bulb_manager.bulb.domain;

import fr.depix.bulb_manager.bulb.domain.aggregate.Bulb;
import fr.depix.bulb_manager.bulb.domain.command.BulbTurnOff;
import fr.depix.bulb_manager.bulb.domain.command.BulbTurnOn;
import fr.depix.bulb_manager.bulb.domain.command.Command;
import fr.depix.bulb_manager.bulb.domain.event.BulbSwitchedOff;
import fr.depix.bulb_manager.bulb.domain.event.BulbSwitchedOn;
import fr.depix.bulb_manager.bulb.domain.event.BulbWentOut;
import fr.depix.bulb_manager.bulb.domain.event.Event;
import fr.depix.bulb_manager.bulb.domain.spi.BulbRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulbService {

    public static final int LIMIT = 2;

    private BulbRepository bulbRepository;

    public BulbService(BulbRepository bulbRepository) {
        this.bulbRepository = bulbRepository;
    }

    private static Bulb evolve(List<Event> events, Bulb bulb) {
        for (Event event : events) {
            bulb = switch (event) {
                case BulbSwitchedOff ignored -> new Bulb(false, bulb.count());
                case BulbSwitchedOn ignored -> new Bulb(true, bulb.count() + 1);
                case BulbWentOut ignored -> new Bulb(false, bulb.count());
            };
        }
        return bulb;
    }

    private static List<Event> decide(Command command, Bulb bulb) {
        return switch (command) {
            case BulbTurnOff ignored when bulb.isTurnOn() -> List.of(new BulbSwitchedOff());
            case BulbTurnOff ignored -> List.of();
            case BulbTurnOn ignored when !bulb.isTurnOn() && bulb.count() >= LIMIT -> List.of(new BulbWentOut());
            case BulbTurnOn ignored when !bulb.isTurnOn() -> List.of(new BulbSwitchedOn());
            case BulbTurnOn ignored -> List.of();
        };
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

        final List<Event> events = decide(command, bulb);

        bulb = evolve(events, bulb);

        bulbRepository.save(bulb);
    }

}
