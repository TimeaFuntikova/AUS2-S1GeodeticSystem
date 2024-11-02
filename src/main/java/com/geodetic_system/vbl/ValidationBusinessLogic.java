package com.geodetic_system.vbl;

import com.geodetic_system.GeodeticObject;
import com.geodetic_system.Parcela;
import com.geodetic_system.Property;
import com.geodetic_system.model.Model;
import com.geodetic_system.visual.GUIObject;

public class ValidationBusinessLogic {
    private final Model model = new Model();

    public ValidationBusinessLogic() {
        //intentionally empty
    }

    public boolean checkAndTryToInsert(GUIObject guiObject) {
        // -- check correct paramst and stuff
        if (!this.checkTheParameters(guiObject)) return false;

        //-- ask the model to Create an object and if this object is valied
        GeodeticObject geodeticObject = this.model.createGeodeticObject(guiObject);
        if (geodeticObject == null) return false;

        //ask the model to insert.
        // if valied, tell it to insert and handle duplicities - if there are, handle then gracefully :D
        // requiere bool info about insert then return success true
        return this.model.insertGeodeticObject(geodeticObject);
    }

    public GUIObject checkAndTryToFind(GUIObject guiObject) {
        if (!this.checkTheParameters(guiObject)) return guiObject;

        GeodeticObject geodeticObject = this.model.createGeodeticObject(guiObject);
        if (geodeticObject == null) return guiObject;

        GeodeticObject foundObject = this.model.findGeodeticObject(geodeticObject);
        if (foundObject == null) return guiObject;

        String objectType;
        if (foundObject instanceof Parcela) {
            objectType = "PARCELA";
        } else if (foundObject instanceof Property) {
            objectType = "PROPERTY";
        } else {
            objectType = "UNKNOWN";
        }if (foundObject == null) return guiObject;

        String operationType = "DELETE";
        return new GUIObject(foundObject, operationType, objectType);
    }

    public boolean checkAndTryToDelete(GUIObject guiObject) {
        if (!this.checkTheParameters(guiObject)) return false;

        GeodeticObject geodeticObject = this.model.createGeodeticObject(guiObject);
        if (geodeticObject == null) return false;

        return this.model.deleteGeodeticObject(geodeticObject);
    }

    private boolean checkTheParameters(GUIObject guiObject) {
        return checkIfObjectExists(guiObject);
        //return checkIfParametersAreCorrect(guiObject);
        //return checkIfIsInsideSlovakia(guiObject);
    }

    private boolean checkIfObjectExists(GUIObject guiObject) {
        return guiObject != null;
    }

    private boolean checkIfParametersAreCorrect(GUIObject guiObject) {
        //nesmu byt zaporne
        if (guiObject.getGPSPositionTopLeftX() < 0 || guiObject.getGPSPositionTopLeftY() < 0 || guiObject.getGPSPositionBottomRightX() < 0 || guiObject.getGPSPositionBottomRightY() < 0) {
            return false;
        }

        //topleft x < bottomright x
        //topleft y > bottomright y
        if (guiObject.getGPSPositionTopLeftX() > guiObject.getGPSPositionBottomRightX() || guiObject.getGPSPositionTopLeftY() < guiObject.getGPSPositionBottomRightY()) {
            return false;
        }

        return true;
    }

    private boolean checkIfIsInsideSlovakia(GUIObject guiObject) {
        if (guiObject.getGPSPositionTopLeftX() < 16.833333 || guiObject.getGPSPositionTopLeftY() > 49.6 || guiObject.getGPSPositionBottomRightX() > 22.566667 || guiObject.getGPSPositionBottomRightY() < 47.75) {
            return false;
        }

        return true;
    }
}
