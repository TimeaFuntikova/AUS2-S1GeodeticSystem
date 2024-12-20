package com.geodetic_system.visual.command;

import com.geodetic_system.controller.Controller;
import com.geodetic_system.visual.ActionType;
import com.geodetic_system.visual.GUI;
import com.geodetic_system.visual.GUIObject;

/**
 * Command for inserting a new object
 * This class is a command for inserting a new object into the system.
 * It implements the Command Pattern.
 */
    public class InsertCommand implements CommandPattern {
        private final Controller controller;

        /**
         * Constructor
         * @param controller the controller
         */
        public InsertCommand(Controller controller) {
            this.controller = controller;
        }

        /**
         * Execute the command
         * Creates a new object from the input fields in the GUI and tries to insert it into the system.
         * The result is displayed in the output area of the GUI.
         */
        @Override
        public void execute(GUI gui) {
            try {
                GUIObject obj = gui.getComponents().createGUIObject(ActionType.INSERT);
                if (obj != null) {
                    boolean success = controller.tryToProcess(obj);
                    String message = success ? "Inserted: " + obj : "Insertion failed.";
                    gui.appendOutput(message);
                }
            } catch (Exception e) {
                gui.showError("Error during insertion.");
            }
        }
    }

