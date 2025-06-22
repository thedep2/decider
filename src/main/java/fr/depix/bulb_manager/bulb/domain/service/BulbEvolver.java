package fr.depix.bulb_manager.bulb.domain.service;

import fr.depix.bulb_manager.bulb.domain.aggregate.BulbAggregate;
import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;
import fr.depix.bulb_manager.bulb.domain.event.BulbEvent;
import fr.depix.bulb_manager.framework.annotation.Evolve;

@FunctionalInterface
public interface BulbEvolver extends Evolve<BulbId, BulbAggregate, BulbEvent> {

}
