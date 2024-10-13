package com.geodetic_system;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        setFormattingOnConsole(log);

        Main mainInstance = new Main();
        mainInstance.runParcelaExample();
        mainInstance.runPropertyExample();
    }

    private static void setFormattingOnConsole(Logger log) {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new SimpleFormatter());
        log.addHandler(consoleHandler);
        log.setUseParentHandlers(false);
        log.setLevel(Level.FINE);
    }

    // Parcely
    private void runParcelaExample() {
        final int POCET_DIMENZII = 2;

        GPSPosition position1 = new GPSPosition('N', 48.150, 'E', 17.100);
        GPSPosition position2 = new GPSPosition('N', 48.100, 'E', 17.050);
        GPSPosition position3 = new GPSPosition('N', 48.200, 'E', 17.200);
        GPSPosition position4 = new GPSPosition('N', 48.050, 'E', 17.020);
        GPSPosition position5 = new GPSPosition('N', 48.120, 'E', 17.080);
        GPSPosition position6 = new GPSPosition('N', 48.180, 'E', 17.180);
        GPSPosition position7 = new GPSPosition('N', 48.250, 'E', 17.300);

        Parcela parcela1 = new Parcela(1, "Parcela 1", position1, position1);
        Parcela parcela2 = new Parcela(2, "Parcela 2", position2, position2);
        Parcela parcela3 = new Parcela(3, "Parcela 3", position3, position3);
        Parcela parcela4 = new Parcela(4, "Parcela 4", position4, position4);
        Parcela parcela5 = new Parcela(5, "Parcela 5", position5, position5);
        Parcela parcela6 = new Parcela(6, "Parcela 6", position6, position6);
        Parcela parcela7 = new Parcela(7, "Parcela 7", position7, position7);

        KDTree<Parcela, Property> kdTreeParcela = new KDTree<>();
        kdTreeParcela.insert(parcela1, POCET_DIMENZII);
        kdTreeParcela.insert(parcela2, POCET_DIMENZII);
        kdTreeParcela.insert(parcela3, POCET_DIMENZII);
        kdTreeParcela.insert(parcela4, POCET_DIMENZII);
        kdTreeParcela.insert(parcela5, POCET_DIMENZII);
        kdTreeParcela.insert(parcela6, POCET_DIMENZII);
        kdTreeParcela.insert(parcela7, POCET_DIMENZII);

        //printTree(kdTreeParcela.getRoot());
    }

    private void runPropertyExample() {
        final int POCET_DIMENZII = 2;

        GPSPosition position1 = new GPSPosition('N', 48.151, 'E', 17.101);
        GPSPosition position2 = new GPSPosition('N', 48.201, 'E', 17.201);
        GPSPosition position3 = new GPSPosition('N', 48.251, 'E', 17.301);
        GPSPosition position4 = new GPSPosition('N', 48.131, 'E', 17.111);
        GPSPosition position5 = new GPSPosition('N', 48.221, 'E', 17.211);
        GPSPosition position6 = new GPSPosition('N', 48.181, 'E', 17.181);
        GPSPosition position7 = new GPSPosition('N', 48.271, 'E', 17.311);

        Property property1 = new Property(100, "Property 1", position1, position1);
        Property property2 = new Property(101, "Property 2", position2, position2);
        Property property3 = new Property(102, "Property 3", position3, position3);
        Property property4 = new Property(103, "Property 4", position4, position4);
        Property property5 = new Property(104, "Property 5", position5, position5);
        Property property6 = new Property(105, "Property 6", position6, position6);
        Property property7 = new Property(106, "Property 7", position7, position7);

        KDTree<Property, Parcela> kdTreeProperty = new KDTree<>();
        kdTreeProperty.insert(property1, POCET_DIMENZII);
        kdTreeProperty.insert(property2, POCET_DIMENZII);
        kdTreeProperty.insert(property3, POCET_DIMENZII);
        kdTreeProperty.insert(property4, POCET_DIMENZII);
        kdTreeProperty.insert(property5, POCET_DIMENZII);
        kdTreeProperty.insert(property6, POCET_DIMENZII);
        kdTreeProperty.insert(property7, POCET_DIMENZII);

        //printTree(kdTreeProperty.getRoot());
    }

    private <T extends IObjectInSystem<T, R>, R> void printTree(KDNode<T, R> root) {
        List<List<String>> levels = new ArrayList<>();
        buildTreeLevels(root, 0, levels);
        printCenteredTree(levels);
    }

    private <T extends IObjectInSystem<T, R>, R> void buildTreeLevels(KDNode<T, ?> node, int level, List<List<String>> levels) {
        if (node == null) return;
        if (levels.size() <= level) levels.add(new ArrayList<>());
        levels.get(level).add("(" + node.getData().getTopLeft().getLatitude() + ", " + node.getData().getTopLeft().getLongitude() + ")");
        buildTreeLevels(node.getLeft(), level + 1, levels);
        buildTreeLevels(node.getRight(), level + 1, levels);
    }

    private void printCenteredTree(List<List<String>> levels) {
        int maxLevel = levels.size();
        for (int i = 0; i < levels.size(); i++) {
            int levelWidth = (int) Math.pow(2, maxLevel - i - 1);
            String line = String.join(" ".repeat(levelWidth), levels.get(i));
            System.out.println(" ".repeat(levelWidth) + line);
        }
    }
}
