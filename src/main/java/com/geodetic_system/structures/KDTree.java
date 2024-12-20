package com.geodetic_system.structures;

import com.geodetic_system.geodeticObjects.IObjectInSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * Trieda 'KDTree' reprezentuje struktúru KD strom.
 * @param <T> typ objektu, ktory je ulozeny v uzle
 */
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
    /**
     * Vloženie dátového objektu do KD stromu.
     *
     * @param data           dátový objekt
     * @param POCET_DIMENZII počet dimenzií
     * @return true, ak sa podarilo vložiť dátový objekt, inak false
     */
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
            log.info("Inserted " + nodeBeingInserted.getData().getTopLeft() + ", " + nodeBeingInserted.getData().getBottomRight() + " as a left child of parent with position: " + parent.getData().getTopLeft() + ", " + parent.getData().getBottomRight());
        } else {
            parent.setRight(nodeBeingInserted);
            log.info("Inserted " + nodeBeingInserted.getData().getTopLeft() + ", " + nodeBeingInserted.getData().getBottomRight() + " as a right child of parent with position: " + parent.getData().getTopLeft() + ", " + parent.getData().getBottomRight());
        }

        nodeBeingInserted.setParent(parent);
        log.fine(String.format("New node parent set to: %s", parent.getData().getTopLeft() + ", " + parent.getData().getBottomRight()));
        return true;
    }

    /**
     * Nájdenie uzla v KD strome.
     * Aplikuje sa efektívne vyhľadávanie v KD strome za účelom nájdenia uzla s rovnakými dátami ako cieľový uzol.
     * @param target cieľový uzol
     * @return nájdený uzol, ak sa nájde, inak null
     */
    public List<KDNode<T>> find(T target) {
        if (target == null) return Collections.emptyList();

        List<KDNode<T>> foundNodes = new ArrayList<>();
        KDNode<T> current = root;

        while (current != null) {
            int comparison = current.getData().compareForFind(current.getData(), target);
            if (comparison == 0) foundNodes.add(current);
            current = (comparison < 0) ? current.getRight() : current.getLeft();
        }

        if (!foundNodes.isEmpty()) return foundNodes;
        log.warning("Node not found.");

        return Collections.emptyList();
    }

    /**
     * Vymazanie uzla z KD stromu.
     *
     * @param target         cieľový uzol
     * @param POCET_DIMENZII počet dimenzií
     * @return true, ak sa podarilo vymazať uzol, inak false
     */
    public boolean delete(T target, int POCET_DIMENZII) {
        if (root == null) return false;

        Stack<KDNode<T>> stack = new Stack<>();
        KDNode<T> currentNode = root;
        KDNode<T> parent = null;
        int depth = 0;

        while (currentNode != null && !currentNode.getData().equalsData(target)) {
            stack.push(currentNode);
            parent = currentNode;
            int currentDimension = depth % POCET_DIMENZII;

            if (currentNode.getData().compareByDimension(target, currentDimension) < 0) {
                currentNode = currentNode.getRight();
            } else {
                currentNode = currentNode.getLeft();
            }
            depth++;
        }
        if (currentNode == null) return false;
        if (currentNode.getLeft() == null
                && currentNode.getRight() == null) replaceChild(parent, currentNode, null);
        if (currentNode.getLeft() == null) replaceChild(parent, currentNode, currentNode.getRight());
        if (currentNode.getRight() == null) replaceChild(parent, currentNode, currentNode.getLeft());
        else {
            KDNode<T> replacementParent = currentNode;
            KDNode<T> replacementNode = currentNode.getRight();
            depth++;

            while (replacementNode.getLeft() != null) {
                replacementParent = replacementNode;
                replacementNode = replacementNode.getLeft();
                depth++;
            }

            KDNode<T> newReplacement = new KDNode<>(replacementNode.getData(), currentNode.getLeft(), currentNode.getRight());
            replaceChild(parent, currentNode, newReplacement);

            if (replacementParent.getLeft() == replacementNode) {
                replacementParent.setLeft(replacementNode.getRight());
            } else {
                replacementParent.setRight(replacementNode.getRight());
            }
        }

        return true;
    }

    private void replaceChild(KDNode<T> parent, KDNode<T> oldChild, KDNode<T> newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.getLeft() == oldChild) {
            parent.setLeft(newChild);
        } else {
            parent.setRight(newChild);
        }
    }

    public void printTreeStructure() { printSubTree(root, "", true, 0); }

    private void printSubTree(KDNode<T> node, String prefix, boolean isLeft, int depth) {
        if (node == null) return;

        // Print current node with depth and data information
        System.out.println(prefix + (isLeft ? "|-- " : "\\-- ") + "ID: " + node.getData().getId()
                + " (TopLeft: " + node.getData().getTopLeft()
                + ", BottomRight: " + node.getData().getBottomRight() + ")");

        // Recur on left and right children with additional indentation for clarity
        printSubTree(node.getLeft(), prefix + (isLeft ? "|   " : "    "), true, depth + 1);
        printSubTree(node.getRight(), prefix + (isLeft ? "|   " : "    "), false, depth + 1);
    }
}
