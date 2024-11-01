package com.geodetic_system;

/**
 * Trieda má za účel porovnať dva vstupné objekty na základe ich kľúča, ktorým je
 * v našom prípade objekt GPS pozícia.
 * Pokiaľ sa na základe ľavej hornej pozície objekty rovnajú, porovnáva sa ešte aj ďalší kľúč, ktorým je
 * druhá GPS pozícia, pravý dolný roh objektu. Až keď sa obe tieto hodnoty rovnajú, vráti sa 0.
 *
 * @param <T>
 */
public class MyComparator<T extends IObjectInSystem<T>> {
    public static final double TOLERANCE = 0.0001;

    public int compare(T a, T b, int currentDimension) {
        if (currentDimension == 0) {
            int result = compareWithTolerance(a.getTopLeft().getLatitude(), b.getTopLeft().getLatitude());
            if (result == 0) {
                return compareWithTolerance(a.getBottomRight().getLatitude(), b.getBottomRight().getLatitude());
            } else {
                return result;
            }
        } else {
            int result =  compareWithTolerance(a.getTopLeft().getLongitude(), b.getTopLeft().getLongitude());
            if (result == 0) {
                return compareWithTolerance(a.getBottomRight().getLongitude(), b.getBottomRight().getLongitude());
            } else {
                return result;
            }
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
