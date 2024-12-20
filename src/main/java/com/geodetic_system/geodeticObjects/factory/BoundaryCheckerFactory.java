package com.geodetic_system.geodeticObjects.factory;

import com.geodetic_system.singleton.Country;
import com.geodetic_system.singleton.CountryCoordinates;
import com.geodetic_system.strategyBoundaries.BoundaryChecker;
import com.geodetic_system.strategyBoundaries.RectangularBoundaryChecker;
import com.geodetic_system.strategyBoundaries.SlovakiaBoundaryChecker;

public class BoundaryCheckerFactory {
    public static BoundaryChecker getCheckerForCountry(Country country) {
        if (country.equals(CountryCoordinates.SLOVAKIA.getCountry())) {
            return new SlovakiaBoundaryChecker();
        }
        return new RectangularBoundaryChecker();
    }
}
