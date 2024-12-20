package com.geodetic_system.strategyCoordinatesValidator;

import com.geodetic_system.visual.GUIObject;

public class NonNegativeCoordinateValidator implements CoordinateValidator {
    @Override
    public boolean validate(GUIObject guiObject) {
        return guiObject.getGPSPositionTopLeftX() >= 0 &&
                guiObject.getGPSPositionTopLeftY() >= 0 &&
                guiObject.getGPSPositionBottomRightX() >= 0 &&
                guiObject.getGPSPositionBottomRightY() >= 0;
    }
}
