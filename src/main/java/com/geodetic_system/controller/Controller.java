package com.geodetic_system.controller;

import com.geodetic_system.vbl.ValidationBusinessLogic;
import com.geodetic_system.visual.GUIObject;

/**
 * Controller class
 */
public class Controller {
    private final ValidationBusinessLogic vbl = new ValidationBusinessLogic();

    /**
     * Constructor for the Controller
     */
    public Controller() {
        //empty
    }

    /**
     * Accepts a GUIObject and tries to process it according to its operation type
     * @param guiObject the object to process
     * @return true if the object was processed, false otherwise
     */
    public boolean tryToProcess(GUIObject guiObject) {
        if (guiObject.getOperationType().equals("INSERT")) {
            return this.sendForInsertValidation(guiObject);
        }

        if (guiObject.getOperationType().equals("DELETE")) {
            return this.sendForDeleteValidation(guiObject);
        }

        return false;
    }

    /**
     * Sends the GUIObject for validation to ValidationBusinessLogic class
     * @param guiObject the object to validate
     * @return true if the object was validated, false otherwise
     */
    private boolean sendForInsertValidation(GUIObject guiObject) {
        return this.vbl.checkAndTryToInsert(guiObject);
    }

    /**
     * Tries to find the object in the database
     * @param dummyGUIObject the object to find and to be transformed into a GeodedicObject
     * @return the guiobject with data found, the same dummy object otherwise
     */
    public GUIObject tryToFindGUIObject(GUIObject dummyGUIObject) {
        if (dummyGUIObject.getOperationType().equals("FIND") || dummyGUIObject.getOperationType().equals("DELETE")) {
            return this.sendForFindValidation(dummyGUIObject);
        }
        return dummyGUIObject;
    }

    /**
     * Sends the GUIObject for validation to ValidationBusinessLogic class
     * @param guiObject the object to validate
     * @return the guiobject with data found, the same dummy object otherwise
     */
    private GUIObject sendForFindValidation(GUIObject guiObject) {
        return this.vbl.checkAndTryToFind(guiObject);
    }

    /**
     * Sends the GUIObject for validation to ValidationBusinessLogic class
     * @param guiObject the object to validate
     * @return true if the object was validated, false otherwise
     */
    private boolean sendForDeleteValidation(GUIObject guiObject) {
        return this.vbl.checkAndTryToDelete(guiObject);
    }
}
