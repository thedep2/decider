package fr.depix.bulb_manager.framework.service;

import fr.depix.bulb_manager.framework.annotation.Aggregate;
import fr.depix.bulb_manager.framework.annotation.AggregateId;
import fr.depix.bulb_manager.framework.annotation.Command;
import fr.depix.bulb_manager.framework.annotation.Decider;
import fr.depix.bulb_manager.framework.annotation.Event;
import fr.depix.bulb_manager.framework.annotation.Evolve;
import fr.depix.bulb_manager.framework.annotation.Repository;
import fr.depix.bulb_manager.framework.exception.AggregateNotFoundRuntimeException;

import java.util.List;

public class CommandHandler<A extends Aggregate, I extends AggregateId, C extends Command<I>, R extends Repository<A, I>, E extends Event> {

    private final R repository;
    private final Decider<I, C, A, E> decider;
    private final Evolve<A, E> evolve;

    public CommandHandler(R repository, Decider<I, C, A, E> decider, Evolve<A, E> evolve) {
        this.repository = repository;
        this.decider = decider;
        this.evolve = evolve;
    }

    public void handle(C command) {

        A aggregate = repository.findById(command.aggregateId())
                                .orElseThrow(AggregateNotFoundRuntimeException::new);

        final List<E> events = decider.apply(command, aggregate);
        final A newState = evolve.apply(aggregate, events);

        repository.save(newState);

    }

}
