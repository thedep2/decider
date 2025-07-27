package fr.depix.bulb_manager.framework.annotation;

import org.jmolecules.ddd.annotation.AggregateRoot;

@AggregateRoot
public interface Aggregate<I extends Identifier> extends Entity<I> {

    Long aggregateVersion();

}
