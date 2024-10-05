package com.geodetic_system;

import java.util.List;

public interface IObjectInSystem <T extends IObjectInSystem<?>> {
    int getId(); // supuisne cislo pre property alebo parcelu
    String getDescription();
    GPSPosition getTopLeft();
    GPSPosition getBottomRight();
    List<T> getRelatedObjects(); // vrati zoznam pripojenych objektov
    void addRelatedObject(T obj); // prida objekt do zoznamu pripojenych objektov
}