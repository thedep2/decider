package fr.depix.bulb_manager.framework.decision;

import fr.depix.bulb_manager.framework.annotation.Event;
import fr.depix.bulb_manager.framework.annotation.ValidationError;

public sealed interface Decision<E extends Event, VE extends ValidationError> permits ErrorList, EventList {
}
