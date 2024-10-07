package com.geodetic_system;

import java.util.ArrayList;
import java.util.List;

/**
 * Trieda 'Nehnutelnost' ma svoje unikatne supisne cislo, popis, GPS pozicie, ktore definuju
 * jej ohranicenie a zoznam parciel, na ktorych nehnutelnost stoji.
 */
public class Property implements IObjectInSystem<Parcela> {
    private final int propertyNumber;
    private final String description;
    private final GPSPosition topLeft;
    private final GPSPosition bottomRight;
    private final List<Parcela> relatedObjects; // bude obsahovat iba parcely (IObjectInSystem)

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
    public GPSPosition getTopLeft() {
        return topLeft;
    }

    @Override
    public GPSPosition getBottomRight() {
        return bottomRight;
    }

    //len parcely
    @Override
    public List<Parcela> getRelatedObjects() {
        return relatedObjects;
    }

    @Override
    public void addRelatedObject(Parcela obj) {
        relatedObjects.add(obj);
    }

    @Override
    public int compareByID(Object instance, Object other) {
        return Integer.compare(this.getId(), ((Property) other).getId());
    }
}