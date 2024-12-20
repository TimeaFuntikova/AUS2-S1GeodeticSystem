package com.geodetic_system.adapter;

import com.geodetic_system.singleton.Country;
import com.geodetic_system.strategyBoundaries.BoundaryChecker;
import com.geodetic_system.strategyCoordinatesValidator.CoordinateValidator;
import com.geodetic_system.visual.GUIObject;

/**
 * Adapter for BoundaryChecker
 * Here the BoundaryChecker is adapted to the CoordinateValidator interface
 */
public class BoundaryCheckerAdapter implements CoordinateValidator {
    private final BoundaryChecker boundaryChecker;
    private final Country country;

    public BoundaryCheckerAdapter(BoundaryChecker boundaryChecker, Country country) {
        this.boundaryChecker = boundaryChecker;
        this.country = country;
    }

    @Override
    public boolean validate(GUIObject guiObject) {
        return boundaryChecker.isInside(guiObject, country);
    }
}
