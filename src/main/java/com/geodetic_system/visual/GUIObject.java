package com.geodetic_system.visual;

import com.geodetic_system.geodeticObjects.GeodeticObject;

public class GUIObject {

    private int id = 0;
    private String description = "";
    private double GPSPositionTopLeftX = 0;
    private double GPSPositionTopLeftY = 0;
    private double GPSPositionBottomRightX = 0;
    private double GPSPositionBottomRightY = 0;
    private String operationType = OperationType.NONE.name();
    private String objectToAssign = ObjectToAssign.NONE.name();

    public GUIObject(int id, String description, double GPSPositionTopLeftX, double GPSPositionTopLeftY, double GPSPositionBottomRightX, double GPSPositionBottomRightY, String operationType, String objectToAssign) {
        this.id = id;
        this.description = description;
        this.GPSPositionTopLeftX = GPSPositionTopLeftX;
        this.GPSPositionTopLeftY = GPSPositionTopLeftY;
        this.GPSPositionBottomRightX = GPSPositionBottomRightX;
        this.GPSPositionBottomRightY = GPSPositionBottomRightY;
        this.operationType = operationType;
        this.objectToAssign = objectToAssign;
    }

    /**
     * Constructor for the GUIObject
     * @param geodeticObject the GeodeticObject to create the GUIObject from
     */
    public GUIObject(GeodeticObject geodeticObject, String operationType, String objectToAssign) {
        this.id = geodeticObject.getId();
        this.description = geodeticObject.getDescription();
        this.GPSPositionTopLeftX = geodeticObject.getLeftTopX();
        this.GPSPositionTopLeftY = geodeticObject.getLeftTopY();
        this.GPSPositionBottomRightX = geodeticObject.getRightBottomX();
        this.GPSPositionBottomRightY = geodeticObject.getRightBottomY();
        this.operationType = operationType;
        this.objectToAssign = objectToAssign;
    }

    public int getId() {
        return this.id;
    }

    public String getOperationType() {
        return this.operationType;
    }

    public double getGPSPositionTopLeftX() {
        return this.GPSPositionTopLeftX;
    }

    public double getGPSPositionTopLeftY() {
        return this.GPSPositionTopLeftY;
    }

    public double getGPSPositionBottomRightX() {
        return this.GPSPositionBottomRightX;
    }

    public double getGPSPositionBottomRightY() {
        return this.GPSPositionBottomRightY;
    }

    public String getDescription() {
        return this.description;
    }

    public String getObjectToAssign() {
        return this.objectToAssign;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return "ID: " + getId() +
                ", Type: " + getObjectToAssign() +
                ", Top-Left: (" + getGPSPositionTopLeftX() + ", " + getGPSPositionTopLeftY() + ")" +
                ", Bottom-Right: (" + getGPSPositionBottomRightX() + ", " + getGPSPositionBottomRightY() + ")";
    }

}
