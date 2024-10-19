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
            log.info("Inserted as root: " + root.getData().getGpsPosition());
            return true;
        }

        KDNode<T> current = root;
        KDNode<T> parent = null;
        int depth = 0;

        while (current != null) {
            parent = current;
            //ak je iného typu, current získa dátový typ, ktorý nemusí sedieť. - preto T a nie Object
            //zavolat flag - compare to na zaklade objektu a porovnat s objektom, ktory sa ma vlozit (data.getID())
            //d - 0/1 == sirka/dlzka - na zaklae coho sa urcuje, v tomto pripade
            //ak bude sirka / dlzka mensia ako hodnota, s ktorou sa porovnava, zaradi sa novy uzol do LAVEHO podstromu:

            int result = current.getData().compareByDimension(data, POCET_DIMENZII, depth);
            //je vacsi alebo mensi? - ak je mensi, tak sa posuvame dolava, inak doprava

            //current je mensi ako data, ideme doprava lebo nove data su vacsie
            if (result < 0) {
                current = current.getRight();
            }

            else if (result > 0) {
                current = current.getLeft();
            }
            else {
                log.warning("Duplicate data or other data detected at current dimension. Moving to the next dimension.");
            }

            depth++;
        }

        // Keď nájdeme miesto pre uzol, porovnáme ho s rodičom
        int compareWithParent = parent.getData().compareByDimension(data, POCET_DIMENZII, depth - 1);

        // Ak je rodič menší, uzol ide doprava
        if (compareWithParent < 0) {
            parent.setRight(nodeBeingInserted);
            log.info("Inserted " + nodeBeingInserted.getData().getGpsPosition() + " as right child of parent with position: " + parent.getData().getGpsPosition());
        }  // Ak je rodič väčší, uzol ide doľava
        else if (compareWithParent > 0) {
            parent.setLeft(nodeBeingInserted);
            log.info("Inserted " + nodeBeingInserted.getData().getGpsPosition() + " as left child of parent: " + parent.getData().getGpsPosition());
        }  // Ak sú hodnoty rovnaké vo všetkých dimenziách, nevkladáme ho
        else {
            log.warning("Data with ID " + data.getId() + " already exists in the tree.");
            return false;
        }

        nodeBeingInserted.setParent(parent);
        log.fine(String.format("New node parent set to: %s", parent.getData().getGpsPosition()));

        return true;
    }

    // TODO: Implement find() and delete() methods
}