package fr.depix.bulb_manager.bulb.domain.aggregate;

public record Bulb(
        boolean isTurnOn,
        int count
) implements BulbAggregate {

    public Bulb() {
        this(false, 0);
    }

    @Override
    public BulbId aggregateId() {
        return new BulbId(1L);
    }
}
