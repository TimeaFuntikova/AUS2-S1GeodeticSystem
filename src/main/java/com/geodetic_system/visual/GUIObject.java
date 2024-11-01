package com.geodetic_system.visual;

public class GUIObject {

    private int id = 0;
    private String description = "";
    private String GPSPositionTopLeftX = "";
    private String GPSPositionTopLeftY = "";
    private String GPSPositionBottomRightX = "";
    private String GPSPositionBottomRightY = "";
    private String operationType = OperationType.NONE.name();
    private GUIObject objectToAssign = null;

    public GUIObject(int id, String description, String GPSPositionTopLeftX, String GPSPositionTopLeftY, String GPSPositionBottomRightX, String GPSPositionBottomRightY, String operationType, GUIObject objectToAssign) {
        this.id = id;
        this.description = description;
        this.GPSPositionTopLeftX = GPSPositionTopLeftX;
        this.GPSPositionTopLeftY = GPSPositionTopLeftY;
        this.GPSPositionBottomRightX = GPSPositionBottomRightX;
        this.GPSPositionBottomRightY = GPSPositionBottomRightY;
        this.operationType = operationType;
        this.objectToAssign = objectToAssign;
    }

    public GUIObject getGUIObject() {
        return this;
    }

}
