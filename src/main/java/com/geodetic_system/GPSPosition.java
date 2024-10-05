package com.geodetic_system;

/**
 * Trieda 'GPSPozicia' obsahuje udaje  šírke/pozícii šírky a dlzke/pozicii dlzky.
 */
public class GPSPosition {
    private char latitudeDirection; // 'N' || 'S'
    private double latitude;
    private char longitudeDirection; // 'E' || 'W'
    private double longitude;

    public GPSPosition(char latitudeDirection, double latitude, char longitudeDirection, double longitude) {
        this.latitudeDirection = latitudeDirection;
        this.latitude = latitude;
        this.longitudeDirection = longitudeDirection;
        this.longitude = longitude;
    }

    public char getLatitudeDirection() {
        return latitudeDirection;
    }

    public double getLatitude() {
        return latitude;
    }

    public char getLongitudeDirection() {
        return longitudeDirection;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return latitudeDirection + " " + latitude + ", " + longitudeDirection + " " + longitude;
    }
}
