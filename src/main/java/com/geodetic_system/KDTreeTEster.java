package com.geodetic_system;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTreeTEster {
        private final Logger log = Logger.getLogger(KDTreeTEster.class.getName());
        private KDTree<Parcela, Property> kdTreeParcela;
        private List<Parcela> referenceList;
        private List<Property> referenceList2; // pomocny zoznam pre porovnanie
        private Random random;

        public KDTreeTEster(long seed) {
            this.kdTreeParcela = new KDTree<>();
            this.referenceList = new ArrayList<>();
            this.random = new Random(seed); //pre reprodukciu vysledkov je dany fixne
        }

        // 30% insert 30% find 30% delete
        public void generateRandomOperations(int pocetOperacii) {
            for (int i = 0; i < pocetOperacii; i++) {
                int operation = random.nextInt(100); //do 99
                if (operation < 30) {
                    insertRandomParcela();
                } else if (operation < 60) {
                    //findRandomParcela();
                } else {
                    //deleteRandomParcela();
                }
                validateStructure();
            }
        }

        // Vkladanie náhodnej parcely
        private void insertRandomParcela() {
            int id = random.nextInt(1000); // random id pre parcelu
            GPSPosition topLeft = new GPSPosition('N', random.nextDouble() * 90, 'E', random.nextDouble() * 180);
            GPSPosition bottomRight = new GPSPosition('N', random.nextDouble() * 90, 'E', random.nextDouble() * 180);
            Parcela parcel = new Parcela(id, "Parcel " + id, topLeft, bottomRight);

            log.info("Inserting Parcel: " + parcel.getId());
            kdTreeParcela.insert(parcel, 2);
            referenceList.add(parcel);
        }
       /**
         Kontrola štruktúry stromu
        */
        private void validateStructure() {
            //či počet prvkov v strome sedí s referenčným zoznamom
            log.info("Validating structure...");
            // int treeSize = getTreeSize(kdTreeParcela.getRoot()); //TODO: Implementácia prechádzania stromu
            //if (treeSize != referenceList.size()) {
            // log.severe("Size mismatch. KDTree size: " + treeSize + ", Reference list size: " + referenceList.size());
            //}
        }

        // Spustenie testov
        public static void main(String[] args) {
            KDTreeTEster tester = new KDTreeTEster(12345); // Seed pre reprodukciu
            tester.generateRandomOperations(10000); // Generuje 10 000 operácií
            //tester.testOscillation(); // TODO:Oscilácia okolo prázdnej štruktúry
        }
    }
