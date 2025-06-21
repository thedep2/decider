package fr.depix.bulb_manager.bulb.domain.command;

public sealed interface BulbCommand permits BulbTurnOff, BulbTurnOn {
}
