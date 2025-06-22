package fr.depix.bulb_manager.bulb.domain.command;

import fr.depix.bulb_manager.bulb.domain.aggregate.BulbId;
import fr.depix.bulb_manager.framework.annotation.Command;

public sealed interface BulbCommand extends Command<BulbId> permits BulbTurnOff, BulbTurnOn {
}
