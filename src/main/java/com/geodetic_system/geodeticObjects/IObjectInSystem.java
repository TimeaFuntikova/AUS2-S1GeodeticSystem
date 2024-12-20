package com.geodetic_system.geodeticObjects;

public interface IObjectInSystem<T> {
    int getId(); // supuisne cislo pre property alebo parcelu
    String getDescription();
    GPSPosition getTopLeft();
    GPSPosition getBottomRight();
    int compareByDimension(T other, int currentDimension);
    int compareForFind(T current, T other);
    boolean equalsData(T other);
    boolean areIntersecting(T a, T b);
}