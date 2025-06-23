package fr.depix.bulb_manager.framework.annotation;

import fr.depix.bulb_manager.framework.decision.Decision;

import java.util.Optional;
import java.util.function.BiFunction;

public interface Decider<I extends Identifier, C extends Command<I>, A extends Aggregate<I>, E extends Event, VE extends ValidationError> extends BiFunction<C, Optional<A>, Decision<E, VE>> {
}
