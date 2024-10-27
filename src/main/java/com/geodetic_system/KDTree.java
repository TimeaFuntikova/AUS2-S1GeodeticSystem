package com.geodetic_system;

import java.util.logging.Logger;

public class KDTree<T extends IObjectInSystem<T>> {
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
            if (result == 0) log.warning("Duplicate data or other data detected at current dimension. Moving to the next dimension.");
            current = (result < 0) ? current.getRight() : current.getLeft(); //current je mensi ako data, ideme doprava lebo nove data su vacsie

            depth++;
        }

        // Keď nájdeme miesto pre uzol, porovnáme ho s rodičom
        int currentDimension = (depth - 1) % POCET_DIMENZII;
        int compareWithParent = parent.getData().compareByDimension(data, currentDimension);

        if (compareWithParent == 0) {
            log.warning("Data with ID " + data.getId() + " already exists in the tree.");
            return false;
        }

        // Ak je rodič menší, uzol ide doprava
        if (compareWithParent < 0) {
            parent.setRight(nodeBeingInserted);
        } else {
            parent.setLeft(nodeBeingInserted);
        }

        nodeBeingInserted.setParent(parent);
        log.info("Inserted " + nodeBeingInserted.getData().getTopLeft() + ", " + nodeBeingInserted.getData().getBottomRight()+ " as a child of parent with position: " + parent.getData().getTopLeft() + ", " + parent.getData().getBottomRight());
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

    public boolean delete(T target, int numDimensions) {
        if (root == null || target == null) return false;

        KDNode<T> current = root;
        KDNode<T> parent = null;
        int depth = 0;

        while (current != null && current.getData().compareByDimension(target, depth % numDimensions) != 0) {
            parent = current;
            int currentDimension = depth % numDimensions;
            int comparison = current.getData().compareByDimension(target, currentDimension);

            if (comparison < 0) {
                current = current.getRight();
            } else {
                current = current.getLeft();
            }
            depth++;
        }

        if (current == null) {
            log.warning("Node to delete not found.");
            return false;
        }

        if (current.getLeft() == null && current.getRight() == null) {
            replaceChild(parent, current, null);
        }
        else if (current.getLeft() == null || current.getRight() == null) {
            KDNode<T> child = (current.getLeft() != null) ? current.getLeft() : current.getRight();
            replaceChild(parent, current, child);
        } else {
            int currentDim = depth % numDimensions;
            KDNode<T> replacement = findMin(current.getRight(), currentDim, numDimensions);

            if (replacement != null) {
                current.setData(replacement.getData());
                replaceChild(replacement.getParent(), replacement, replacement.getRight());
            }
        }

        return true;
    }

    private KDNode<T> findMin(KDNode<T> root, int dimension, int numDimensions) {
        KDNode<T> current = root;

        while (current != null) {
            int currentDimension = dimension % numDimensions;

            if (currentDimension == dimension) {
                if (current.getLeft() != null) {
                    current = current.getLeft();
                } else {
                    return current;
                }
            } else {
                if (current.getRight() != null) {
                    current = current.getRight();
                } else {
                    return current;
                }
            }
        }
        return null;
    }

    private void replaceChild(KDNode<T> parent, KDNode<T> current, KDNode<T> replacement) {
        if (parent == null) {
            root = replacement;
        } else if (parent.getLeft() == current) {
            parent.setLeft(replacement);
        } else {
            parent.setRight(replacement);
        }

        if (replacement != null) {
            replacement.setParent(parent);
        }
    }
}
