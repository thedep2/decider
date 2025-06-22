package fr.depix.bulb_manager.framework.annotation;

import java.util.List;
import java.util.function.BiFunction;

public interface Decider<I extends AggregateId, C extends Command<I>, A extends Aggregate, E extends Event> extends BiFunction<C, A, List<E>> {
}
