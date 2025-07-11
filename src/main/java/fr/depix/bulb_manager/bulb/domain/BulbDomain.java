package fr.depix.bulb_manager.bulb.domain;

import fr.depix.bulb_manager.bulb.domain.aggregate.Bulb;
import fr.depix.bulb_manager.bulb.domain.aggregate.BulbAggregate;
import fr.depix.bulb_manager.bulb.domain.aggregate.InitialBulb;
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

import java.util.List;
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
        return bulbAggregate -> bulbAggregate.count() >= LIMIT || bulbAggregate instanceof WentOutBulb;
    }

    @Override
    public BulbDecider decider() {
        return (command, bulbOptional) -> {
            if (!(command instanceof CreateBulB ignored) && bulbOptional.isEmpty())
                return new ErrorList<>(List.of(new BulbValidationError("Don't exist")));

            if (command instanceof CreateBulB(BulbId bulbId) && bulbOptional.isEmpty())
                return new EventList<>(List.of(new BulbCreated(bulbId)));

            final BulbAggregate bulb = bulbOptional.get();

            return switch (command) {
                case CreateBulB createBulB -> new EventList<>(List.of(new BulbCreated(createBulB.bulbId())));
                case BulbTurnOff ignored when bulb.isTurnOn() -> new EventList<>(List.of(new BulbSwitchedOff()));
                case BulbTurnOff ignored -> new EventList<>(List.of());
                case BulbTurnOn ignored when !bulb.isTurnOn() && bulb.count() >= LIMIT -> new EventList<>(List.of(new BulbWentOut()));
                case BulbTurnOn ignored when !bulb.isTurnOn() -> new EventList<>(List.of(new BulbSwitchedOn()));
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
                    case BulbCreated bulbCreated -> initialState(bulbCreated.bulbId());
                    case BulbSwitchedOff ignored -> new Bulb(bulb.id(), false, bulb.count());
                    case BulbSwitchedOn ignored -> new Bulb(bulb.id(), true, bulb.count() + 1);
                    case BulbWentOut ignored -> new WentOutBulb(bulb.id());
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
        return new WentOutBulb(state.id());
    }
}
