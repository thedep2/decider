package fr.depix.bulb_manager.framework.decision;

import fr.depix.bulb_manager.framework.annotation.Event;
import fr.depix.bulb_manager.framework.annotation.ValidationError;

import java.util.List;

public record ErrorList<E extends Event, VE extends ValidationError>(
        List<VE> errors
) implements Decision<E, VE> {

}
