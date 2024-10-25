package com.geodetic_system;

import java.util.ArrayList;

import java.util.List;
import java.util.logging.Logger;

/**
 * Trieda 'Parcela' ma svoje unikatne cislo, popis a zoznam nehnutelnosti, ktore sa na nej nachadzaju
 * a rovnako aj zoznam nehnutelnosti, ktore sa na danej parcele nachadzaju.
 */
public class Parcela implements IObjectInSystem<Parcela>, IWrapperForObject<Parcela> {
    private final int parcelaNumber; //(cislo parcely)
    private final String description;
    private final GPSPosition topLeft; // pozicia parcely
    private final GPSPosition bottomRight; // pozicia parcely
    private final List<Property> relatedObjects; // bude obsahovat IBA nehnutelnosti (IObjectInSystem == Property)
    private final MyComparator myComparator = new MyComparator();

    private static final Logger log = Logger.getLogger(Parcela.class.getName());


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
        return this.topLeft;
    }

    @Override
    public int compareByDimension(Parcela other, int POCET_DIMENZII, int depth) {
        this.myComparator.setDepth(depth);
        return this.myComparator.compare(this, other, POCET_DIMENZII);
    }

    public void addRelatedObject(Property obj) {
       this.relatedObjects.add(obj);
       log.info("Pridavam nehnutelnost: " + obj.getId() + " na parcelu: " + this.getId());
    }

    public List<Property> getRelatedProperties() {
        return this.relatedObjects;
    }

    @Override
    public GPSPosition getBottomRight() {
        return this.bottomRight;
    }

    @Override
    public double getLeftTopX() {
        return this.topLeft.getLongitude();
    }

    @Override
    public double getLeftTopY() {
        return this.topLeft.getLatitude();
    }

    @Override
    public double getRightBottomX() {
        return this.bottomRight.getLongitude();
    }

    @Override
    public double getRightBottomY() {
        return this.bottomRight.getLatitude();
    }

    @Override
    public boolean dontTheyIntersect(Parcela cur, Parcela other) {
        return RelatedObjectsManager.dontTheyIntersect(cur, other);
    }
}
