package com.geodetic_system;

import java.util.ArrayList;
import java.util.List;

/**
 * Trieda 'Nehnutelnost' ma svoje unikatne supisne cislo, popis, GPS pozicie, ktore definuju
 * jej ohranicenie a zoznam parciel, na ktorych nehnutelnost stoji.
 */
public class Property implements IObjectInSystem<Property> {
    private final int propertyNumber;
    private final String description;
    private final GPSPosition gpsPosition; // pozicia parcely
    private final List<Parcela> relatedObjects; //bude obsahovat iba parcely (IObjectInSystem)
    private final MyComparator myComparator = new MyComparator();

    public Property(int propertyNumber, String description, GPSPosition gpsPosition) {
        this.propertyNumber = propertyNumber;
        this.description = description;
        this.gpsPosition = gpsPosition;
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
    public GPSPosition getGpsPosition() {
        return gpsPosition;
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

}