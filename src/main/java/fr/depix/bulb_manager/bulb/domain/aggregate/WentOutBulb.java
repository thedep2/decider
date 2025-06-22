package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.bulb.domain.BulbDomain;
import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

public record WentOutBulb(
        BulbId id
) implements BulbAggregate {

    @Override
    public boolean isTurnOn() {
        return false;
    }

    @Override
    public int count() {
        return BulbDomain.LIMIT;
    }

}
