package com.geodetic_system;

/**
 * Trieda 'MyComparator' sluzi na porovnavanie dvoch objektov podla ich suradnic.
 * Pre porovnanie sa pouziva 'depth' a 'POCET_DIMENZII' a je prisposobeny priamo zadaniu.
 * @param <T> typ objektu, ktory sa bude porovnavat
 */
public class MyComparator<T extends IObjectInSystem<T>> {
    public static final double TOLERANCE = 0.0001; // Example tolerance value

    public int compare(T a, T b, int currentDimension) {
        if (currentDimension == 0) {
            return compareWithTolerance(a.getTopLeft().getLatitude(), b.getTopLeft().getLatitude());
        } else {
            return compareWithTolerance(a.getTopLeft().getLongitude(), b.getTopLeft().getLongitude());
        }
    }

    private int compareWithTolerance(double value1, double value2) {
        if (Math.abs(value1 - value2) < TOLERANCE) {
            return 0;
        } else {
            return Double.compare(value1, value2);
        }
    }
}
