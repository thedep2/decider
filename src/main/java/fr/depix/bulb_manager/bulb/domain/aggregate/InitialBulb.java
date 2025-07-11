package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

public record InitialBulb(
        BulbId id
) implements BulbAggregate {

    @Override
    public BulbId id() {
        return new BulbId(1L);
    }

    @Override
    public boolean isTurnOn() {
        return false;
    }

    @Override
    public int count() {
        return 0;
    }
}
