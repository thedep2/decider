package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

public record OffBulb(
        BulbId id,
        Count count,
        Long aggregateVersion
) implements BulbAggregate {

    public OffBulb(BulbAggregate bulbAggregate) {
        this(bulbAggregate.id(), bulbAggregate.count(), bulbAggregate.aggregateVersion() + 1L);
    }

    @Override
    public boolean isTurnOn() {
        return false;
    }

    @Override
    public int nbActivation() {
        return count.value();
    }
}
