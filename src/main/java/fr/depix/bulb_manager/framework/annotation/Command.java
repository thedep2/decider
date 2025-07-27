package fr.depix.bulb_manager.framework.annotation;

import java.time.ZonedDateTime;

@org.jmolecules.architecture.cqrs.Command
public interface Command<I extends Identifier> {

    I aggregateId();

    Long aggregateVersion();

    ZonedDateTime commandDate();
}
