package fr.depix.bulb_manager.framework.annotation;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public interface Evolve<I extends Identifier, A extends Aggregate<I>, E extends Event> extends BiFunction<Optional<A>, List<E>, A> {
}
