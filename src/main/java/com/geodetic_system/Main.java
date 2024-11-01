package com.geodetic_system;

import com.geodetic_system.visual.GUI;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/** old unused class - vymaze sa .
 *
 */
public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static final int POCET_DIMENZII = 2;
    private KDTree<GeodeticObject> kdTreeParcela;  // KDTree pre Parcely
    private KDTree<GeodeticObject> kdTreeProperty; // KDTree pre Nehnuteľnosti

    public static void main(String[] args) {
        setFormattingOnConsole();
        //Main mainInstance = new Main();
        // Spustenie GUI
        javax.swing.SwingUtilities.invokeLater(GUI::new);
        //mainInstance.runParcelaExample();
       // mainInstance.runPropertyExample();
    }

    public Main() {
        // Inicializácia KD stromov
        kdTreeParcela = new KDTree<>();
        kdTreeProperty = new KDTree<>();
    }

    private static void setFormattingOnConsole() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new SimpleFormatter());
        Main.log.addHandler(consoleHandler);
        Main.log.setUseParentHandlers(false);
        Main.log.setLevel(Level.FINE);
    }

    // Parcely (test prekryvu)
    private void runParcelaExample() {
        GPSPosition gpsPosition1 = new GPSPosition('N', 48.02, 'E', 17.02); // Top-left
        GPSPosition gpsPosition2 = new GPSPosition('N', 48.05, 'E', 17.05); // Bottom-right

        GPSPosition gpsPosition3 = new GPSPosition('N', 48.03, 'E', 17.03); // Top-left (táto sa prekrýva s parcela1)
        GPSPosition gpsPosition4 = new GPSPosition('N', 48.06, 'E', 17.06); // Bottom-right

        GPSPosition gpsPosition5 = new GPSPosition('N', 48.10, 'E', 17.10); // Top-left (táto sa neprekrýva)
        GPSPosition gpsPosition6 = new GPSPosition('N', 48.15, 'E', 17.15); // Bottom-right

        Parcela parcela1 = new Parcela(1, "Parcela 1", gpsPosition1, gpsPosition2); // Neprekrýva sa
        Parcela parcela2 = new Parcela(2, "Parcela 2", gpsPosition3, gpsPosition4); // Prekrýva sa s parcela1
        Parcela parcela3 = new Parcela(3, "Parcela 3", gpsPosition5, gpsPosition6); // Neprekrýva sa

        log.info("Parcela example-------------------------------------");
        KDTree<GeodeticObject> kdTreeParcela = new KDTree<>();
        kdTreeParcela.insert(parcela1, POCET_DIMENZII); // Insert úspešný
        kdTreeParcela.insert(parcela2, POCET_DIMENZII); // Prekrýva sa, vkladanie je odmietnuté
        kdTreeParcela.insert(parcela3, POCET_DIMENZII); // Insert úspešný

        log.info("Kontrola prekryvu pre parcely:");
        boolean intersects = RelatedObjectsManager.areIntersecting(parcela1, parcela2);
        log.info("Parcela 1 a Parcela 2 sa " + (intersects ? "neprekrývajú." : "prekrývajú!"));

        intersects = RelatedObjectsManager.areIntersecting(parcela1, parcela3);
        log.info("Parcela 1 a Parcela 3 sa " + (intersects ? "neprekrývajú." : "prekrývajú!"));

        GeodeticObject found = kdTreeParcela.find(parcela1, POCET_DIMENZII);
        if (found != null) {
            log.info("Found parcela: " + found.getDescription());
        } else {
            log.warning("Parcela not found!");
        }

        kdTreeParcela.delete(parcela1, POCET_DIMENZII);
        log.info("Deleted parcela: " + parcela2 .getId());
    }

    private void runPropertyExample() {
        GPSPosition position1 = new GPSPosition('N', 48.151, 'E', 17.101); // Top-left
        GPSPosition position2 = new GPSPosition('N', 48.171, 'E', 17.121); // Bottom-right

        GPSPosition position3 = new GPSPosition('N', 48.160, 'E', 17.111); // Top-left (prekrýva sa s property1)
        GPSPosition position4 = new GPSPosition('N', 48.180, 'E', 17.131); // Bottom-right

        GPSPosition position5 = new GPSPosition('N', 48.200, 'E', 17.200); // Top-left (neprekrýva sa)
        GPSPosition position6 = new GPSPosition('N', 48.220, 'E', 17.220); // Bottom-right

        Property property1 = new Property(100, "Property 1", position1, position2); // Neprekrýva sa
        Property property2 = new Property(101, "Property 2", position3, position4); // Prekrýva sa s property1
        Property property3 = new Property(102, "Property 3", position5, position6); // Neprekrýva sa

        log.info("Property example-------------------------------------");
        KDTree<GeodeticObject> kdTreeProperty = new KDTree<>();
        kdTreeProperty.insert(property1, POCET_DIMENZII); // Insert úspešný
        kdTreeProperty.insert(property2, POCET_DIMENZII); // Prekrýva sa s property1, vkladanie je odmietnuté
        kdTreeProperty.insert(property3, POCET_DIMENZII); // Insert úspešný

        // Kontrola prekryvu
        log.info("Kontrola prekryvu pre nehnuteľnosti:");
        boolean intersects = RelatedObjectsManager.areIntersecting(property1, property2);
        log.info("Property 1 a Property 2 sa " + (intersects ? "neprekrývajú." : "prekrývajú!"));

        intersects = RelatedObjectsManager.areIntersecting(property1, property3);
        log.info("Property 1 a Property 3 sa " + (intersects ? "neprekrývajú." : "prekrývajú!"));
    }
}
