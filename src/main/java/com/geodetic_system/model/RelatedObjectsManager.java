package com.geodetic_system.model;

import com.geodetic_system.geodeticObjects.GeodeticObject;

public class RelatedObjectsManager {

    private RelatedObjectsManager() {}
        public static boolean areIntersecting(GeodeticObject a, GeodeticObject b) {
            // Zistíme, či sa objekty neprekrývajú:
            boolean notIntersecting = (a.getRightBottomX() < b.getLeftTopX() || // a je úplne naľavo od b
                    a.getLeftTopX() > b.getRightBottomX() || // a je úplne napravo od b
                    a.getRightBottomY() > b.getLeftTopY() || // a je úplne nad b
                    a.getLeftTopY() < b.getRightBottomY());  // a je úplne pod b

            boolean intersect = !notIntersecting;

            //System.out.println("Checking intersection between: ");
            //System.out.println("Object A Top-Left: " + a.getLeftTopX() + ", " + a.getLeftTopY() + " Bottom-Right: " + a.getRightBottomX() + ", " + a.getRightBottomY());
            //System.out.println("Object B Top-Left: " + b.getLeftTopX() + ", " + b.getLeftTopY() + " Bottom-Right: " + b.getRightBottomX() + ", " + b.getRightBottomY());
            //System.out.println("Intersect result: " + intersect);

            return intersect;
    }
}
