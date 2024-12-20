package com.geodetic_system.visual.command;

import com.geodetic_system.controller.Controller;
import com.geodetic_system.visual.ActionType;
import com.geodetic_system.visual.GUI;
import com.geodetic_system.visual.GUIObject;

import java.util.List;

public class FindCommand implements CommandPattern {

    private final Controller controller;

    /**
     * Constructor
     * @param controller the controller
     */
    public FindCommand(Controller controller) {
        this.controller = controller;
    }
    @Override
    public void execute(GUI gui) {
        try {
        GUIObject obj = gui.getComponents().createGUIObject(ActionType.FIND);
        if (obj != null) {
            List<GUIObject> foundObjs = controller.tryToFindGUIObject(obj);
            boolean success = foundObjs != null && !foundObjs.isEmpty();
            String message = success ? "Found: " + obj : "Searching failed.";
            gui.appendOutput(message);
        }
    } catch (Exception e) {
        gui.showError("Error during searching.");
    }
}
}
