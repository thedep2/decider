package fr.depix.bulb_manager.framework.annotation;

@org.jmolecules.architecture.cqrs.Command
public interface Command<I extends Identifier> {

    I aggregateId();
}
