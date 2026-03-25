package fr.depix.bulb_manager.bulb.domain.service;

import fr.depix.bulb_manager.bulb.domain.BulbDomain;
import fr.depix.bulb_manager.bulb.domain.BulbIsTerminal;
import fr.depix.bulb_manager.bulb.domain.BulbValidationError;
import fr.depix.bulb_manager.bulb.domain.aggregate.BulbAggregate;
import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;
import fr.depix.bulb_manager.bulb.domain.command.BulbCommand;
import fr.depix.bulb_manager.bulb.domain.event.BulbEvent;
import fr.depix.bulb_manager.bulb.domain.spi.BulbRepository;
import fr.depix.bulb_manager.framework.exception.AggregateNotFoundRuntimeException;
import fr.depix.bulb_manager.framework.result.Result;
import fr.depix.bulb_manager.framework.service.CommandHandler;
import org.springframework.stereotype.Service;

@Service
public class BulbService {

    private final BulbRepository bulbRepository;

    private final CommandHandler<BulbAggregate, BulbId, BulbCommand, BulbRepository, BulbEvent, BulbIsTerminal, BulbDecider, BulbEvolver, BulbValidationError> commandHandler;

    public BulbService(BulbRepository bulbRepository) {
        this.bulbRepository = bulbRepository;
        this.commandHandler = new CommandHandler<>(new BulbDomain(bulbRepository));
    }

    public boolean isTurnOn() {
        return bulbRepository.find()
                             .orElseThrow(AggregateNotFoundRuntimeException::new)
                             .isTurnOn();
    }

    public Result<BulbEvent, BulbValidationError> handleCommand(BulbCommand command) {
        return commandHandler.handle(command);
    }

    public Long getAggregateVersion() {
        return bulbRepository.find()
                             .orElseThrow(AggregateNotFoundRuntimeException::new)
                             .aggregateVersion();

    }
}
