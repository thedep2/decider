package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

public record Bulb(
        BulbId id,
        boolean isTurnOn,
        int count
) implements BulbAggregate {

}
