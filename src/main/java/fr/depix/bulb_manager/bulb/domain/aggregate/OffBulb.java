package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

public record OffBulb(
        BulbId id,
        Count count

) implements BulbAggregate {

    public OffBulb(BulbAggregate bulbAggregate) {
        this(bulbAggregate.id(), bulbAggregate.count());
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
