package com.geodetic_system;

import java.util.List;

public interface IObjectInSystem <T extends IObjectInSystem<T,R>, R> {
    int getId(); // supuisne cislo pre property alebo parcelu
    String getDescription();
    GPSPosition getTopLeft();
    GPSPosition getBottomRight();
    List<R> getRelatedObjects(); // vrati zoznam pripojenych objektov
    void addRelatedObject(R obj); // prida objekt do zoznamu pripojenych objektov
    //pouižitie T = metody budu bezpecne voci typovym chybam
    int compareByID(T other);
}