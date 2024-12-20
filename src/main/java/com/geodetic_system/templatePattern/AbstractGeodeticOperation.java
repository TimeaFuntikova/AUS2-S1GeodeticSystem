package com.geodetic_system.templatePattern;

import com.geodetic_system.cmopositeCoordinateValidator.CompositeCoordinateValidator;
import com.geodetic_system.geodeticObjects.GeodeticObject;
import com.geodetic_system.model.Model;
import com.geodetic_system.visual.GUIObject;

/**
 * Abstract template class for Geodetic Operations
 * This class is an abstract template class for Geodetic Operations illustrating the Template Design Pattern flow.
 * It contains the template method execute() which defines the flow of the operation.
 */
public abstract class AbstractGeodeticOperation<R> {
    protected final Model model;
    private final CompositeCoordinateValidator coordinateValidator;

    /**
     * Constructor
     * @param model the model
     * @param coordinateValidator the coordinate validator
     */
    protected AbstractGeodeticOperation(Model model, CompositeCoordinateValidator coordinateValidator) {
        this.model = model;
        this.coordinateValidator = coordinateValidator;
    }

    /**
     * Execute the Geodetic Operation
     * @param guiObject the GUIObject to execute the operation on
     * @return the result of the operation
     */
    public final R execute(GUIObject guiObject) {
        if (!validate(guiObject)) {
            return handleInvalidInput();
        }

        GeodeticObject geodeticObject = model.createGeodeticObject(guiObject);
        if (geodeticObject == null) {
            return handleInvalidInput();
        }

        return performOperation(geodeticObject, guiObject);
    }

    /**
     * Validate the GUIObject
     * @param guiObject the GUIObject to validate
     * @return true if the GUIObject is valid
     */
    private boolean validate(GUIObject guiObject) {
        return guiObject != null && coordinateValidator.validate(guiObject);
    }

    /**
     * Handle invalid input
     * @return the result of the operation
     */
    protected abstract R handleInvalidInput();

    /**
     * Perform the operation
     * @param geodeticObject the GeodeticObject to perform the operation on
     * @param guiObject the GUIObject to perform the operation on
     * @return the result of the operation
     */
    protected abstract R performOperation(GeodeticObject geodeticObject, GUIObject guiObject);
}

