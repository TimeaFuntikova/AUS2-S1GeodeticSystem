package com.geodetic_system.strategyCoordinatesValidator;

import com.geodetic_system.visual.GUIObject;

public class ValidRectangleCoordinateValidator implements CoordinateValidator {
    @Override
    public boolean validate(GUIObject guiObject) {
        return guiObject.getGPSPositionTopLeftX() <= guiObject.getGPSPositionBottomRightX() &&
                guiObject.getGPSPositionTopLeftY() >= guiObject.getGPSPositionBottomRightY();
    }
}