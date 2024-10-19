package com.geodetic_system;

public interface IObjectInSystem<T> {
    int getId(); // supuisne cislo pre property alebo parcelu
    String getDescription();
    GPSPosition getGpsPosition();
    int compareByDimension(T other, int POCET_DIMENZII, int depth);
}