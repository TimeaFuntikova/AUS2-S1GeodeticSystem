package com.geodetic_system.geodeticObjects.factory;

import com.geodetic_system.adapter.BoundaryCheckerAdapter;
import com.geodetic_system.cmopositeCoordinateValidator.CompositeCoordinateValidator;
import com.geodetic_system.configurationLoader.ConfigurationLoader;
import com.geodetic_system.model.Model;
import com.geodetic_system.singleton.Country;
import com.geodetic_system.strategyBoundaries.BoundaryChecker;
import com.geodetic_system.strategyCoordinatesValidator.NonNegativeCoordinateValidator;
import com.geodetic_system.strategyCoordinatesValidator.ValidRectangleCoordinateValidator;

/**
 * This class is responsible for the creation of the validator object.
 * Combines patterns like Composite, Adapter and Strategy and Singleton and Factory.
 * Composite is used in the CompositeCoordinateValidator class, Adapter is used in the BoundaryCheckerAdapter class,
 * Strategy is used in the BoundaryChecker and CoordinateValidator interfaces, Singleton is used in the Country class,
 * Factory used in the BoundaryCheckerFactory class.
 */
public class ValidatorFactory {
    /**
     * This class is a utility class and cannot be instantiated.
     */
    private ValidatorFactory() {
        throw new UnsupportedOperationException("ValidatorFactory is a utility class and cannot be instantiated.");
    }

    /**
     * Creates a validator object with all the necessary validators
     * @param model the model to be used
     * @return the created validator
     */
    public static CompositeCoordinateValidator createValidator(Model model) {
        CompositeCoordinateValidator validator = new CompositeCoordinateValidator();
        validator.addValidator(new NonNegativeCoordinateValidator());
        validator.addValidator(new ValidRectangleCoordinateValidator());

        Country targetCountry = ConfigurationLoader.getCountryFromConfig();
        BoundaryChecker boundaryChecker = BoundaryCheckerFactory.getCheckerForCountry(targetCountry);
        validator.addValidator(new BoundaryCheckerAdapter(boundaryChecker, targetCountry));

        return validator;
    }
}

