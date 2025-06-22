package fr.depix.bulb_manager.bulb.domain.spi;

import fr.depix.bulb_manager.bulb.domain.aggregate.BulbAggregate;
import fr.depix.bulb_manager.bulb.domain.aggregate.BulbId;
import fr.depix.bulb_manager.framework.annotation.Repository;

import java.util.Optional;

public interface BulbRepository extends Repository<BulbAggregate, BulbId> {

    Optional<BulbAggregate> get();
}
