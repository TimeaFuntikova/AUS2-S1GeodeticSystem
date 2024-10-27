package com.geodetic_system;

import java.util.Objects;

import static com.geodetic_system.MyComparator.TOLERANCE;

public abstract class GeodeticObject implements IObjectInSystem<GeodeticObject> {
    private static long currentId = 0;
    private final long uniqueId;

    public GeodeticObject() {
        this.uniqueId = generateUniqueId();
    }

    private synchronized long generateUniqueId() {
        return currentId++;
    }

    public long getUniqueId() {
        return uniqueId;
    }

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

    public abstract int getId();
    public abstract double getLeftTopX();
    public abstract double getRightBottomX();
    public abstract double getLeftTopY();
    public abstract double getRightBottomY();
}
