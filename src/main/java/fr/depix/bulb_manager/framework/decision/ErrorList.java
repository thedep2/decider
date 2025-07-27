package fr.depix.bulb_manager.framework.decision;

import fr.depix.bulb_manager.framework.annotation.Event;
import fr.depix.bulb_manager.framework.annotation.Identifier;
import fr.depix.bulb_manager.framework.annotation.ValidationError;

import java.util.List;

public record ErrorList<E extends Event<I>, VE extends ValidationError, I extends Identifier>(
        List<VE> errors
) implements Decision<E, VE, I> {

}
