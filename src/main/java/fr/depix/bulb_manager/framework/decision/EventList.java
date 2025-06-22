package fr.depix.bulb_manager.framework.decision;

import fr.depix.bulb_manager.framework.annotation.Event;
import fr.depix.bulb_manager.framework.annotation.ValidationError;

import java.util.List;

public record EventList<E extends Event, VE extends ValidationError>(
        List<E> events
) implements Decision<E, VE> {
}
