package com.geodetic_system.geodeticObjects;

import com.geodetic_system.model.MyComparator;
import com.geodetic_system.model.RelatedObjectsManager;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Trieda 'Parcela' ma svoje unikatne cislo, popis a zoznam nehnutelnosti, ktore sa na nej nachadzaju
 * a rovnako aj zoznam nehnutelnosti, ktore sa na danej parcele nachadzaju.
 */
public class Parcela extends GeodeticObject {
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
    public int compareByDimension(GeodeticObject other, int currentDimension) {
        return this.myComparator.compare(this, other, currentDimension);
    }

    @Override
    public int compareForFind(GeodeticObject current, GeodeticObject other) {
        return this.myComparator.compareForFind(current, other);
    }

    @Override
    public boolean areIntersecting(GeodeticObject a, GeodeticObject b) {
        return RelatedObjectsManager.areIntersecting(a, b);
    }

    @Override
    public double getLeftTopX() {
        return this.topLeft.getLatitude();
    }

    @Override
    public double getLeftTopY() {
        return this.topLeft.getLongitude();
    }

    @Override
    public double getRightBottomX() {
        return this.bottomRight.getLatitude();
    }

    @Override
    public double getRightBottomY() {
        return this.bottomRight.getLongitude();
    }


    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId());
    }


}
