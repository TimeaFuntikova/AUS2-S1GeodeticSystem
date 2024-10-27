package com.geodetic_system;

public interface IObjectInSystem<T> {
    int getId(); // supuisne cislo pre property alebo parcelu
    String getDescription();
    GPSPosition getTopLeft();
    GPSPosition getBottomRight();
    int compareByDimension(T other, int currentDimension);
    boolean areIntersecting(T a, T b);
}