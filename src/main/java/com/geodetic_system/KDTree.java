package com.geodetic_system;

import java.util.ArrayList;
import java.util.List;
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
            if (result == 0) {
                log.info("Duplicate key detected at current dimension. Inserting into left subtree.");
                current = current.getLeft();
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

    /**
     * Vymazanie uzla z KD stromu.
     * @param target cieľový uzol
     * @param POCET_DIMENZII počet dimenzií
     * @return true, ak sa podarilo vymazať uzol, inak false
     */
    public boolean delete(T target, int POCET_DIMENZII) {
        if (root == null || target == null) return false;

        //od rootu cez cely strom najdeme take uzly, ktore pot. mozu robit problem - zhodna suradnica
        List<KDNode<T>> matchingNodes = findNodesByComparison(root, target);
        if (matchingNodes.isEmpty()) {
            log.warning("No matching nodes found for deletion.");
            return false;
        }

        // uzol na zaklade porovnania uniqueId:
        KDNode<T> nodeToDelete = matchingNodes.stream()
                .filter(node -> node.getData().equals(target))
                .findFirst()
                .orElse(null);

        if (nodeToDelete == null) {
            log.warning("No node with the same unique ID found for deletion.");
            return false;
        }

        //vymazanie najdeneho uzla
        return deleteExactNode(nodeToDelete, POCET_DIMENZII);
    }

    /**
     * Vyhľadá všetky uzly, ktoré majú rovnaké dáta ako cieľový uzol.
     * @param currentNode aktuálny uzol
     * @param target cieľový uzol
     * @return zoznam uzlov, ktoré majú rovnaké dáta ako cieľový uzol
     */
    private List<KDNode<T>> findNodesByComparison(KDNode<T> currentNode, T target) {
        List<KDNode<T>> matchingNodes = new ArrayList<>();
        Stack<KDNode<T>> stack = new Stack<>();
        stack.push(currentNode);

        while (!stack.isEmpty()) {
            KDNode<T> node = stack.pop();

            // porovnanie podla dimenzie
            if (node.getData().compareByDimension(target, 0) == 0) matchingNodes.add(node);
            if (node.getLeft() != null) stack.push(node.getLeft());
            if (node.getRight() != null) stack.push(node.getRight());
        }

        return matchingNodes;
    }

    /**
     * Vymazanie konkrétneho uzla z KD stromu.
     * @param nodeToDelete uzol na vymazanie
     * @param POCET_DIMENZII počet dimenzií
     * @return true, ak sa podarilo vymazať uzol, inak false
     */
    private boolean deleteExactNode(KDNode<T> nodeToDelete, int POCET_DIMENZII) {
        if (root == null || nodeToDelete == null) return false;

        KDNode<T> parent = nodeToDelete.getParent();

        log.info("Deleting node with data: " + nodeToDelete.getData());

        // leaf uzol - len vymazat
        if (nodeToDelete.getLeft() == null && nodeToDelete.getRight() == null) {
            replaceChild(parent, nodeToDelete, null);
        } else if (nodeToDelete.getLeft() == null || nodeToDelete.getRight() == null) {
            KDNode<T> child = (nodeToDelete.getLeft() != null) ? nodeToDelete.getLeft() : nodeToDelete.getRight();
            replaceChild(parent, nodeToDelete, child);
        } else {
           //nahradny uzol:
            int currentDim = calculateDepth(nodeToDelete) % POCET_DIMENZII;

            KDNode<T> replacement;
            if (currentDim == 0) {
                // aj je aktualna dimenzia x tak hladame najvacsi uzolv v lavom podstome podla x
                replacement = findMax(nodeToDelete.getLeft(), currentDim);
            } else {
                // aj je aktualna dimenzia y tak hladame nsjmensi uzol v pravom podstome podla y
                replacement = findMin(nodeToDelete.getRight(), currentDim);
            }

            if (replacement != null) {
                nodeToDelete.setData(replacement.getData());

                //v pripade ze ma nahrada uzol nejake deti, tak ich treba znovu vlozit do stromu
                if (replacement.getLeft() != null || replacement.getRight() != null) {
                    List<KDNode<T>> nodesToReinsert = collectNodesWithSameKey(
                            replacement, currentDim, replacement.getData()
                    );

                    for (KDNode<T> node : nodesToReinsert) {
                        manualRemoveNode(node, POCET_DIMENZII);
                        manualInsertNode(node.getData(), POCET_DIMENZII);
                    }
                }

                removeReplacementNode(replacement);
            }
        }
        return true;
    }

    /**
     * Vráti hĺbku uzla v KD strome. SPočíta sa ako počet rodičovských uzlov.
     * @param node uzol
     * @return hĺbka uzla
     */
    private int calculateDepth(KDNode<T> node) {
        int depth = 0;
        while (node.getParent() != null) {
            node = node.getParent();
            depth++;
        }
        return depth;
    }

    private KDNode<T> findMin(KDNode<T> root, int dimension) {
        KDNode<T> minNode = root;
        Stack<KDNode<T>> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            KDNode<T> currentNode = stack.pop();
            if (currentNode.getData().compareByDimension(minNode.getData(), dimension) < 0) {
                minNode = currentNode;
            }
            if (currentNode.getLeft() != null) {
                stack.push(currentNode.getLeft());
            }
        }

        return minNode;
    }

    private KDNode<T> findMax(KDNode<T> root, int dimension) {
        KDNode<T> maxNode = root;
        Stack<KDNode<T>> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            KDNode<T> currentNode = stack.pop();
            if (currentNode.getData().compareByDimension(maxNode.getData(), dimension) > 0) {
                maxNode = currentNode;
            }
            if (currentNode.getRight() != null) {
                stack.push(currentNode.getRight());
            }
        }

        return maxNode;
    }

    /**
     * Odstránenie náhradného uzla z KD stromu.
     * @param replacement náhradný uzol
     */
    private void removeReplacementNode(KDNode<T> replacement) {
        KDNode<T> parent = replacement.getParent();

        if (replacement.getLeft() == null && replacement.getRight() == null) {
            replaceChild(parent, replacement, null);
        } else if (replacement.getRight() != null) {
            replaceChild(parent, replacement, replacement.getRight());
        } else {
            replaceChild(parent, replacement, replacement.getLeft());
        }
    }

    /**
     * Zozbiera všetky uzly s rovnakým kľúčom v danej dimenzii.
     * @param root koreň stromu
     * @param dimension dimenzia
     * @param keyData kľúčové dáta
     * @return zoznam uzlov s rovnakým kľúčom
     */
    private List<KDNode<T>> collectNodesWithSameKey(KDNode<T> root, int dimension, T keyData) {
        List<KDNode<T>> nodesToReinsert = new ArrayList<>();
        if (root == null) return nodesToReinsert;

        Stack<KDNode<T>> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            KDNode<T> currentNode = stack.pop();
            if (currentNode.getData().compareByDimension(keyData, dimension) == 0) {
                nodesToReinsert.add(currentNode);
            }

            if (currentNode.getLeft() != null) {
                stack.push(currentNode.getLeft());
            }
            if (currentNode.getRight() != null) {
                stack.push(currentNode.getRight());
            }
        }

        return nodesToReinsert;
    }

    private void manualRemoveNode(KDNode<T> node, int numDimensions) {
        KDNode<T> current = root;
        KDNode<T> parent = null;
        int depth = 0;

        while (current != null && !current.getData().equals(node.getData())) {
            parent = current;
            int currentDimension = depth % numDimensions;
            int comparison = current.getData().compareByDimension(node.getData(), currentDimension);

            current = (comparison < 0) ? current.getRight() : current.getLeft();
            depth++;
        }

        if (current == null) return;

        //v priade ze je list ho len odstranime
        if (current.getLeft() == null && current.getRight() == null) {
            replaceChild(parent, current, null);
        } else if (current.getLeft() == null || current.getRight() == null) {
            KDNode<T> child = (current.getLeft() != null) ? current.getLeft() : current.getRight();
            replaceChild(parent, current, child);
        } else {
            int currentDim = depth % numDimensions;
            KDNode<T> replacement = findMin(current.getRight(), currentDim);

            //ak nahradny uzol nie je null, tak ho nastavime ako data aktualneho uzla a odstranime ho
            if (replacement != null) {
                current.setData(replacement.getData());
                removeReplacementNode(replacement);
            }
        }
    }

    /**
     * Manuálne vloženie uzla do KD stromu.
     * @param data dáta uzla
     * @param POCET_DIMENZII počet dimenzií
     */
    private void manualInsertNode(T data, int POCET_DIMENZII) {
        KDNode<T> newNode = new KDNode<>(data);
        if (root == null) {
            root = newNode;
            return;
        }

        KDNode<T> current = root;
        KDNode<T> parent = null;
        int depth = 0;

        while (current != null) {
            parent = current;
            int currentDimension = depth % POCET_DIMENZII;
            int comparison = current.getData().compareByDimension(data, currentDimension);

            if (comparison < 0) {
                current = current.getRight();
            } else {
                current = current.getLeft();
            }
            depth++;
        }

        int currentDimension = (depth - 1) % POCET_DIMENZII;
        int compareWithParent = parent.getData().compareByDimension(data, currentDimension);

        if (compareWithParent > 0) {
            parent.setRight(newNode);
        } else {
            parent.setLeft(newNode);
        }
        newNode.setParent(parent);
    }

    /**
     * Nahradenie potomka rodičom.
     * @param parent rodič
     * @param current aktuálny uzol
     * @param replacement náhrada
     */
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
