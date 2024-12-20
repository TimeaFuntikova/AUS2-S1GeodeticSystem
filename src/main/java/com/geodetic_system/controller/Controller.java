package com.geodetic_system.controller;

import com.geodetic_system.vbl.ValidationBusinessLogic;
import com.geodetic_system.visual.GUI;
import com.geodetic_system.visual.GUIObject;
import com.geodetic_system.visual.command.CommandPattern;
import com.geodetic_system.visual.command.DeleteCommand;
import com.geodetic_system.visual.command.FindCommand;
import com.geodetic_system.visual.command.InsertCommand;

import java.util.Collections;
import java.util.List;

/**
 * Controller class for the MVC pattern
 */
public class Controller {
    private final ValidationBusinessLogic vbl;
    private final GUI gui;


    public Controller(ValidationBusinessLogic vbl) {
        this.vbl = vbl;
        this.gui = null;
    }

    public void handleInsert() {
        CommandPattern insertCommand = new InsertCommand(this);
        insertCommand.execute(this.gui);
    }

    public void handleFind() {
        CommandPattern findCommand = new FindCommand(this);
        findCommand.execute(this.gui);
    }

    public void handleDelete() {
        CommandPattern deleteCommand = new DeleteCommand(this);
        deleteCommand.execute(this.gui);
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
     * Tries to find the object in the database
     * @param dummyGUIObject the object to find and to be transformed into a GeodedicObject
     * @return the guiobject with data found, the same dummy object otherwise
     */
    public List<GUIObject> tryToFindGUIObject(GUIObject dummyGUIObject) {
        if (dummyGUIObject.getOperationType().equals("FIND") || dummyGUIObject.getOperationType().equals("DELETE")) {
            return this.sendForFindValidation(dummyGUIObject);
        }

        return Collections.emptyList();
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
     * Sends the GUIObject for validation to ValidationBusinessLogic class
     * @param guiObject the object to validate
     * @return the guiobject with data found, the same dummy object otherwise
     */
    private List<GUIObject> sendForFindValidation(GUIObject guiObject) {
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
