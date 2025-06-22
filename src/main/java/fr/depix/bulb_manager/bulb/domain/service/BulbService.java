package fr.depix.bulb_manager.bulb.domain.service;

import fr.depix.bulb_manager.bulb.domain.aggregate.Bulb;
import fr.depix.bulb_manager.bulb.domain.aggregate.BulbId;
import fr.depix.bulb_manager.bulb.domain.command.BulbCommand;
import fr.depix.bulb_manager.bulb.domain.command.BulbTurnOff;
import fr.depix.bulb_manager.bulb.domain.command.BulbTurnOn;
import fr.depix.bulb_manager.bulb.domain.event.BulbEvent;
import fr.depix.bulb_manager.bulb.domain.event.BulbSwitchedOff;
import fr.depix.bulb_manager.bulb.domain.event.BulbSwitchedOn;
import fr.depix.bulb_manager.bulb.domain.event.BulbWentOut;
import fr.depix.bulb_manager.bulb.domain.spi.BulbRepository;
import fr.depix.bulb_manager.framework.exception.AggregateNotFoundRuntimeException;
import fr.depix.bulb_manager.framework.service.CommandHandler;
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

    private final CommandHandler<Bulb, BulbId, BulbCommand, BulbRepository, BulbEvent> commandHandler;

    public BulbService(BulbRepository bulbRepository) {
        this.bulbRepository = bulbRepository;
        this.commandHandler = new CommandHandler<>(bulbRepository, DECIDER, EVOLVE);
    }

    public void newBulb() {
        bulbRepository.save(new Bulb());
    }

    public boolean isTurnOn() {
        return bulbRepository.get()
                             .orElseThrow(AggregateNotFoundRuntimeException::new)
                             .isTurnOn();
    }

    public void handleCommand(BulbCommand command) {
        commandHandler.handle(command);
    }

}
