package fr.depix.bulb_manager.framework.decision;

import fr.depix.bulb_manager.framework.annotation.Event;
import fr.depix.bulb_manager.framework.annotation.Identifier;
import fr.depix.bulb_manager.framework.annotation.ValidationError;

import java.util.List;

public record EventList<E extends Event<I>, VE extends ValidationError, I extends Identifier>(
        List<E> events
) implements Decision<E, VE, I> {
}
