package fr.depix.bulb_manager.bulb.domain.service;

import fr.depix.bulb_manager.bulb.domain.aggregate.Bulb;
import fr.depix.bulb_manager.bulb.domain.event.BulbEvent;
import fr.depix.bulb_manager.framework.annotation.Evolve;

@FunctionalInterface
public interface BulbEvolver extends Evolve<Bulb, BulbEvent> {

}
