package fr.depix.bulb_manager.bulb.domain.event;

public sealed interface Event permits BulbSwitchedOff, BulbSwitchedOn, BulbWentOut, NothingHappen {
}

