package fr.depix.bulb_manager.framework.annotation;

import java.util.List;
import java.util.function.BiFunction;

public interface Evolve<I extends AggregateId, A extends Aggregate<I>, E extends Event> extends BiFunction<A, List<E>, A> {
}
