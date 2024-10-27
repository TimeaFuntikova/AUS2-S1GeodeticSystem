package com.geodetic_system;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Trieda 'Nehnutelnost' ma svoje unikatne supisne cislo, popis, GPS pozicie, ktore definuju
 * jej ohranicenie a zoznam parciel, na ktorych nehnutelnost stoji.
 */
public class Property extends GeodeticObject implements IWrapperForObject<Property> {
    private final int propertyNumber;
    private final String description;
    private final GPSPosition topLeft; // pozicia parcely
    private final GPSPosition bottomRight; // pozicia parcely
    private final List<Parcela> relatedObjects; //bude obsahovat iba parcely (IObjectInSystem)
    private final MyComparator myComparator = new MyComparator();

    public Property(int propertyNumber, String description, GPSPosition topLeft, GPSPosition bottomRight) {
        this.propertyNumber = propertyNumber;
        this.description = description;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.relatedObjects = new ArrayList<>();
    }

    @Override
    public int getId() {
        return propertyNumber;
    }

    @Override
    public String getDescription() {
        return description;
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
    public boolean areIntersecting(GeodeticObject a, GeodeticObject b) {
        return RelatedObjectsManager.areIntersecting(a, b);

    }

    @Override
    public GPSPosition getTopLeft() {
        return this.topLeft;
    }


    public void addRelatedObject(Parcela obj) {
        this.relatedObjects.add(obj);
    }

    public List<Parcela> getRelatedObjects() {
        return this.relatedObjects;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return this.getUniqueId() == property.getUniqueId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId());
    }


}