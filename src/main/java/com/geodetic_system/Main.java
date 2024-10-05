package com.geodetic_system;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    public static void main(String[] args) {
        Logger log = Logger.getLogger(KDTree.class.getName());
        setFormattingOnConsole(log);

        KDTree kdTree = new KDTree();

        GPSPosition position1 = new GPSPosition('N', 48.150, 'E', 17.100);
        GPSPosition position2 = new GPSPosition('N', 48.151, 'E', 17.101);

        Parcela parcel1 = new Parcela(1, "Parcel 1", position1, new GPSPosition('N', 48.155, 'E', 17.105));
        Property property1 = new Property(100, "Property 1", position2, new GPSPosition('N', 48.154, 'E', 17.104));

        kdTree.insert(parcel1.getTopLeft(), parcel1);
        kdTree.insert(property1.getTopLeft(), property1);
    }

    private static void setFormattingOnConsole(Logger log) {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new SimpleFormatter());
        log.addHandler(consoleHandler);
        log.setUseParentHandlers(false);
        log.setLevel(Level.FINE);
    }
}
