package fr.depix.bulb_manager.framework.annotation;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.ZonedDateTime;
import java.util.UUID;

@DomainEvent
public interface Event<I extends Identifier> {

    I aggregateId();

    UUID eventId();

    Long aggregateVersion();

    String eventType();

    Long eventVersion();

    ZonedDateTime eventDate();

}
