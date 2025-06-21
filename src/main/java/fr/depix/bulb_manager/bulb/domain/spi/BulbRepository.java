package fr.depix.bulb_manager.bulb.domain.spi;

import fr.depix.bulb_manager.bulb.domain.aggregate.Bulb;

public interface BulbRepository {

    void save(Bulb bulb);

    Bulb get();
}
