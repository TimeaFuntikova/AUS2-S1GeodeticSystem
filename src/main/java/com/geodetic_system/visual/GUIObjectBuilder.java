package com.geodetic_system.visual;

public class GUIObjectBuilder {
    private int id;
    private String description;
    private double topLeftLat, topLeftLong, bottomRightLat, bottomRightLong;
    private String operationType, objectType;

    public GUIObjectBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public GUIObjectBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public GUIObjectBuilder setTopLeftCoordinates(double lat, double lon) {
        this.topLeftLat = lat;
        this.topLeftLong = lon;
        return this;
    }

    public GUIObjectBuilder setBottomRightCoordinates(double lat, double lon) {
        this.bottomRightLat = lat;
        this.bottomRightLong = lon;
        return this;
    }

    public GUIObjectBuilder setOperationType(String operationType) {
        this.operationType = operationType;
        return this;
    }

    public GUIObjectBuilder setObjectType(String objectType) {
        this.objectType = objectType;
        return this;
    }

    public GUIObject build() {
        return new GUIObject(id, description, topLeftLat, topLeftLong, bottomRightLat, bottomRightLong, operationType, objectType);
    }
}
