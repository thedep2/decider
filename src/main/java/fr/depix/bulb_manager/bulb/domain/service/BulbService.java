package fr.depix.bulb_manager.bulb.domain.service;

import fr.depix.bulb_manager.bulb.domain.BulbDomain;
import fr.depix.bulb_manager.bulb.domain.BulbIsTerminal;
import fr.depix.bulb_manager.bulb.domain.aggregate.BulbAggregate;
import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;
import fr.depix.bulb_manager.bulb.domain.command.BulbCommand;
import fr.depix.bulb_manager.bulb.domain.event.BulbEvent;
import fr.depix.bulb_manager.bulb.domain.spi.BulbRepository;
import fr.depix.bulb_manager.framework.annotation.ValidationError;
import fr.depix.bulb_manager.framework.exception.AggregateNotFoundRuntimeException;
import fr.depix.bulb_manager.framework.service.CommandHandler;
import org.springframework.stereotype.Service;

@Service
public class BulbService {

    private final BulbRepository bulbRepository;

    private final CommandHandler<BulbAggregate, BulbId, BulbCommand, BulbRepository, BulbEvent, BulbIsTerminal, BulbDecider, BulbEvolver, ValidationError> commandHandler;

    public BulbService(BulbRepository bulbRepository) {
        this.bulbRepository = bulbRepository;
        this.commandHandler = new CommandHandler<>(new BulbDomain(bulbRepository));
    }

    public boolean isTurnOn() {
        return bulbRepository.find()
                             .orElseThrow(AggregateNotFoundRuntimeException::new)
                             .isTurnOn();
    }

    public void handleCommand(BulbCommand command) {
        commandHandler.handle(command);
    }

}
