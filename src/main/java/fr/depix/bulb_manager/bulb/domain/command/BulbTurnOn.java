package fr.depix.bulb_manager.bulb.domain.command;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

import java.time.ZonedDateTime;

public record BulbTurnOn(
        BulbId aggregateId,
        Long aggregateVersion,
        ZonedDateTime commandDate
) implements BulbCommand {

}
