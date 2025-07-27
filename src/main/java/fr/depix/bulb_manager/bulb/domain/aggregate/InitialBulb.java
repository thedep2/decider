package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

public record InitialBulb(
        BulbId id
) implements BulbAggregate {

    public static final int INITIALE_COUNT = 0;

    @Override
    public BulbId id() {
        return new BulbId(1L);
    }

    @Override
    public boolean isTurnOn() {
        return false;
    }

    @Override
    public Count count() {
        return new Count(INITIALE_COUNT);
    }

    @Override
    public int nbActivation() {
        return 0;
    }

    @Override
    public Long aggregateVersion() {
        return 0L;
    }
}
