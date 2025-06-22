package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;
import fr.depix.bulb_manager.framework.annotation.Aggregate;

public sealed interface BulbAggregate extends Aggregate<BulbId> permits Bulb, InitialBulb, WentOutBulb {

    boolean isTurnOn();

    int count();

}
