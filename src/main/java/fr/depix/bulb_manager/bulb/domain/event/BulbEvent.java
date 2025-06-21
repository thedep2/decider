package fr.depix.bulb_manager.bulb.domain.event;

public sealed interface BulbEvent permits BulbSwitchedOff, BulbSwitchedOn, BulbWentOut {
}

