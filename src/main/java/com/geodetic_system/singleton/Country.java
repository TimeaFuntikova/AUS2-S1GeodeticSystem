package com.geodetic_system.singleton;

public class Country {
    private final double minX;
    private final double maxY;
    private final double maxX;
    private final double minY;

    public Country(double minX, double maxY, double maxX, double minY) {
        this.minX = minX;
        this.maxY = maxY;
        this.maxX = maxX;
        this.minY = minY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }
}
