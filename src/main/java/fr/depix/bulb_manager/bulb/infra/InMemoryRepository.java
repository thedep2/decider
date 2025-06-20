package fr.depix.bulb_manager.bulb.infra;

import fr.depix.bulb_manager.bulb.domain.Bulb;
import fr.depix.bulb_manager.bulb.domain.spi.BulbRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryRepository implements BulbRepository {

    private Bulb bulb;

    @Override
    public void save(Bulb bulb) {
        this.bulb = bulb;
    }

    @Override
    public Bulb get() {
        return bulb;
    }
}
