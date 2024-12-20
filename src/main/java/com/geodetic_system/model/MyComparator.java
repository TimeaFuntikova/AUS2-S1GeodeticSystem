package com.geodetic_system.model;

import com.geodetic_system.geodeticObjects.IObjectInSystem;

/**
 * Trieda má za účel porovnať dva vstupné objekty na základe ich kľúča, ktorým je
 * v našom prípade objekt GPS pozícia.
 * Pokiaľ sa na základe ľavej hornej pozície objekty rovnajú, porovnáva sa ešte aj ďalší kľúč, ktorým je
 * druhá GPS pozícia, pravý dolný roh objektu. Až keď sa obe tieto hodnoty rovnajú, vráti sa 0.
 *
 * @param <T> typ objektu, ktorý sa porovnáva
 */
public class MyComparator<T extends IObjectInSystem<T>> {
    public static final double TOLERANCE = 0.0001;

    /**
     * Porovnáva dva objekty na základe ich GPS pozície.
     * @param a prvý objekt
     * @param b druhý objekt
     * @param currentDimension aktuálna dimenzia, podľa ktorej sa porovnáva
     * @return 0 ak sa objekty rovnajú, inak -1 alebo 1
     */
    public int compare(T a, T b, int currentDimension) {
        if (currentDimension == 0) {
            return compareWithTolerance(a.getTopLeft().getLatitude(), b.getTopLeft().getLatitude());
            //TODO: compare bottom right
        } else {
            return compareWithTolerance(a.getTopLeft().getLongitude(), b.getTopLeft().getLongitude());
        }
    }

    /**
     * Porovnáva dve hodnoty s určitou toleranciou.
     * @param value1 prvé číslo
     * @param value2 druhé číslo
     * @return 0 ak sa hodnoty rovnajú, inak -1 alebo 1
     */
    private int compareWithTolerance(double value1, double value2) {
        if (Math.abs(value1 - value2) < TOLERANCE) {
            return 0;
        } else {
            return Double.compare(value1, value2);
        }
    }

    public int compareForFind(T a, T b) {
        if (compareWithTolerance(a.getTopLeft().getLatitude(), b.getTopLeft().getLatitude()) == 0) {
            int findResult = compareWithTolerance(a.getTopLeft().getLongitude(), b.getTopLeft().getLongitude());
            return findResult == 0 ? 0 : -1;
      }
        return -1;
    }
}
