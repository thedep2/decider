package fr.depix.bulb_manager.bulb.domain.event;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

import java.time.ZonedDateTime;
import java.util.UUID;

public record BulbSwitchedOn(
        BulbId aggregateId,
        UUID eventId,
        Long aggregateVersion,
        ZonedDateTime eventDate
) implements BulbEvent {

    @Override
    public String eventType() {
        return "BulbSwitchedOn";
    }

    @Override
    public Long eventVersion() {
        return 1L;
    }
}
