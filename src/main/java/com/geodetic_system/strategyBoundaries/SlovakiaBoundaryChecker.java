package com.geodetic_system.strategyBoundaries;

import com.geodetic_system.singleton.Country;
import com.geodetic_system.visual.GUIObject;

public class SlovakiaBoundaryChecker implements BoundaryChecker {
    @Override
    public boolean isInside(GUIObject guiObject, Country country) {
        return new RectangularBoundaryChecker().isInside(guiObject, country);
    }
}