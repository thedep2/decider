package fr.depix.bulb_manager.bulb.domain.aggregate.id;

import fr.depix.bulb_manager.framework.annotation.AggregateId;

public record BulbId(Long id) implements AggregateId {
}
