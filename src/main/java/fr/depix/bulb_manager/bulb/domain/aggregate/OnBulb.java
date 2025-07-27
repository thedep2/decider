package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

public record OnBulb(
        BulbId id,
        Count count,
        Long aggregateVersion
) implements BulbAggregate {

    public OnBulb(BulbAggregate bulbAggregate) {
        this(bulbAggregate.id(), bulbAggregate.count().increment(), bulbAggregate.aggregateVersion() + 1L);
    }

    @Override
    public boolean isTurnOn() {
        return true;
    }

    @Override
    public int nbActivation() {
        return count.value();
    }
}
