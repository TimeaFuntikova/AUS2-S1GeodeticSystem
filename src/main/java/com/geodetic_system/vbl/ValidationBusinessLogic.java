package com.geodetic_system.vbl;

import com.geodetic_system.cmopositeCoordinateValidator.CompositeCoordinateValidator;
import com.geodetic_system.model.Model;
import com.geodetic_system.templatePattern.FindOperation;
import com.geodetic_system.templatePattern.InsertOperation;
import com.geodetic_system.templatePattern.DeleteOperation;
import com.geodetic_system.visual.GUIObject;
import java.util.List;

/**
 * This class is responsible for the validation of the objects and the interaction with the model.
 * Combines patterns like Composite, Adapter and Strategy and Singleton and Factory.
 * Composite is used in the CompositeCoordinateValidator class, Adapter is used in the BoundaryCheckerAdapter class,
 * Strategy is used in the BoundaryChecker and CoordinateValidator interfaces, Singleton is used in the Country class,
 * Factory used in the BoundaryCheckerFactory class.
 */
public class ValidationBusinessLogic {
    private final Model model;
    private final CompositeCoordinateValidator coordinateValidator;

    /**
     * Constructor for the ValidationBusinessLogic class.
     * @param model the model to interact with
     * @param coordinateValidator the coordinate validator to use
     */
    public ValidationBusinessLogic(Model model, CompositeCoordinateValidator coordinateValidator) {
        this.model = model;
        this.coordinateValidator = coordinateValidator;
    }

    /**
     * Check and try to insert a GUIObject.
     * @param guiObject the GUIObject to insert
     * @return true if the insertion was successful
     */
    public boolean checkAndTryToInsert(GUIObject guiObject) {
        InsertOperation insertOperation = new InsertOperation(model, coordinateValidator);
        return insertOperation.execute(guiObject);
    }

    /**
     * Check and try to find a GUIObject.
     * @param guiObject the GUIObject to find
     * @return the list of found GUIObjects
     */
    public List<GUIObject> checkAndTryToFind(GUIObject guiObject) {
        FindOperation findOperation = new FindOperation(model, coordinateValidator);
        return findOperation.execute(guiObject);
    }

    /**
     * Check and try to delete a GUIObject.
     * @param guiObject the GUIObject to delete
     * @return true if the deletion was successful
     */
    public boolean checkAndTryToDelete(GUIObject guiObject) {
        DeleteOperation deleteOperation = new DeleteOperation(model, coordinateValidator);
        return deleteOperation.execute(guiObject);
    }
}
