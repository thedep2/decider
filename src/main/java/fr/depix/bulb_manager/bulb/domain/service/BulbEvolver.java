package fr.depix.bulb_manager.bulb.domain.service;

import fr.depix.bulb_manager.bulb.domain.aggregate.Bulb;
import fr.depix.bulb_manager.bulb.domain.event.BulbEvent;

import java.util.List;
import java.util.function.BiFunction;

@FunctionalInterface
public interface BulbEvolver extends BiFunction<Bulb, List<BulbEvent>, Bulb> {

}
