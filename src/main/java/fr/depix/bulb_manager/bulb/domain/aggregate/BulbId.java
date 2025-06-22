package fr.depix.bulb_manager.bulb.domain.aggregate;

import fr.depix.bulb_manager.framework.annotation.AggregateId;

public record BulbId(Long id) implements AggregateId {
}
