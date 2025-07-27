package fr.depix.bulb_manager.bulb.domain.command;

import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;

import java.time.ZonedDateTime;

public record CreateBulB(
        BulbId aggregateId,
        ZonedDateTime commandDate) implements BulbCommand {

    @Override
    public Long aggregateVersion() {
        return 0L;
    }

}
