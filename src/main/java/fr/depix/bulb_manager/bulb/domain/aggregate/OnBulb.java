package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

public record OnBulb(
        BulbId id,
        Count count
) implements BulbAggregate {

    public OnBulb(BulbAggregate bulbAggregate) {
        this(bulbAggregate.id(), bulbAggregate.count().increment());
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
