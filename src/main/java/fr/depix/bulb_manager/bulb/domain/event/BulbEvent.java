package fr.depix.bulb_manager.bulb.domain.event;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;
import fr.depix.bulb_manager.framework.annotation.Event;

public sealed interface BulbEvent extends Event<BulbId> permits BulbCreated, BulbSwitchedOff, BulbSwitchedOn, BulbWentOut {
}

