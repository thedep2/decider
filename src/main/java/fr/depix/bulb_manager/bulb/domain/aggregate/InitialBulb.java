package fr.depix.bulb_manager.bulb.domain.aggregate;

public record InitialBulb(

) implements BulbAggregate {

    @Override
    public BulbId aggregateId() {
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
