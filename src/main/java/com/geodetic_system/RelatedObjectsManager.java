package com.geodetic_system;

public class RelatedObjectsManager {

    private RelatedObjectsManager() {}

        public static <T extends IWrapperForObject<T>> boolean dontTheyIntersect(T a, T b) {
        if (a.getRightBottomX() <= b.getLeftTopX() || // Jeden objekt je naľavo od druhého
                a.getLeftTopX() >= b.getRightBottomX() || // Jeden objekt je napravo od druhého
                a.getRightBottomY() <= b.getLeftTopY() || // Jeden objekt je nad druhým
                a.getLeftTopY() >= b.getRightBottomY())   // Jeden objekt je pod druhým
        {
            return true; // Neprekrývajú sa
        }
        return false; // Prekrývajú sa
    }
}