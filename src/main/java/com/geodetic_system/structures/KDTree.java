package com.geodetic_system.structures;

import com.geodetic_system.geodeticObjects.IObjectInSystem;
import java.util.logging.Logger;

public class KDTree<T extends IObjectInSystem<T>>  {
    private final Logger log = Logger.getLogger(KDTree.class.getName());
    private KDNode<T> root;

    public KDTree() {
        this.root = null;
    }

    public KDNode<T> getRoot() {
        return root;
    }

    //K1 - insert
    public boolean insert(T data, int POCET_DIMENZII) {

        if (data == null) {
            log.warning("Data to insert is null.");
            return false;
        }

        KDNode<T> nodeBeingInserted = new KDNode<>(data);

        if (root == null) {
            root = nodeBeingInserted;
            log.info("Inserted as root: " + root.getData().getTopLeft() + ", " + root.getData().getBottomRight());
            return true;
        }

        KDNode<T> current = root;
        KDNode<T> parent = null;
        int depth = 0;

        while (current != null) {
            parent = current;
            int currentDimension = depth % POCET_DIMENZII;

            //skontroluj, ci sa nepretina s inymi parcelami/nehnutelnostami
            if (data.areIntersecting(current.getData(), data)) {
                log.warning("Data intersects with another data in the tree.");
                return false;
            }

            //ak je iného typu, current získa dátový typ, ktorý nemusí sedieť. - preto T a nie Object
            //zavolat flag - compare to na zaklade objektu a porovnat s objektom, ktory sa ma vlozit (data.getID())
            //d - 0/1 == sirka/dlzka - na zaklae coho sa urcuje, v tomto pripade
            //ak bude sirka / dlzka mensia ako hodnota, s ktorou sa porovnava, zaradi sa novy uzol do LAVEHO podstromu:

            int result = current.getData().compareByDimension(data, currentDimension);
            if (result == 0) {
                log.info("Duplicate key detected at current dimension. Duplication not allowed.");
                return false;
            } else {
                current = (result < 0) ? current.getRight() : current.getLeft();
            }
            depth++;
        }

        // porovna sa s rodicom a podla toho sa prida do laveho alebo praveho podstromu
        int currentDimension = (depth - 1) % POCET_DIMENZII;
        int compareWithParent = parent.getData().compareByDimension(data, currentDimension);

        if (compareWithParent == 0 || compareWithParent > 0) {
            parent.setLeft(nodeBeingInserted);
        } else {
            parent.setRight(nodeBeingInserted);
        }

        nodeBeingInserted.setParent(parent);

        //tu je natvrdo vypisana pozicia, ale iba kvoli logom zatial, aby sa vedelo, ze sa vlozilo - pojde prec.
        log.info("Inserted " + nodeBeingInserted.getData().getTopLeft() + ", " + nodeBeingInserted.getData().getBottomRight() + " as a child of parent with position: " + parent.getData().getTopLeft() + ", " + parent.getData().getBottomRight());
        log.fine(String.format("New node parent set to: %s", parent.getData().getTopLeft() + ", " + parent.getData().getBottomRight()));

        return true;
    }

    // K2 - find
    public T find(T target, int numDimensions) {
        if (target == null) return null;

        KDNode<T> current = root;
        int depth = 0;

        while (current != null) {
            int currentDimension = depth % numDimensions;

            if (current.getData().compareByDimension(target, currentDimension) == 0) {
                log.info("Found node: " + current.getData());
                return current.getData();
            }

            int comparison = current.getData().compareByDimension(target, currentDimension);
            if (comparison < 0) {
                current = current.getRight();
            } else {
                current = current.getLeft();
            }
            depth++;
        }

        log.warning("Node not found.");
        return null;
    }

    /**
     * Vymazanie uzla z KD stromu.
     *
     * @param target         cieľový uzol
     * @param POCET_DIMENZII počet dimenzií
     * @return true, ak sa podarilo vymazať uzol, inak false
     */
    public boolean delete(T target, int POCET_DIMENZII) {
        if (root == null || target == null) return false;

        KDNode<T> current = root;
        KDNode<T> parent = null;
        int depth = 0;

        // Krok 1: Nájdeme uzol, ktorý chceme vymazať
        while (current != null && !current.getData().equals(target)) {
            parent = current;
            int currentDimension = depth % POCET_DIMENZII;
            if (current.getData().compareByDimension(target, currentDimension) == 0) {
                this.deleteNode(current, parent, depth, POCET_DIMENZII);
            }
            if (current.getData().compareByDimension(target, currentDimension ) < 0) {
                current = current.getRight();
            } else {
                current = current.getLeft();
            }
            depth++;
        }

        // Ak uzol neexistuje, vrátime false
        if (current == null) {
            return false;
        }

        // Krok 2: Vymažeme uzol
        return deleteNode(current, parent, depth - 1, POCET_DIMENZII);
    }

    private boolean deleteNode(KDNode<T> nodeToDelete, KDNode<T> parent, int depth, int POCET_DIMENZII) {
        KDNode<T> replacementNode;
        KDNode<T> replacementParent;
        int dimension = depth % POCET_DIMENZII;

        // Ak má uzol pravý podstrom, nájdeme minimálny uzol v tejto dimenzii v pravom podstrome
        if (nodeToDelete.getRight() != null) {
            replacementNode = nodeToDelete.getRight();
            replacementParent = nodeToDelete;

            while (replacementNode.getLeft() != null) {
                replacementParent = replacementNode;
                replacementNode = replacementNode.getLeft();
            }

            // Skopírujeme dáta z náhradného uzla do uzla na odstránenie
            nodeToDelete.setData(replacementNode.getData());

            // Vymažeme náhradný uzol
            if (replacementParent.getLeft() == replacementNode) {
                replacementParent.setLeft(replacementNode.getRight());
            } else {
                replacementParent.setRight(replacementNode.getRight());
            }
        }
        // Ak má uzol len ľavý podstrom
        else if (nodeToDelete.getLeft() != null) {
            replacementNode = nodeToDelete.getLeft();
            replacementParent = nodeToDelete;

            while (replacementNode.getRight() != null) {
                replacementParent = replacementNode;
                replacementNode = replacementNode.getRight();
            }

            // Skopírujeme dáta z náhradného uzla do uzla na odstránenie
            nodeToDelete.setData(replacementNode.getData());

            // Vymažeme náhradný uzol
            if (replacementParent.getRight() == replacementNode) {
                replacementParent.setRight(replacementNode.getLeft());
            } else {
                replacementParent.setLeft(replacementNode.getLeft());
            }
        }
        // Ak uzol nemá žiadnych potomkov
        else {
            if (parent == null) {
                // Ak je uzol koreňom stromu a nemá potomkov
                root = null;
            } else if (parent.getLeft() == nodeToDelete) {
                parent.setLeft(null);
            } else {
                parent.setRight(null);
            }
        }
        return true;
    }
}
