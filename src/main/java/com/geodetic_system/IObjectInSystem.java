package com.geodetic_system;

public interface IObjectInSystem<T> {
    int getId(); // supuisne cislo pre property alebo parcelu
    String getDescription();
    GPSPosition getTopLeft();
    GPSPosition getBottomRight();
    int compareByDimension(T other, int POCET_DIMENZII, int depth);
    boolean dontTheyIntersect(T a, T b);
}