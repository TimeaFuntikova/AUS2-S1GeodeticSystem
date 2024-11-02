package com.geodetic_system.model;

import com.geodetic_system.geodeticObjects.GPSPosition;
import com.geodetic_system.geodeticObjects.GeodeticObject;
import com.geodetic_system.geodeticObjects.Parcela;
import com.geodetic_system.geodeticObjects.Property;
import com.geodetic_system.structures.KDTree;
import com.geodetic_system.visual.GUIObject;

/**
 * Model class for the Geodetic System application that is responsible for creating GeodeticObjects
 * from GUIObjects and inserting them into the database.
 * Communicates with the database and ValidationBusinessLogic.
 * Able to insert, find and delete GeodeticObjects and transform them from GUIObjects.
 */
public class Model  {


    private final KDTree<GeodeticObject> kdTree = new KDTree<>();
    private final int POCET_DIMENZII = 2;

    public Model() {
    //emtpy constructor
    }


    public GeodeticObject createGeodeticObject(GUIObject guiObject) {
        return switch (guiObject.getObjectToAssign()) {
            case "PARCELA" -> createParcela(guiObject);
            case "PROPERTY" -> createProperty(guiObject);
            default -> null;
        };
    }

    public boolean insertGeodeticObject(GeodeticObject geodeticObject) {
        return this.kdTree.insert(geodeticObject, POCET_DIMENZII);
    }

    public GeodeticObject findGeodeticObject(GeodeticObject geodeticObject) {
        return this.kdTree.find(geodeticObject, POCET_DIMENZII);
    }

    public boolean deleteGeodeticObject(GeodeticObject geodeticObject) {
        return this.kdTree.delete(geodeticObject, POCET_DIMENZII);
    }

    private GeodeticObject createParcela(GUIObject guiObject) {
        return new Parcela(guiObject.getId(), guiObject.getDescription(), createTopLeftGPSPosition(guiObject), createBottomRightGPSPosition(guiObject));
    }

    private GeodeticObject createProperty(GUIObject guiObject) {
        return new Property(guiObject.getId(), guiObject.getDescription(), createTopLeftGPSPosition(guiObject), createBottomRightGPSPosition(guiObject));
    }

    private GPSPosition createTopLeftGPSPosition(GUIObject guiObject) {
        return new GPSPosition('N', guiObject.getGPSPositionTopLeftX(), 'E', guiObject.getGPSPositionTopLeftY());
    }

    private GPSPosition createBottomRightGPSPosition(GUIObject guiObject) {
        return new GPSPosition('N', guiObject.getGPSPositionBottomRightX(), 'E', guiObject.getGPSPositionBottomRightY());
    }
}
