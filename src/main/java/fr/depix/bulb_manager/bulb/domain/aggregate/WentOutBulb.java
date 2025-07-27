package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.bulb.domain.BulbDomain;
import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

public record WentOutBulb(
        BulbId id,
        Long aggregateVersion
) implements BulbAggregate {

    public WentOutBulb(BulbAggregate bulbAggregate) {
        this(bulbAggregate.id(), bulbAggregate.aggregateVersion() + 1L);
    }

    @Override
    public boolean isTurnOn() {
        return false;
    }

    @Override
    public Count count() {
        return new Count(BulbDomain.LIMIT);
    }

    @Override
    public int nbActivation() {
        return BulbDomain.LIMIT;
    }

}
