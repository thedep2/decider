package fr.depix.bulb_manager.framework.annotation;

import java.util.Optional;

public interface Repository<A extends Aggregate, I extends AggregateId> {

    Optional<A> findById(I id);

    void save(A newState);

}
