package com.geodetic_system;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        setFormattingOnConsole();

        Main mainInstance = new Main();
        mainInstance.runParcelaExample();
        mainInstance.runPropertyExample();
    }

    private static void setFormattingOnConsole() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new SimpleFormatter());
        Main.log.addHandler(consoleHandler);
        Main.log.setUseParentHandlers(false);
        Main.log.setLevel(Level.FINE);
    }

    // Parcely


    private void runParcelaExample() {
        final int POCET_DIMENZII = 2;
        GPSPosition gpsPosition1 = new GPSPosition('N', 48.02, 'E', 17.02);
        GPSPosition gpsPosition2 = new GPSPosition('N', 48.05, 'E', 17.05);
        GPSPosition gpsPosition3 = new GPSPosition('N', 48.10, 'E', 17.10);
        GPSPosition gpsPosition4 = new GPSPosition('N', 48.07, 'E', 17.07);
        GPSPosition gpsPosition5 = new GPSPosition('N', 48.15, 'E', 17.09);
        GPSPosition gpsPosition6 = new GPSPosition('N', 48.09, 'E', 17.12);

        Parcela parcela1 = new Parcela(1, "Parcela 1", gpsPosition1);
        Parcela parcela2 = new Parcela(2, "Parcela 2", gpsPosition2);
        Parcela parcela3 = new Parcela(3, "Parcela 3", gpsPosition3);
        Parcela parcela4 = new Parcela(4, "Parcela 4", gpsPosition4);
        Parcela parcela5 = new Parcela(5, "Parcela 5", gpsPosition5);
        Parcela parcela6 = new Parcela(6, "Parcela 6", gpsPosition6);

        log.info("Parcela example-------------------------------------");
        KDTree<Parcela> kdTreeParcela = new KDTree<>();
        kdTreeParcela.insert(parcela1, POCET_DIMENZII);
        kdTreeParcela.insert(parcela2, POCET_DIMENZII);
        kdTreeParcela.insert(parcela3, POCET_DIMENZII);
        kdTreeParcela.insert(parcela4, POCET_DIMENZII);
        kdTreeParcela.insert(parcela5, POCET_DIMENZII);
        kdTreeParcela.insert(parcela6, POCET_DIMENZII);

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

        Property property1 = new Property(100, "Property 1", position1);
        Property property2 = new Property(101, "Property 2", position2);
        Property property3 = new Property(102, "Property 3", position3);
        Property property4 = new Property(103, "Property 4", position4);
        Property property5 = new Property(104, "Property 5", position5);
        Property property6 = new Property(105, "Property 6", position6);
        Property property7 = new Property(106, "Property 7", position7);

        log.info("Property example-------------------------------------");
        KDTree<Property> kdTreeProperty = new KDTree<>();
        kdTreeProperty.insert(property1, POCET_DIMENZII);
        kdTreeProperty.insert(property2, POCET_DIMENZII);
        kdTreeProperty.insert(property3, POCET_DIMENZII);
        kdTreeProperty.insert(property4, POCET_DIMENZII);
        kdTreeProperty.insert(property5, POCET_DIMENZII);
        kdTreeProperty.insert(property6, POCET_DIMENZII);
        kdTreeProperty.insert(property7, POCET_DIMENZII);
    }

}
