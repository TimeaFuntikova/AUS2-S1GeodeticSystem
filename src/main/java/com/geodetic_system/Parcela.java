package com.geodetic_system;

import java.util.ArrayList;

import java.util.List;

/**
 * Trieda 'Parcela' ma svoje unikatne cislo, popis a zoznam nehnutelnosti, ktore sa na nej nachadzaju
 * a rovnako aj zoznam nehnutelnosti, ktore sa na danej parcele nachadzaju.
 */
public class Parcela implements IObjectInSystem<Parcela> {
    private final int parcelaNumber; //(cislo parcely)
    private final String description;
    private final GPSPosition gpsPosition; // pozicia parcely
    private final List<Property> relatedObjects; // bude obsahovat IBA nehnutelnosti (IObjectInSystem == Property)
    private final MyComparator myComparator = new MyComparator();

    public Parcela(int parcelaNumber, String description, GPSPosition gpsPosition) {
        this.parcelaNumber = parcelaNumber;
        this.description = description;
        this.gpsPosition = gpsPosition;
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
    public GPSPosition getGpsPosition() {
        return gpsPosition;
    }

    @Override
    public int compareByDimension(Parcela other, int POCET_DIMENZII, int depth) {
        this.myComparator.setDepth(depth);
        return this.myComparator.compare(this, other, POCET_DIMENZII);
    }

    public void addRelatedObject(Property obj) {
       this.relatedObjects.add(obj);
    }

    public List<Property> getRelatedProperties() {
        return this.relatedObjects;
    }
}
