package com.geodetic_system;

import com.geodetic_system.cmopositeCoordinateValidator.CompositeCoordinateValidator;
import com.geodetic_system.controller.Controller;
import com.geodetic_system.geodeticObjects.factory.ValidatorFactory;
import com.geodetic_system.model.Model;
import com.geodetic_system.vbl.ValidationBusinessLogic;
import com.geodetic_system.visual.GUI;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        CompositeCoordinateValidator validator = ValidatorFactory.createValidator(model);
        ValidationBusinessLogic vbl = new ValidationBusinessLogic(model, validator);
        Controller controller = new Controller(vbl);

        SwingUtilities.invokeLater(() -> new GUI(controller));
    }
}