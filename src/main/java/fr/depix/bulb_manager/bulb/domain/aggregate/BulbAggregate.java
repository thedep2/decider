package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;
import fr.depix.bulb_manager.framework.annotation.Aggregate;
import org.jmolecules.ddd.annotation.AggregateRoot;

@AggregateRoot
public sealed interface BulbAggregate extends Aggregate<BulbId> permits InitialBulb, OffBulb, OnBulb, WentOutBulb {

    boolean isTurnOn();

    Count count();

    int nbActivation();

}
