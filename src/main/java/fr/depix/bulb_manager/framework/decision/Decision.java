package fr.depix.bulb_manager.framework.decision;

import fr.depix.bulb_manager.framework.annotation.Event;
import fr.depix.bulb_manager.framework.annotation.Identifier;
import fr.depix.bulb_manager.framework.annotation.ValidationError;

public sealed interface Decision<E extends Event<I>, VE extends ValidationError, I extends Identifier> permits ErrorList, EventList {
}
