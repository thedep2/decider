package fr.depix.bulb_manager.bulb.domain.event;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

public record BulbCreated(BulbId bulbId) implements BulbEvent {
}
