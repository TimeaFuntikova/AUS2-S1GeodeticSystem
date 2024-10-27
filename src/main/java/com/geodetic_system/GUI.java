package com.geodetic_system;

import javax.swing.*;

public class GUI {
    private KDTree<GeodeticObject> kdTree = new KDTree<>();

    private JTextField userText;
    private JTextField latitudeText;
    private JTextField longitudeText;
    private JTextField bottomRightLatText;
    private JTextField bottomRightLongText;
    private JTextField findLatitudeText;
    private JTextField findLongitudeText;
    private JTextField deleteLatitudeText; // Field for deletion based on latitude
    private JTextField deleteLongitudeText; // Field for deletion based on longitude
    private JTextArea outputArea; // For output messages

    private static final int POCET_DIMENZII = 2;

    public GUI() {
        JFrame frame = new JFrame("KDTree App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("ID:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel latitudeLabel = new JLabel("Top-Left Lat/Long:");
        latitudeLabel.setBounds(10, 50, 120, 25);
        panel.add(latitudeLabel);

        latitudeText = new JTextField(10);
        latitudeText.setBounds(140, 50, 75, 25);
        panel.add(latitudeText);

        longitudeText = new JTextField(10);
        longitudeText.setBounds(225, 50, 75, 25);
        panel.add(longitudeText);

        JLabel bottomRightLabel = new JLabel("Bottom-Right Lat/Long:");
        bottomRightLabel.setBounds(10, 80, 140, 25);
        panel.add(bottomRightLabel);

        bottomRightLatText = new JTextField(10);
        bottomRightLatText.setBounds(160, 80, 75, 25);
        panel.add(bottomRightLatText);

        bottomRightLongText = new JTextField(10);
        bottomRightLongText.setBounds(245, 80, 75, 25);
        panel.add(bottomRightLongText);

        JButton insertButton = new JButton("Insert");
        insertButton.setBounds(10, 110, 150, 25);
        panel.add(insertButton);

        JLabel findByLatLongLabel = new JLabel("Find by Top-Left GPS:");
        findByLatLongLabel.setBounds(10, 150, 150, 25);
        panel.add(findByLatLongLabel);

        findLatitudeText = new JTextField(10);
        findLatitudeText.setBounds(160, 150, 75, 25);
        panel.add(findLatitudeText);

        findLongitudeText = new JTextField(10);
        findLongitudeText.setBounds(245, 150, 75, 25);
        panel.add(findLongitudeText);

        JButton findButton = new JButton("Find");
        findButton.setBounds(330, 150, 100, 25);
        panel.add(findButton);

        JLabel deleteByLatLongLabel = new JLabel("Delete by Top-Left GPS:");
        deleteByLatLongLabel.setBounds(10, 190, 150, 25);
        panel.add(deleteByLatLongLabel);

        deleteLatitudeText = new JTextField(10);
        deleteLatitudeText.setBounds(160, 190, 75, 25);
        panel.add(deleteLatitudeText);

        deleteLongitudeText = new JTextField(10);
        deleteLongitudeText.setBounds(245, 190, 75, 25);
        panel.add(deleteLongitudeText);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(10, 230, 150, 25);
        panel.add(deleteButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(10, 270, 460, 150);
        panel.add(scrollPane);

        insertButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(userText.getText());
                double topLeftLat = Double.parseDouble(latitudeText.getText());
                double topLeftLong = Double.parseDouble(longitudeText.getText());
                double bottomRightLat = Double.parseDouble(bottomRightLatText.getText());
                double bottomRightLong = Double.parseDouble(bottomRightLongText.getText());

                GPSPosition topLeft = new GPSPosition('N', topLeftLat, 'E', topLeftLong);
                GPSPosition bottomRight = new GPSPosition('N', bottomRightLat, 'E', bottomRightLong);
                Parcela parcela = new Parcela(id, "Parcela " + id, topLeft, bottomRight);

                if (kdTree.insert(parcela, POCET_DIMENZII)) {
                    outputArea.append("Parcela inserted: ID " + id + "\n");
                } else {
                    outputArea.append("Insertion failed. Parcela overlaps with an existing one.\n");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid input. Please enter valid numbers.");
            }
        });

        findButton.addActionListener(e -> {
            try {
                double latitude = Double.parseDouble(findLatitudeText.getText());
                double longitude = Double.parseDouble(findLongitudeText.getText());

                GPSPosition searchPosition = new GPSPosition('N', latitude, 'E', longitude);
                Parcela searchParcela = new Parcela(0, "", searchPosition, searchPosition);

                Parcela found = (Parcela) kdTree.find(searchParcela, POCET_DIMENZII);
                if (found != null) {
                    outputArea.append("Found parcela: " + found.getDescription() + "\n");
                } else {
                    outputArea.append("Parcela not found.\n");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid input. Please enter valid numbers for Latitude and Longitude.");
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                double deleteTopLeftLat = Double.parseDouble(deleteLatitudeText.getText());
                double deleteTopLeftLong = Double.parseDouble(deleteLongitudeText.getText());
                GPSPosition deleteTopLeft = new GPSPosition('N', deleteTopLeftLat, 'E', deleteTopLeftLong);
                Parcela toDelete = new Parcela(0, "", deleteTopLeft, deleteTopLeft);

                if (kdTree.delete(toDelete, POCET_DIMENZII)) {
                    outputArea.append("Deleted parcela at Top-Left position: (" + deleteTopLeftLat + ", " + deleteTopLeftLong + ")\n");
                } else {
                    outputArea.append("Parcela at Top-Left position: (" + deleteTopLeftLat + ", " + deleteTopLeftLong + ") not found.\n");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid input. Please enter valid numbers for Latitude and Longitude.");
            }
        });
    }

    public static void main(String[] args) {
        new GUI();
    }
}
