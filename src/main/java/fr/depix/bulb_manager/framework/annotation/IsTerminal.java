package fr.depix.bulb_manager.framework.annotation;

import java.util.function.Predicate;

@FunctionalInterface
public interface IsTerminal<A extends Aggregate<I>, I extends Identifier> extends Predicate<A> {

}
