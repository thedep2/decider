package fr.depix.bulb_manager.bulb.domain.command;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

public record BulbTurnOff(
        BulbId id
) implements BulbCommand {

    @Override
    public BulbId aggregateId() {
        return id;
    }
}
