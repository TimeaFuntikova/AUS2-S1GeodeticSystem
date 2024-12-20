package com.geodetic_system.templatePattern;

import com.geodetic_system.cmopositeCoordinateValidator.CompositeCoordinateValidator;
import com.geodetic_system.geodeticObjects.GeodeticObject;
import com.geodetic_system.model.Model;
import com.geodetic_system.structures.KDNode;
import com.geodetic_system.visual.GUIObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FindOperation extends AbstractGeodeticOperation<List<GUIObject>> {
    public FindOperation(Model model, CompositeCoordinateValidator coordinateValidator) {
        super(model, coordinateValidator);
    }

    @Override
    protected List<GUIObject> handleInvalidInput() {
        return Collections.emptyList();
    }

    @Override
    protected List<GUIObject> performOperation(GeodeticObject geodeticObject, GUIObject guiObject) {
        List<KDNode<GeodeticObject>> foundObjects = model.findGeodeticObject(geodeticObject);
        if (foundObjects.isEmpty()) {
            return Collections.emptyList();
        }

        List<GUIObject> guisToReturn = new ArrayList<>();
        for (KDNode<GeodeticObject> node : foundObjects) {
            GUIObject newGuiObj = new GUIObject(node.getData(), guiObject.getOperationType(), guiObject.getObjectToAssign());
            guisToReturn.add(newGuiObj);
        }

        return guisToReturn;
    }
}
