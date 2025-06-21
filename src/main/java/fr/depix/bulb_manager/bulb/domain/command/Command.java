package fr.depix.bulb_manager.bulb.domain.command;

public sealed interface Command permits BulbTurnOff, BulbTurnOn {
}
