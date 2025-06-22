package fr.depix.bulb_manager.bulb.infra;

import fr.depix.bulb_manager.bulb.domain.aggregate.BulbAggregate;
import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;
import fr.depix.bulb_manager.bulb.domain.spi.BulbRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InMemoryRepository implements BulbRepository {

    @Nullable
    private BulbAggregate bulb;

    @Override
    public Optional<BulbAggregate> get() {
        return Optional.ofNullable(bulb);
    }

    public void reset() {
        bulb = null;
    }

    @Override
    public Optional<BulbAggregate> findById(BulbId id) {
        return Optional.ofNullable(bulb);
    }

    @Override
    public void save(BulbAggregate newState) {
        this.bulb = newState;
    }
}
