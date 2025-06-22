package fr.depix.bulb_manager.bulb.domain.spi;

import fr.depix.bulb_manager.bulb.domain.aggregate.Bulb;
import fr.depix.bulb_manager.bulb.domain.aggregate.BulbId;
import fr.depix.bulb_manager.framework.annotation.Repository;

import java.util.Optional;

public interface BulbRepository extends Repository<Bulb, BulbId> {

    void save(Bulb bulb);

    Optional<Bulb> get();
}
