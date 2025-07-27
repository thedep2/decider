package fr.depix.bulb_manager.framework.service;

import fr.depix.bulb_manager.framework.annotation.Aggregate;
import fr.depix.bulb_manager.framework.annotation.Command;
import fr.depix.bulb_manager.framework.annotation.Decider;
import fr.depix.bulb_manager.framework.annotation.Domain;
import fr.depix.bulb_manager.framework.annotation.Event;
import fr.depix.bulb_manager.framework.annotation.Evolve;
import fr.depix.bulb_manager.framework.annotation.Identifier;
import fr.depix.bulb_manager.framework.annotation.IsTerminal;
import fr.depix.bulb_manager.framework.annotation.Repository;
import fr.depix.bulb_manager.framework.annotation.ValidationError;
import fr.depix.bulb_manager.framework.decision.Decision;
import fr.depix.bulb_manager.framework.decision.ErrorList;
import fr.depix.bulb_manager.framework.decision.EventList;
import fr.depix.bulb_manager.framework.exception.ValidationRuntimeException;

import java.util.Optional;
import java.util.stream.Collectors;

public class CommandHandler<
        A extends Aggregate<I>,
        I extends Identifier,
        C extends Command<I>,
        R extends Repository<A, I>,
        E extends Event<I>,
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

    @org.jmolecules.architecture.cqrs.CommandHandler
    public A handle(C command) {

        Optional<A> aggregate = repository.findAggregateById(command.aggregateId());

        final Decision<E, VE, I> decision = decider.apply(command, aggregate);

        final A newState = switch (decision) {
            case EventList<E, VE, I> events -> evolve.apply(aggregate, events.events());
            case ErrorList<E, VE, I> errorList -> throw new ValidationRuntimeException(errorList.errors()
                                                                                                .stream()
                                                                                                .map(ValidationError::message)
                                                                                                .collect(Collectors.joining(" "))
            );
        };

        repository.save(newState);

        return newState;
    }

}
