package com.geodetic_system.strategyBoundaries;

import com.geodetic_system.singleton.Country;
import com.geodetic_system.visual.GUIObject;

public class RectangularBoundaryChecker implements BoundaryChecker {

    @Override
    public boolean isInside(GUIObject guiObject, Country country) {
        return (guiObject.getGPSPositionTopLeftX() >= country.getMinX()) &&
                (guiObject.getGPSPositionTopLeftY() <= country.getMaxY()) &&
                (guiObject.getGPSPositionBottomRightX() <= country.getMaxX()) &&
                (guiObject.getGPSPositionBottomRightY() >= country.getMinY());
    }

}
