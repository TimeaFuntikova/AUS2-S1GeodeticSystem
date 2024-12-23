package com.geodetic_system.geodeticObjects;

import java.util.Objects;
import static com.geodetic_system.model.MyComparator.TOLERANCE;

public abstract class GeodeticObject implements IObjectInSystem<GeodeticObject> {
    private static long currentId = 0;
    private final long uniqueId;

    public abstract double getLeftTopX();
    public abstract double getRightBottomX();
    public abstract double getLeftTopY();
    public abstract double getRightBottomY();

    GeodeticObject referenceObject = null;

    protected GeodeticObject() {this.uniqueId = generateUniqueId();}
    private synchronized long generateUniqueId() {return currentId++;}
    public long getUniqueId() {return uniqueId;}

    //TODO: referencing Objects
    public GeodeticObject getReferenceObject() {return referenceObject;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeodeticObject that = (GeodeticObject) o;

        return Math.abs(this.getLeftTopX() - that.getLeftTopX()) < TOLERANCE &&
                Math.abs(this.getLeftTopY() - that.getLeftTopY()) < TOLERANCE;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId);
    }
}
