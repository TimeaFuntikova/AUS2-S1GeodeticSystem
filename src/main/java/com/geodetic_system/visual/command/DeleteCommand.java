package com.geodetic_system.visual.command;

import com.geodetic_system.controller.Controller;
import com.geodetic_system.visual.ActionType;
import com.geodetic_system.visual.GUI;
import com.geodetic_system.visual.GUIObject;

public class DeleteCommand implements CommandPattern {
    private final Controller controller;

    public DeleteCommand(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void execute(GUI gui) {
        try {
            GUIObject obj = gui.getComponents().createGUIObject(ActionType.DELETE);
            if (obj != null) {
                boolean success = controller.tryToProcess(obj);
                String message = success ? "Deleted: " + obj : "Deletion failed.";
                gui.appendOutput(message);
            }
        } catch (Exception e) {
            gui.showError("Error during deletion.");
        }
    }
}
