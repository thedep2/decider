package fr.depix.bulb_manager.bulb.domain;

import fr.depix.bulb_manager.bulb.domain.aggregate.BulbAggregate;
import fr.depix.bulb_manager.bulb.domain.aggregate.InitialBulb;
import fr.depix.bulb_manager.bulb.domain.aggregate.OffBulb;
import fr.depix.bulb_manager.bulb.domain.aggregate.OnBulb;
import fr.depix.bulb_manager.bulb.domain.aggregate.WentOutBulb;
import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;
import fr.depix.bulb_manager.bulb.domain.command.BulbCommand;
import fr.depix.bulb_manager.bulb.domain.command.BulbTurnOff;
import fr.depix.bulb_manager.bulb.domain.command.BulbTurnOn;
import fr.depix.bulb_manager.bulb.domain.command.CreateBulB;
import fr.depix.bulb_manager.bulb.domain.event.BulbCreated;
import fr.depix.bulb_manager.bulb.domain.event.BulbEvent;
import fr.depix.bulb_manager.bulb.domain.event.BulbSwitchedOff;
import fr.depix.bulb_manager.bulb.domain.event.BulbSwitchedOn;
import fr.depix.bulb_manager.bulb.domain.event.BulbWentOut;
import fr.depix.bulb_manager.bulb.domain.service.BulbDecider;
import fr.depix.bulb_manager.bulb.domain.service.BulbEvolver;
import fr.depix.bulb_manager.bulb.domain.spi.BulbRepository;
import fr.depix.bulb_manager.framework.annotation.Domain;
import fr.depix.bulb_manager.framework.annotation.ValidationError;
import fr.depix.bulb_manager.framework.decision.ErrorList;
import fr.depix.bulb_manager.framework.decision.EventList;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public record BulbDomain(
        BulbRepository bulbRepository
) implements Domain<BulbAggregate, BulbId, BulbCommand, BulbRepository, BulbEvent, BulbIsTerminal, BulbDecider, BulbEvolver, ValidationError> {

    public static final int LIMIT = 2;

    @Override
    public Supplier<BulbRepository> repository() {
        return () -> bulbRepository;
    }

    @Override
    public BulbIsTerminal isTerminal() {
        return bulbAggregate -> bulbAggregate.nbActivation() >= LIMIT || bulbAggregate instanceof WentOutBulb;
    }

    @Override
    public BulbDecider decider() {
        return (command, bulbOptional) -> {
            if (!(command instanceof CreateBulB ignored) && bulbOptional.isEmpty())
                return new ErrorList<>(List.of(new BulbValidationError("Don't exist")));

            if (bulbOptional.isPresent() && !command.aggregateVersion().equals(bulbOptional.get().aggregateVersion()))
                return new ErrorList<>(List.of(new BulbValidationError("Command ran on an old version of the aggregate, please retry with the latest version of the aggregate.")));

            if (command instanceof CreateBulB(BulbId bulbId, ZonedDateTime ignored) && bulbOptional.isEmpty())
                return new EventList<>(List.of(new BulbCreated(bulbId, UUID.randomUUID(), 0L, ZonedDateTime.now())));

            final BulbAggregate bulb = bulbOptional.get();

            return switch (command) {
                case CreateBulB createBulB -> new EventList<>(List.of(new BulbCreated(createBulB.aggregateId(), UUID.randomUUID(), 0L, ZonedDateTime.now())));
                case BulbTurnOff ignored when bulb.isTurnOn() -> new EventList<>(List.of(new BulbSwitchedOff(bulb.id(), UUID.randomUUID(), bulb.aggregateVersion(), ZonedDateTime.now())));
                case BulbTurnOff ignored -> new EventList<>(List.of());
                case BulbTurnOn ignored when !bulb.isTurnOn() && bulb.nbActivation() >= LIMIT -> new EventList<>(List.of(new BulbWentOut(bulb.id(), UUID.randomUUID(), bulb.aggregateVersion(), ZonedDateTime.now())));
                case BulbTurnOn ignored when !bulb.isTurnOn() -> new EventList<>(List.of(new BulbSwitchedOn(bulb.id(), UUID.randomUUID(), bulb.aggregateVersion(), ZonedDateTime.now())));
                case BulbTurnOn ignored -> new EventList<>(List.of());
            };
        };
    }

    @Override
    public BulbEvolver evolve() {
        return (bulbOpt, bulbEvents) -> {
            BulbAggregate bulb = bulbOpt.orElse(initialState(new BulbId(0L)));
            for (BulbEvent event : bulbEvents) {
                bulb = switch (event) {
                    case BulbCreated bulbCreated -> initialState(bulbCreated.aggregateId());
                    case BulbSwitchedOff ignored -> new OffBulb(bulb);
                    case BulbSwitchedOn ignored -> new OnBulb(bulb);
                    case BulbWentOut ignored -> terminalState(bulb);
                };
            }
            return bulb;
        };
    }

    @Override
    public InitialBulb initialState(BulbId id) {
        return new InitialBulb(id);
    }

    @Override
    public BulbAggregate terminalState(BulbAggregate state) {
        return new WentOutBulb(state);
    }
}
