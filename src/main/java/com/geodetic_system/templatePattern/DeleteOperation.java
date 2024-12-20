package com.geodetic_system.templatePattern;

import com.geodetic_system.cmopositeCoordinateValidator.CompositeCoordinateValidator;
import com.geodetic_system.geodeticObjects.GeodeticObject;
import com.geodetic_system.model.Model;
import com.geodetic_system.visual.GUIObject;

public class DeleteOperation extends AbstractGeodeticOperation<Boolean> {
    public DeleteOperation(Model model, CompositeCoordinateValidator coordinateValidator) {
        super(model, coordinateValidator);
    }

    @Override
    protected Boolean handleInvalidInput() {
        return false;
    }

    @Override
    protected Boolean performOperation(GeodeticObject geodeticObject, GUIObject guiObject) {
        return model.deleteGeodeticObject(geodeticObject);
    }
}