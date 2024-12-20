package com.geodetic_system.tester;

import com.geodetic_system.geodeticObjects.GPSPosition;
import com.geodetic_system.geodeticObjects.GeodeticObject;
import com.geodetic_system.geodeticObjects.Parcela;
import com.geodetic_system.geodeticObjects.Property;
import com.geodetic_system.structures.KDNode;
import com.geodetic_system.structures.KDTree;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class KDTreeTEster {
    private final Logger log = Logger.getLogger(KDTreeTEster.class.getName());
    private final KDTree<GeodeticObject> kdTreeParcela;
    private final KDTree<GeodeticObject> kdTreeProperty;
    private final KDTree<GeodeticObject> kdTreeMixed;
    private final List<GeodeticObject> listForValidation;
    private final Random random;
    private static int currentSupisneCislo = 100;
    private static final int POCET_OPERACII = 10000;

    public KDTreeTEster(long seed) {
        this.kdTreeParcela = new KDTree<>();
        this.kdTreeProperty = new KDTree<>();
        this.kdTreeMixed = new KDTree<>();
        this.listForValidation = new ArrayList<>();
        this.random = new Random(seed);
    }

    public static void main(String[] args) {
        KDTreeTEster tester = new KDTreeTEster(47);
        tester.generateRandomOperations(POCET_OPERACII, TestCase.PROPERTY);
        //tester.testOscillationAroundEmptyStructure(TestCase.MIXED, 5000, 100);
    }

    private void performInsertForParcela(int supisneCislo, GPSPosition topLeft, GPSPosition bottomRight, boolean isMixed) {
        Parcela parcela = createParcela(supisneCislo, topLeft, bottomRight);
        KDTree<GeodeticObject> targetTree = isMixed ? kdTreeMixed : kdTreeParcela;

        log.info("Attempting to insert a Parcela with ID: " + parcela.getId());
        if (!targetTree.insert(parcela, 2)) {
            log.warning("Parcela insertion failed. Parcela overlaps or duplicates an existing one in tree.");
        } else {
            listForValidation.add(parcela);
            log.info("Successfully inserted Parcela: " + parcela.getId());
        }
    }

    private void performInsertForProperty(int supisneCislo, GPSPosition topLeft, GPSPosition bottomRight, boolean isMixed) {
        Property property = createProperty(supisneCislo, topLeft, bottomRight);
        KDTree<GeodeticObject> targetTree = isMixed ? kdTreeMixed : kdTreeProperty;

        log.info("Attempting to insert a Property with ID: " + property.getId());
        if (!targetTree.insert(property, 2)) {
            log.warning("Property insertion failed. Property overlaps or duplicates an existing one in tree.");
        } else {
            listForValidation.add(property);
            log.info("Successfully inserted Property: " + property.getId());
        }
    }

    private void performInsertForMixed(int supisneCislo, GPSPosition topLeft, GPSPosition bottomRight) {
        GeodeticObject objectToInsert;

        if (random.nextBoolean()) {
            objectToInsert = createParcela(supisneCislo, topLeft, bottomRight);
        } else {
            objectToInsert = createProperty(supisneCislo, topLeft, bottomRight);
        }

        String objectType = (objectToInsert instanceof Parcela) ? "Parcela" : "Property";

        log.info(String.format("Attempting to insert a %s with ID: %d at TopLeft: %s, BottomRight: %s",
                objectType, objectToInsert.getId(), objectToInsert.getTopLeft(), objectToInsert.getBottomRight()));
        if (kdTreeMixed.insert(objectToInsert, 2)) {
            listForValidation.add(objectToInsert);
            log.info("Successfully inserted " + objectType + " into mixed tree: " + objectToInsert.getId());
        } else {
            log.warning(objectType + " insertion failed. " + objectType + " overlaps or duplicates an existing one in mixed tree.");
        }
    }


    private Parcela createParcela(int supisneCislo, GPSPosition topLeft, GPSPosition bottomRight) {
        return new Parcela(supisneCislo, "Parcela " + supisneCislo, topLeft, bottomRight);
    }

    private Property createProperty(int supisneCislo, GPSPosition topLeft, GPSPosition bottomRight) {
        return new Property(supisneCislo, "Property " + supisneCislo, topLeft, bottomRight);
    }

    public void generateRandomOperations(int pocetOperacii, TestCase type) {
        insertRandom(type);
        for (int i = 0; i < pocetOperacii; i++) {
            int operation = random.nextInt(100);
            if (operation < 30) {
                insertRandom(type);
            } else if (operation < 60) {
                findRandom(type);
            } else {
                deleteRandom(type);
            }

            if (!validateStructure(type)) {
                log.severe("Structure validation failed after operation " + (i + 1) + ". Terminating test.");
                return;
            }
        }
    }

    private void insertRandom(TestCase type) {
        int supisneCislo = currentSupisneCislo++;

        double topLeftLat = Math.round((40.5 + random.nextDouble() * 0.5) * 100.0) / 100.0;
        double topLeftLong = Math.round((17.0 + random.nextDouble() * 0.5) * 100.0) / 100.0;

        double bottomRightLat = Math.round((40.5 + random.nextDouble() * 0.5) * 100.0) / 100.0;
        double bottomRightLong = Math.round((17.0 + random.nextDouble() * 0.5) * 100.0) / 100.0;

        if (topLeftLat < bottomRightLat) {
            double temp = topLeftLat;
            topLeftLat = bottomRightLat;
            bottomRightLat = temp;
        }

        if (topLeftLong > bottomRightLong) {
            double temp = topLeftLong;
            topLeftLong = bottomRightLong;
            bottomRightLong = temp;
        }

        GPSPosition topLeft = new GPSPosition('N', topLeftLat, 'E', topLeftLong);
        GPSPosition bottomRight = new GPSPosition('N', bottomRightLat, 'E', bottomRightLong);

        log.info("Attempting random insert for TestCase: " + type);
        switch (type) {
            case PARCELA:
                performInsertForParcela(supisneCislo, topLeft, bottomRight, false);
                break;
            case PROPERTY:
                performInsertForProperty(supisneCislo, topLeft, bottomRight, false);
                break;
            case MIXED:
                performInsertForMixed(supisneCislo, topLeft, bottomRight);
                break;
        }
    }

    private void findRandom(TestCase type) {
        if (listForValidation.isEmpty()) {
            log.warning("Reference list is empty. Nothing to find.");
            return;
        }

        GeodeticObject randomObject = listForValidation.get(random.nextInt(listForValidation.size()));
        boolean found = false;
        KDTree<GeodeticObject> targetTree = getTargetTree(randomObject, type);

        if (targetTree != null) {
            found = targetTree.find(randomObject) != null;
        }

        log.info((randomObject instanceof Parcela ? "Parcela" : "Property") + " with ID: "
                + randomObject.getId() + (found ? " found in tree." : " not found in tree."));
    }

    private void deleteRandom(TestCase type) {
        if (listForValidation.isEmpty()) {
            log.warning("Reference list is empty. Nothing to delete.");
            return;
        }

        GeodeticObject randomObject = listForValidation.get(random.nextInt(listForValidation.size()));
        boolean deleted;
        KDTree<GeodeticObject> targetTree = getTargetTree(randomObject, type);

        if (targetTree != null) {
            log.info(String.format("Attempting to delete a %s with ID: %d at TopLeft: %s, BottomRight: %s",
                    randomObject instanceof Parcela ? "Parcela" : "Property",
                    randomObject.getId(), randomObject.getTopLeft(), randomObject.getBottomRight()));

            // Attempt to delete the object from the KD-Tree
            deleted = targetTree.delete(randomObject, 2);

            if (deleted) {
                boolean removedFromList = false;
                while (listForValidation.removeIf(obj -> areObjectsEqual(obj, randomObject))) {
                    removedFromList = true;
                }

                if (!removedFromList) {
                    log.warning("Failed to remove the exact matching entry from the reference list.");
                } else {
                    log.info("Successfully removed from the reference list.");
                }

                // Print the structure of the KD-Tree after successful deletion
                log.info("KD-Tree Structure after deletion of ID " + randomObject.getId() + ":");
                targetTree.printTreeStructure(); // Call printTreeStructure() to display the KD-Tree structure

            } else {
                log.warning("Deletion failed in KDTree.");
            }
        } else {
            log.warning("Target tree is null.");
        }

        // Log the current state of the reference list
        log.info("Current Reference List State: " + listForValidation.stream()
                .map(obj -> String.format("ID: %d, TopLeft: %s, BottomRight: %s",
                        obj.getId(), obj.getTopLeft(), obj.getBottomRight()))
                .collect(Collectors.joining(", ")));
    }


    private boolean areObjectsEqual(GeodeticObject obj1, GeodeticObject obj2) {
        if (obj1 == null || obj2 == null) return false;
        if (obj1.getClass() != obj2.getClass()) return false;
        return obj1.getTopLeft().equals(obj2.getTopLeft())
                && obj1.getBottomRight().equals(obj2.getBottomRight());
    }


    private KDTree<GeodeticObject> getTargetTree(GeodeticObject object, TestCase type) {
        if (type == TestCase.MIXED) return kdTreeMixed;
        if (object instanceof Parcela) return kdTreeParcela;
        if (object instanceof Property) return kdTreeProperty;
        return null;
    }

    //TODO: nastavoit toto iba raz
    private boolean validateStructure(TestCase type) {
        int treeSize;
        if (type == TestCase.MIXED) {
            treeSize = getTreeSize(kdTreeMixed.getRoot());
        } else if (type == TestCase.PARCELA) {
            treeSize = getTreeSize(kdTreeParcela.getRoot());
        } else {
            treeSize = getTreeSize(kdTreeProperty.getRoot());
        }

        boolean valid = treeSize == listForValidation.size();
        if (!valid) {
            log.severe("Inconsistency detected. KDTree size: " + treeSize + ", Reference list size: " + listForValidation.size());
            log.info("Reference List State: " + listForValidation.stream()
                    .map(obj -> String.format("ID: %d, TopLeft: %s, BottomRight: %s",
                            obj.getId(), obj.getTopLeft(), obj.getBottomRight()))
                    .collect(Collectors.joining(", ")));
            log.info("Tree Size: " + treeSize);
            log.info("Tree Contents: ");
            printTreeStructure();
        } else {
            log.info("Structure validated successfully for TestCase: " + type);
            log.info("Tree Size: " + treeSize);
            log.info("Reference List Size: " + listForValidation.size());
        }
        return valid;
    }

    private void printTreeStructure() {
        printSubTree(kdTreeMixed.getRoot(), "", true);
    }

    private void printSubTree(KDNode<?> node, String prefix, boolean isLeft) {
        if (node == null) return;
        log.info(prefix + (isLeft ? "|-- " : "\\-- ") + node.getData());
        printSubTree(node.getLeft(), prefix + (isLeft ? "|   " : "    "), true);
        printSubTree(node.getRight(), prefix + (isLeft ? "|   " : "    "), false);
    }


    private int getTreeSize(KDNode<?> node) {
        if (node == null) return 0;
        return 1 + getTreeSize(node.getLeft()) + getTreeSize(node.getRight());
    }


    public void testOscillationAroundEmptyStructure(TestCase type, int cycles, int maxInsertsPerCycle) {
        log.info("Starting oscillation test around empty structure with " + cycles + " cycles.");

        for (int cycle = 0; cycle < cycles; cycle++) {
            log.info("Cycle " + (cycle + 1) + " of " + cycles);

            int insertsThisCycle = random.nextInt(maxInsertsPerCycle) + 1;
            for (int i = 0; i < insertsThisCycle; i++) {
                insertRandom(type);
            }
            validateStructure(type);
            int deletionsThisCycle = Math.min(random.nextInt(insertsThisCycle / 2 + 1), listForValidation.size());
            for (int i = 0; i < deletionsThisCycle; i++) {
                deleteRandom(type);
            }
            validateStructure(type);
        }

        log.info("Oscillation test completed.");
    }
}
