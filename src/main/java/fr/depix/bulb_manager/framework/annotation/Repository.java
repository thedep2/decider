package fr.depix.bulb_manager.framework.annotation;

import java.util.Optional;

@org.jmolecules.ddd.annotation.Repository
public interface Repository<A extends Aggregate<I>, I extends Identifier> {

    Optional<A> findAggregateById(I id);

    void save(A newState);

}
