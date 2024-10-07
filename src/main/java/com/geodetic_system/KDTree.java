package com.geodetic_system;

import java.util.logging.Logger;
public class KDTree {

    private final Logger log = Logger.getLogger(KDTree.class.getName());
    private KDNode root;

    public KDTree() {
        this.root = null;
    }

    /**
     * Metóda, ktorá vracia v tomto prípade na prvej úrovni dimenzie zemepisnú šírku,
     * na druhej zemepisnú dĺžku získanú z objektu GPSPosition.
     * @param position GPSPosition objekt
     * @param dimension dimenzia, ktorá je zadaná ako parameter
     * @return
     */
    private double getDimensionValue(GPSPosition position, int dimension) {
       // if (dimension != 2) return -1; ošetrenie prípadu, že sa užívateľ pokúsi prepísať dané dimenzie pre toto zadanie
        return switch (dimension) {
            case 0 -> position.getLatitude();
            case 1 -> position.getLongitude();
            default -> throw new IllegalArgumentException("Invalid dimension: " + dimension);
        };
    }


    //K1 - insert
    public void insert(IObjectInSystem<?> data, int POCET_DIMENZII) {
        //je tu uzol, ktory sa bude vkladat do stromu ?
        if (data == null) {
            log.warning("Data to insert is null.");
            return;
        }

        //TODO: ošetriť, či sú vstupné dáta len platného typu - užívatelsky či sa nevložilo niečo iné

        GPSPosition positionTopLeftOfInsertingObject = data.getTopLeft();
        KDNode nodeBeingInserted = new KDNode(data);
        log.info(String.format("Inserting new node with position: %s", positionTopLeftOfInsertingObject));

        //1. uplne prvy uzol sa vlozi ako koren
        if (root == null) {
            root = nodeBeingInserted;
            log.info("Inserted to root: " + positionTopLeftOfInsertingObject);
            return;
        }

        KDNode current = root;
        KDNode parent = null;
        int depth = 0; //podla hlbky sa bude striedat sirka / dlzka
        while (current != null) {
            parent = current;

            //zavolat flag - compare to na zaklade objektu (current.data.getID())
            //a porovnat s objektom, ktory sa ma vlozit (data.getID())
            int result = current.getData().compareByID(current.getData(), data);
            if (result == 0) {
                log.warning("Data with the same ID already exists in the tree.");
                return;
            }

            //d - 0/1 == sirka/dlzka - na zaklae coho sa urcuje, v tomto pripade
            int dimension = depth % POCET_DIMENZII;
            log.fine(String.format("Depth: %d, Dimension: %d", depth, dimension));

            // bude sa porovnavat na zaklade aktualnej dimenzie -- d == 0 (sirka) alebo d == 1 (dlzka)
            //ak bude sirka / dlzka mensia ako hodnota, s ktorou sa porovnava, zaradi sa novy uzol do LAVEHO
            //podstromu:
            double currentDimensionValue = getDimensionValue(current.getPosition(), dimension);
            double newDimensionValue = getDimensionValue(positionTopLeftOfInsertingObject, dimension);

            //je vacsi alebo mensi? - ak je mensi, tak sa posuvame dolava, inak doprava
            if (newDimensionValue < currentDimensionValue) {
                log.fine(String.format("Moving left from node with position: %s", current.getPosition().toString()));
                current = current.getLeft();
            } else {
                log.fine(String.format("Moving right from node with position: %s", current.getPosition().toString()));
                current = current.getRight();
            }

            depth++;
        }

        int parentDimension = (depth - 1) % POCET_DIMENZII;
        double parentDimensionValue = getDimensionValue(parent.getPosition(), parentDimension);
        double newNodeDimensionValue = getDimensionValue(positionTopLeftOfInsertingObject, parentDimension);

        if (newNodeDimensionValue < parentDimensionValue) {
            parent.setLeft(nodeBeingInserted);
            log.info(String.format("Inserted as left child of node with position: %s", parent.getPosition().toString()));
        } else {
            parent.setRight(nodeBeingInserted);
            log.info(String.format("Inserted as right child of node with position: %s", parent.getPosition().toString()));
        }

        nodeBeingInserted.setParent(parent);
        log.fine(String.format("New node parent set to: %s", parent.getPosition().toString()));
    }

    //TODO: find() - prehladavanie stromu
    //TODO: delete() - mazanie uzlov
}