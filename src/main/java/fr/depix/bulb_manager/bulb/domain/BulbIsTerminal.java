package fr.depix.bulb_manager.bulb.domain;

import fr.depix.bulb_manager.bulb.domain.aggregate.BulbAggregate;
import fr.depix.bulb_manager.bulb.domain.aggregate.id.BulbId;
import fr.depix.bulb_manager.framework.annotation.IsTerminal;

public interface BulbIsTerminal extends IsTerminal<BulbAggregate, BulbId> {

}
