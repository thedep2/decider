package fr.depix.bulb_manager.bulb.domain.service;

import fr.depix.bulb_manager.bulb.domain.aggregate.Bulb;
import fr.depix.bulb_manager.bulb.domain.aggregate.BulbId;
import fr.depix.bulb_manager.bulb.domain.command.BulbCommand;
import fr.depix.bulb_manager.bulb.domain.event.BulbEvent;
import fr.depix.bulb_manager.framework.annotation.Decider;

@FunctionalInterface
public interface BulbDecider extends Decider<BulbId, BulbCommand, Bulb, BulbEvent> {

}
