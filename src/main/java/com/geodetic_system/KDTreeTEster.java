package com.geodetic_system;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTreeTEster {
        private final Logger log = Logger.getLogger(KDTreeTEster.class.getName());
        private KDTree<Parcela> kdTreeParcela;
        private List<Parcela> referenceList;
        private List<Property> referenceList2; // pomocny zoznam pre porovnanie
        private Random random;
        private static int currentSupisneCislo = 100;


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

        private void insertRandomParcela() {
            int supisneCislo = currentSupisneCislo++;

            double latitude = Math.round(random.nextDouble() * 90 * 1000.0) / 1000.0;
            double longitude = Math.round(random.nextDouble() * 180 * 1000.0) / 1000.0;

            GPSPosition gpsPosition = new GPSPosition('N', latitude, 'E', longitude);
            Parcela parcela = new Parcela(supisneCislo, "Parcela " + supisneCislo, gpsPosition);

            log.info("Vkladám parcelu: " + parcela.getId() + " s GPS: " + parcela.getGpsPosition());
            kdTreeParcela.insert(parcela, 2);
            referenceList.add(parcela);
        }

       /**
         Kontrola štruktúry stromu
        */
       private void validateStructure() {
           log.info("Validujem štruktúru...");
           int treeSize = getTreeSize(kdTreeParcela.getRoot());
           if (treeSize != referenceList.size()) {
               log.severe("Nezrovnalosť veľkosti. KDTree: " + treeSize + ", referenčný zoznam: " + referenceList.size());
           }
       }

    private int getTreeSize(KDNode<Parcela> node) {
        if (node == null) {
            return 0;
        }
        return 1 + getTreeSize(node.getLeft()) + getTreeSize(node.getRight());
    }

        // Spustenie testov
        public static void main(String[] args) {
            KDTreeTEster tester = new KDTreeTEster(12345);
            tester.generateRandomOperations(10000);
            //tester.testOscillation(); // TODO:Oscilácia okolo prázdnej štruktúry
        }
    }
