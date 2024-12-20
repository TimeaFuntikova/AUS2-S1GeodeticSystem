package com.geodetic_system.strategyBoundaries;

import com.geodetic_system.singleton.Country;
import com.geodetic_system.visual.GUIObject;

public class BoundaryCheckerService {

    private BoundaryChecker boundaryChecker;

    public BoundaryCheckerService(BoundaryChecker boundaryChecker) {
        this.boundaryChecker = boundaryChecker;
    }

    public boolean checkIfIsInside(GUIObject guiObject, Country country) {
        return boundaryChecker.isInside(guiObject, country);
    }
}