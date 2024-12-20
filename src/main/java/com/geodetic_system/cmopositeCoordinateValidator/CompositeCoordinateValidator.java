package com.geodetic_system.cmopositeCoordinateValidator;

import com.geodetic_system.strategyCoordinatesValidator.CoordinateValidator;
import com.geodetic_system.visual.GUIObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Composite class for the CoordinateValidator interface
 */
public class CompositeCoordinateValidator implements CoordinateValidator {
    private final List<CoordinateValidator> validators = new ArrayList<>();

    public void addValidator(CoordinateValidator validator) {
        validators.add(validator);
    }

    /**
     * Validate the GUIObject with all the validators
     * @param guiObject the GUIObject to validate
     * @return true if all the validators return true
     */
    @Override
    public boolean validate(GUIObject guiObject) {
        for (CoordinateValidator validator : validators) {
            if (!validator.validate(guiObject)) {
                return false;
            }
        }
        return true;
    }
}
