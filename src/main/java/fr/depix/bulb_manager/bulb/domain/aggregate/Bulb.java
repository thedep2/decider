package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.framework.annotation.Aggregate;

public record Bulb(
        boolean isTurnOn,
        int count
) implements Aggregate<BulbId> {

    public Bulb() {
        this(false, 0);
    }

    @Override
    public BulbId aggregateId() {
        return new BulbId(1L);
    }
}
