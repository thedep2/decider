package fr.depix.bulb_manager.framework.service;

import fr.depix.bulb_manager.framework.annotation.Aggregate;
import fr.depix.bulb_manager.framework.annotation.AggregateId;
import fr.depix.bulb_manager.framework.annotation.Command;
import fr.depix.bulb_manager.framework.annotation.Decider;
import fr.depix.bulb_manager.framework.annotation.Domain;
import fr.depix.bulb_manager.framework.annotation.Event;
import fr.depix.bulb_manager.framework.annotation.Evolve;
import fr.depix.bulb_manager.framework.annotation.IsTerminal;
import fr.depix.bulb_manager.framework.annotation.Repository;
import fr.depix.bulb_manager.framework.annotation.ValidationError;
import fr.depix.bulb_manager.framework.decision.Decision;
import fr.depix.bulb_manager.framework.decision.ErrorList;
import fr.depix.bulb_manager.framework.decision.EventList;

import java.util.Optional;

public class CommandHandler<
        A extends Aggregate<I>,
        I extends AggregateId,
        C extends Command<I>,
        R extends Repository<A, I>,
        E extends Event,
        T extends IsTerminal<A, I>,
        D extends Decider<I, C, A, E, VE>,
        V extends Evolve<I, A, E>,
        VE extends ValidationError> {

    private final D decider;
    private final V evolve;
    private final R repository;

    public CommandHandler(Domain<A, I, C, R, E, T, D, V, VE> domain) {
        this.repository = domain.repository().get();
        this.decider = domain.decider();
        this.evolve = domain.evolve();
    }

    public void handle(C command) {

        Optional<A> aggregate = repository.findById(command.aggregateId());

        final Decision<E, VE> decision = decider.apply(command, aggregate);

        final A newState = switch (decision) {
            case EventList<E, VE> events -> evolve.apply(aggregate, events.events());
            case ErrorList<E, VE> ignored -> throw new RuntimeException();
        };

        repository.save(newState);

    }

}
