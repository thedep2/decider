package fr.depix.bulb_manager.framework.annotation;

public interface Command<I extends AggregateId> {

    I aggregateId();
}
