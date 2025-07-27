package fr.depix.bulb_manager.framework.annotation;

import java.util.function.Supplier;

public interface Domain<
        A extends Aggregate<I>,
        I extends Identifier,
        C extends Command<I>,
        R extends Repository<A, I>,
        E extends Event<I>,
        T extends IsTerminal<A, I>,
        D extends Decider<I, C, A, E, VE>,
        V extends Evolve<I, A, E>,
        VE extends ValidationError> {

    Supplier<R> repository();

    T isTerminal();

    D decider();

    V evolve();

    Supplier<A> initialState(I id);

    Supplier<A> terminalState(A state);

}
