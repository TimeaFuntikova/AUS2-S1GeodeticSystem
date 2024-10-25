package com.geodetic_system;

import java.util.ArrayList;
import java.util.List;

/**
 * Trieda 'Nehnutelnost' ma svoje unikatne supisne cislo, popis, GPS pozicie, ktore definuju
 * jej ohranicenie a zoznam parciel, na ktorych nehnutelnost stoji.
 */
public class Property implements IObjectInSystem<Property>, IWrapperForObject<Property> {
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
    public GPSPosition getTopLeft() {
        return this.topLeft;
    }

    @Override
    public int compareByDimension(Property other, int POCET_DIMENZII, int depth) {
        this.myComparator.setDepth(depth);
        return this.myComparator.compare(this, other, POCET_DIMENZII);
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
    public boolean dontTheyIntersect(Property cur, Property other) {
        return RelatedObjectsManager.dontTheyIntersect(cur, other);
    }
}