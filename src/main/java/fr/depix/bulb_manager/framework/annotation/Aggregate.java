package fr.depix.bulb_manager.framework.annotation;

public interface Aggregate<I extends AggregateId> {

    I aggregateId();

}
