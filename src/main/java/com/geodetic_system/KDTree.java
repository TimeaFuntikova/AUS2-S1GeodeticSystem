package com.geodetic_system;

import java.util.logging.Logger;
public class KDTree {

    private final Logger log = Logger.getLogger(KDTree.class.getName());
    private KDNode root;

    public KDTree() {
        this.root = null;
    }

    //K1 - insert
    public void insert(GPSPosition position, IObjectInSystem<?> data) {
        KDNode nodeBeingInserted = new KDNode(position, data);
        log.info(String.format("Inserting new node with position: %s", position.toString()));

        //1. uplne prvy uzol sa vlozi ako koren
        if (root == null) {
            root = nodeBeingInserted;
            log.info("Inserted to root: " + position.toString());
            return;
        }

        KDNode current = root;
        KDNode parent = null;
        int depth = 0; //podla hlbky sa bude striedat sirka / dlzka

        //d - 0/1 == sirka/dlzka - na zaklae coho sa urcuje

        while (current != null) {
            parent = current;  // aktualny uzol do rodica
            int d = depth % 2;  // d == dimenzia (0 = latitude, 1 = longitude)
            log.fine(String.format("Depth: %d, Dimension: %s", depth, (d == 0 ? "Latitude" : "Longitude")));

            // bude sa porovnavat na zaklade aktualnej dimenzie -- d == 0 (sirka) alebo d == 1 (dlzka)
            //ak bude sirka / dlzka mensia ako hodnota, s ktorou sa porovnava, zaradi sa novy uzol do LAVEHO
            //podstromu:
            if (this.sirkaNovejPozicieJeMensiaAkoSirkaAktualnehoUzla(d, position, current) ||
                    (this.dlzkaNovejPozicieJeMensiaAkoDlzkaAktualnehoUzla(d, position, current))) {
                log.fine(String.format("Moving left from node with position: %s", current.getPosition().toString()));
                current = current.getLeft(); //budeme ziskavat referencie na synov aby sme vedeli, kam
                //ma novy uzol patrit
            } else {
                log.fine(String.format("Moving right from node with position: %s", current.getPosition().toString()));
                current = current.getRight();
            }
            depth++;
        }

        // - ak sa dostane na koniec, nasetujeme mu novu poziciu a referencie na synov (null) a rodica:
        if ((depth - 1) % 2 == 0) { //kontrolujeme predchadazuju uroven od current, preto -1, depth je pre currenta
            if (position.getLatitude() < parent.getPosition().getLatitude()) {
                parent.setLeft(nodeBeingInserted); //uzol na vkladanie budeme setovat ako referenciu prave parentovi
                log.info(String.format("Inserted as left child of node with position: %s", parent.getPosition().toString()));
            } else {
                parent.setRight(nodeBeingInserted);
                log.info(String.format("Inserted as right child of node with position: %s", parent.getPosition().toString()));
            }
        } else {
            if (position.getLongitude() < parent.getPosition().getLongitude()) {
                parent.setLeft(nodeBeingInserted);
                log.info(String.format("Inserted as left child of node with position: %s", parent.getPosition().toString()));
            } else {
                parent.setRight(nodeBeingInserted);
                log.info(String.format("Inserted as right child of node with position: %s", parent.getPosition().toString()));
            }
        }

        //pre lepsi in order a mazanie, za cenu vacsieho consumption pamate teraz
        nodeBeingInserted.setParent(parent);
        log.fine(String.format("New node parent set to: %s", parent.getPosition().toString()));
    }

    private boolean sirkaNovejPozicieJeMensiaAkoSirkaAktualnehoUzla(int d, GPSPosition position, KDNode current) {
        return d == 0 && position.getLatitude() < current.getPosition().getLatitude();
    }

    private boolean dlzkaNovejPozicieJeMensiaAkoDlzkaAktualnehoUzla(int d, GPSPosition position, KDNode current) {
        return d == 1 && position.getLongitude() < current.getPosition().getLongitude();
    }
}