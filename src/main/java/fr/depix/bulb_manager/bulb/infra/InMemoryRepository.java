package fr.depix.bulb_manager.bulb.infra;

import fr.depix.bulb_manager.bulb.domain.aggregate.Bulb;
import fr.depix.bulb_manager.bulb.domain.aggregate.BulbId;
import fr.depix.bulb_manager.bulb.domain.spi.BulbRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InMemoryRepository implements BulbRepository {

    @Nullable
    private Bulb bulb;

    @Override
    public void save(Bulb bulb) {
        this.bulb = bulb;
    }

    @Override
    public Optional<Bulb> get() {
        return Optional.ofNullable(bulb);
    }

    public void reset() {
        bulb = null;
    }

    @Override
    public Optional<Bulb> findById(BulbId id) {
        return Optional.ofNullable(bulb);
    }
}
