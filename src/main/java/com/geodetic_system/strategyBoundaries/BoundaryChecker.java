package com.geodetic_system.strategyBoundaries;

import com.geodetic_system.singleton.Country;
import com.geodetic_system.visual.GUIObject;

public interface BoundaryChecker {

    boolean isInside(GUIObject guiObject, Country country);
}
