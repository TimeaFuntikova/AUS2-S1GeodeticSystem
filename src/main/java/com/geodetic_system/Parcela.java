package com.geodetic_system;

import java.util.ArrayList;
import java.util.List;

/**
 * Trieda 'Parcela' ma svoje unikatne cislo, popis a zoznam nehnutelnosti, ktore sa na nej nachadzaju
 * a rovnako aj zoznam nehnutelnosti, ktore sa na danej parcele nachadzaju.
 */
public class Parcela implements IObjectInSystem<Property> {
    private final int parcelaNumber;//(cislo parcely)
    private final String description;
    private final GPSPosition topLeft;
    private final GPSPosition bottomRight;
    private final List<Property> relatedObjects; // bude obsahovat IBA nehnutelnosti (IObjectInSystem == Property)

    public Parcela(int parcelaNumber, String description, GPSPosition topLeft, GPSPosition bottomRight) {
        this.parcelaNumber = parcelaNumber;
        this.description = description;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.relatedObjects = new ArrayList<>();
    }

    @Override
    public int getId() {
        return parcelaNumber;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public GPSPosition getTopLeft() {
        return topLeft;
    }

    @Override
    public GPSPosition getBottomRight() {
        return bottomRight;
    }

    @Override
    public List<Property> getRelatedObjects() {
        return relatedObjects;
    }

    //len nehnutelnosti
    @Override
    public void addRelatedObject(Property obj) {
        relatedObjects.add(obj);
    }
}
