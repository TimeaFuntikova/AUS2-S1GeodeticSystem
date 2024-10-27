package com.geodetic_system;

import java.util.Stack;
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
        public T find(T target, int POCET_DIMENZII) {
            if (target == null) return null;

            KDNode<T> current = root;
            int depth = 0;
            Stack<KDNode<T>> stack = new Stack<>();

            //bude prechadzat strom a hladat cielovy uzol
            while (current != null || !stack.isEmpty()) {
                while (current != null) {
                    stack.push(current);
                    current = current.getLeft();
                }
                current = stack.pop();

                depth++;
                int currentDimension = depth % POCET_DIMENZII;
                if (current.getData().compareByDimension(target, currentDimension) == 0) {
                    log.info("Found node: " + current.getData().getTopLeft() + ", " + current.getData().getBottomRight());
                    return current.getData();
                }

                log.info("Current node: " + current.getData().getTopLeft() + ", " + current.getData().getBottomRight());
                current = current.getRight();
            }

            log.warning("Node with ID: " + target.getId() + " not found.");
            return null; // Uzol nebol nájdený
        }

    public boolean delete(T target, int POCET_DIMENZII) {
        if (root == null) {
            return false;
        }

        Stack<KDNode<T>> stack = new Stack<>();
        KDNode<T> current = root;
        KDNode<T> parent = null;
        int depth = 0;

        while (current != null) {
            if (current.getData().compareByDimension(target, depth % POCET_DIMENZII) == 0) {
                log.info("Deleting node with data: " + current.getData());
                break;
            }

            int currentDimension = depth % POCET_DIMENZII;
            int result = current.getData().compareByDimension(target, currentDimension);
            parent = current;
            current = (result < 0) ? current.getRight() : current.getLeft();
            depth++;
        }

        if (current == null) {
            log.warning("Delete failed. Target not found in KDTree.");
            return false;
        }

        if (current.getLeft() == null && current.getRight() == null) {
            log.info("Deleting leaf node.");
            replaceChild(parent, current, null);
        } else if (current.getLeft() == null || current.getRight() == null) {
            log.info("Deleting node with one child.");
            KDNode<T> child = (current.getLeft() != null) ? current.getLeft() : current.getRight();
            replaceChild(parent, current, child);
        } else {
            int currentDim = depth % POCET_DIMENZII;

            KDNode<T> replacementNode;
            KDNode<T> replacementParent = current;

            if (currentDim == 0) {
                replacementNode = findMaxInLeftSubtree(current.getLeft(), currentDim, replacementParent, POCET_DIMENZII, depth + 1);
            } else {
                replacementNode = findMinInRightSubtree(current.getRight(), currentDim, replacementParent, POCET_DIMENZII, depth + 1);
            }

            if (replacementNode != null) {
                if (currentDim == 0 && replacementParent.getRight() == replacementNode) {
                    replacementParent.setRight(replacementNode.getLeft());
                } else if (currentDim == 1 && replacementParent.getLeft() == replacementNode) {
                    replacementParent.setLeft(replacementNode.getRight());
                }

                current.setData(replacementNode.getData());
                log.info("Replacing with node: " + replacementNode.getData());
            }
        }

        return true;
    }

    private KDNode<T> findMaxInLeftSubtree(KDNode<T> root, int dimension, KDNode<T> parent, int POCET_DIMENZII, int currentDepth) {
        KDNode<T> maxNode = root;

        if (dimension == 0) {
            while (maxNode.getRight() != null) {
                parent = maxNode;
                maxNode = maxNode.getRight();
            }
        } else {

            return null;
        }

        return maxNode;
    }
    private KDNode<T> findMinInRightSubtree(KDNode<T> root, int dimension, KDNode<T> parent, int POCET_DIMENZII, int currentDepth) {
        KDNode<T> minNode = root;

        if (dimension == 1) {
            while (minNode.getLeft() != null) {
                parent = minNode;
                minNode = minNode.getLeft();
            }
        } else {

            return null;
        }

        return minNode;
    }

    private KDNode<T> findMinNode(KDNode<T> root, int dimension, KDNode<T> parent, int POCET_DIMENZII, int depth) {
        KDNode<T> minNode = root;
        while (minNode.getLeft() != null && dimension == depth % POCET_DIMENZII) {
            parent = minNode;
            minNode = minNode.getLeft();
        }
        return minNode;
    }

    private void replaceChild(KDNode<T> parent, KDNode<T> current, KDNode<T> replacement) {
        if (parent == null) {
            // If the parent is null, then current is the root
            root = replacement;
        } else if (parent.getLeft() == current) {
            parent.setLeft(replacement);
        } else {
            parent.setRight(replacement);
        }
    }
}
