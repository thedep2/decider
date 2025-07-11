package fr.depix.bulb_manager.bulb.domain.service;

import fr.depix.bulb_manager.bulb.domain.aggregate.BulbAggregate;
import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;
import fr.depix.bulb_manager.bulb.domain.command.BulbCommand;
import fr.depix.bulb_manager.bulb.domain.event.BulbEvent;
import fr.depix.bulb_manager.framework.annotation.Decider;
import fr.depix.bulb_manager.framework.annotation.ValidationError;

@FunctionalInterface
public interface BulbDecider extends Decider<BulbId, BulbCommand, BulbAggregate, BulbEvent, ValidationError> {

}
