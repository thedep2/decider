package fr.depix.bulb_manager.bulb.domain.event;

import fr.depix.bulb_manager.framework.annotation.Event;

public sealed interface BulbEvent extends Event permits BulbSwitchedOff, BulbSwitchedOn, BulbWentOut {
}

