package fr.depix.bulb_manager.bulb.domain.command;

import fr.depix.bulb_manager.bulb.domain.aggregate.BulbId;

public record BulbTurnOn(
        BulbId id
) implements BulbCommand {

    @Override
    public BulbId aggregateId() {
        return id;
    }
}
