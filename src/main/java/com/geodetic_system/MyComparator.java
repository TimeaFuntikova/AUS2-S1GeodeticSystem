package com.geodetic_system;

/**
 * Trieda 'MyComparator' sluzi na porovnavanie dvoch objektov podla ich suradnic.
 * Pre porovnanie sa pouziva 'depth' a 'POCET_DIMENZII' a je prisposobeny priamo zadaniu.
 * @param <T> typ objektu, ktory sa bude porovnavat
 */
public class MyComparator <T extends IObjectInSystem<T>> {

    private int depth;

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int compare(T a, T b, int POCET_DIMENZII) {
        if (depth % POCET_DIMENZII == 0) return Double.compare(a.getGpsPosition().getLatitude(), b.getGpsPosition().getLatitude());
        return Double.compare(a.getGpsPosition().getLongitude(), b.getGpsPosition().getLongitude());
    }
}
