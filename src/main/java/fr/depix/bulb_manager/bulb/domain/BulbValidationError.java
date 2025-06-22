package fr.depix.bulb_manager.bulb.domain;

import fr.depix.bulb_manager.framework.annotation.ValidationError;

public record BulbValidationError(
        String message
) implements ValidationError {
}
