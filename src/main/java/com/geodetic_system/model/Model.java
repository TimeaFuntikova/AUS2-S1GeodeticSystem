package com.geodetic_system.model;

import com.geodetic_system.geodeticObjects.GPSPosition;
import com.geodetic_system.geodeticObjects.GeodeticObject;
import com.geodetic_system.geodeticObjects.Parcela;
import com.geodetic_system.geodeticObjects.Property;
import com.geodetic_system.structures.KDNode;
import com.geodetic_system.structures.KDTree;
import com.geodetic_system.visual.GUIObject;

import java.util.List;

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
        //empty
    }

    /**
     * Creates a GeodeticObject from a GUIObject
     * @param guiObject the object to create
     * @return the created GeodeticObject
     */
    public GeodeticObject createGeodeticObject(GUIObject guiObject) {
        return switch (guiObject.getObjectToAssign()) {
            case "PARCELA" -> createParcela(guiObject);
            case "PROPERTY" -> createProperty(guiObject);
            default -> null;
        };
    }

    /**
     * Inserts a GeodeticObject into the database
     * @param geodeticObject the object to insert
     * @return true if the object was inserted, false otherwise
     */
    public boolean insertGeodeticObject(GeodeticObject geodeticObject) {
        return this.kdTree.insert(geodeticObject, POCET_DIMENZII);
    }

    /**
     * Finds a GeodeticObject in the database
     * @param geodeticObject the object to find
     * @return the found GeodeticObject
     */
    public List<KDNode<GeodeticObject>> findGeodeticObject(GeodeticObject geodeticObject) {
        return this.kdTree.find(geodeticObject);
    }

    /**
     * Deletes a GeodeticObject from the database
     * @param geodeticObject the object to delete
     * @return true if the object was deleted, false otherwise
     */
    public boolean deleteGeodeticObject(GeodeticObject geodeticObject) {
        return this.kdTree.delete(geodeticObject, POCET_DIMENZII);
    }

    /**
     * Creates a Parcela from a GUIObject
     * @param guiObject the object to create
     * @return the created Parcela object
     */
    private GeodeticObject createParcela(GUIObject guiObject) {
        return new Parcela(guiObject.getId(), guiObject.getDescription(), createTopLeftGPSPosition(guiObject), createBottomRightGPSPosition(guiObject));
    }

    /**
     * Creates a Property from a GUIObject
     * @param guiObject the object to create
     * @return the created Property object
     */
    private GeodeticObject createProperty(GUIObject guiObject) {
        return new Property(guiObject.getId(), guiObject.getDescription(), createTopLeftGPSPosition(guiObject), createBottomRightGPSPosition(guiObject));
    }

    /**
     * Creates a top left GPSPosition from a GUIObject
     * @param guiObject the object to create
     * @return the created GPSPosition object
     */
    private GPSPosition createTopLeftGPSPosition(GUIObject guiObject) {
        return new GPSPosition('N', guiObject.getGPSPositionTopLeftX(), 'E', guiObject.getGPSPositionTopLeftY());
    }

    /**
     * Creates a bottom right GPSPosition from a GUIObject
     * @param guiObject the object to create
     * @return the created GPSPosition object
     */
    private GPSPosition createBottomRightGPSPosition(GUIObject guiObject) {
        return new GPSPosition('N', guiObject.getGPSPositionBottomRightX(), 'E', guiObject.getGPSPositionBottomRightY());
    }
}
