package fr.depix.bulb_manager.bulb.domain.aggregate;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Count(
        int value
) {

    public Count increment() {
        return new Count(value + 1);
    }

}
