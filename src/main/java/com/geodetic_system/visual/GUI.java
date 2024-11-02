package com.geodetic_system.visual;

import com.geodetic_system.controller.Controller;
import javax.swing.*;

public class GUI {

    private Controller controller = new Controller();
    private JTextField userText;
    private JTextField latitudeText;
    private JTextField longitudeText;
    private JTextField bottomRightLatText;
    private JTextField bottomRightLongText;
    private JTextField findLatitudeText;
    private JTextField findLongitudeText;
    private JTextField deleteLatitudeText;
    private JTextField deleteLongitudeText;
    private JTextArea outputArea;
    private JCheckBox parcelaCheckBox;
    private JCheckBox nehnutelnostCheckBox;
    private final String INSERT = "INSERT";
    private final String FIND = "FIND";
    private final String DELETE = "DELETE";
    private final String PARCELA = "PARCELA";
    private final String PROPERTY = "PROPERTY";

    private static final int POCET_DIMENZII = 2;

    /**
     * Main method
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new GUI();
    }

    /**
     * Constructor for the GUI
     */
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

    /**
     * Place components on the panel
     * @param panel panel to place components on
     */
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

        // Add checkboxes for Parcela and Nehnutelnost
        parcelaCheckBox = new JCheckBox("Parcela");
        parcelaCheckBox.setBounds(10, 110, 80, 25);
        panel.add(parcelaCheckBox);

        nehnutelnostCheckBox = new JCheckBox("Nehnutelnost");
        nehnutelnostCheckBox.setBounds(100, 110, 120, 25);
        panel.add(nehnutelnostCheckBox);

        JButton insertButton = new JButton("Insert");
        insertButton.setBounds(10, 140, 150, 25);
        panel.add(insertButton);

        JLabel findByLatLongLabel = new JLabel("Find by Top-Left GPS:");
        findByLatLongLabel.setBounds(10, 180, 150, 25);
        panel.add(findByLatLongLabel);

        findLatitudeText = new JTextField(10);
        findLatitudeText.setBounds(160, 180, 75, 25);
        panel.add(findLatitudeText);

        findLongitudeText = new JTextField(10);
        findLongitudeText.setBounds(245, 180, 75, 25);
        panel.add(findLongitudeText);

        JButton findButton = new JButton("Find");
        findButton.setBounds(330, 180, 100, 25);
        panel.add(findButton);

        JLabel deleteByLatLongLabel = new JLabel("Delete by Top-Left GPS:");
        deleteByLatLongLabel.setBounds(10, 220, 150, 25);
        panel.add(deleteByLatLongLabel);

        deleteLatitudeText = new JTextField(10);
        deleteLatitudeText.setBounds(160, 220, 75, 25);
        panel.add(deleteLatitudeText);

        deleteLongitudeText = new JTextField(10);
        deleteLongitudeText.setBounds(245, 220, 75, 25);
        panel.add(deleteLongitudeText);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(10, 260, 150, 25);
        panel.add(deleteButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(10, 300, 460, 150);
        panel.add(scrollPane);

        /**
         * Insert button action listener
         */
        insertButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(userText.getText());
                double topLeftLat = Double.parseDouble(latitudeText.getText());
                double topLeftLong = Double.parseDouble(longitudeText.getText());
                double bottomRightLat = Double.parseDouble(bottomRightLatText.getText());
                double bottomRightLong = Double.parseDouble(bottomRightLongText.getText());

                GUIObject guiObject = null;

                if (parcelaCheckBox.isSelected()) {
                    guiObject = new GUIObject(id, PARCELA + id, topLeftLat, topLeftLong, bottomRightLat, bottomRightLong, INSERT, PARCELA);
                }
                if (nehnutelnostCheckBox.isSelected()) {
                    guiObject = new GUIObject(id, PROPERTY + id, topLeftLat, topLeftLong, bottomRightLat, bottomRightLong, INSERT,  PROPERTY);
                }

                if (parcelaCheckBox.isSelected() && nehnutelnostCheckBox.isSelected()) {
                    int random = (int) (Math.random() * 2);
                    if (random == 0) {
                        guiObject = new GUIObject(id, PROPERTY + id, topLeftLat, topLeftLong, bottomRightLat, bottomRightLong, INSERT,  PROPERTY);
                    } else {
                        guiObject = new GUIObject(id, PARCELA + id, topLeftLat, topLeftLong, bottomRightLat, bottomRightLong, INSERT,  PARCELA);
                    }
                }

                //TODO: vkladanie dvoch objektov - ako sa spravaju a referencuju v strome?
                if (guiObject != null) {
                    boolean success = this.controller.tryToProcess(guiObject);
                    String message = success ? "Object inserted: ID " + id + "\n" : "Insertion failed. Object overlaps with an existing one.\n";
                    outputArea.append(message);
                } else {
                    outputArea.append("GUIObject is null.\n");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid input. Please enter valid numbers.");
            }
        });

        /**
         * Find button action listener
         */
        findButton.addActionListener(e -> {
            try {
                double latitude = Double.parseDouble(findLatitudeText.getText());
                double longitude = Double.parseDouble(findLongitudeText.getText());

                GUIObject dummyGuiObject = null;

                if (parcelaCheckBox.isSelected()) {
                    dummyGuiObject = new GUIObject(0, "", latitude, longitude, 0, 0, FIND, PARCELA);
                }
                if (nehnutelnostCheckBox.isSelected()) {
                    dummyGuiObject = new GUIObject(0, "", latitude, longitude, 0, 0, FIND, PROPERTY);
                }

                    GUIObject returnedFormValidation = this.controller.tryToFindGUIObject(dummyGuiObject);
                    if (dummyGuiObject.getId() == returnedFormValidation.getId()) {
                        outputArea.append("Object not found.\n");
                    } else {
                        outputArea.append("Object found: ID " + returnedFormValidation.getId() + "\n");
                        outputArea.append("Description: " + returnedFormValidation.getDescription() + "\n");
                        outputArea.append("Top-Left GPS: (" + returnedFormValidation.getGPSPositionTopLeftX() + ", " + returnedFormValidation.getGPSPositionTopLeftY() + ")\n");
                        outputArea.append("Bottom-Right GPS: (" + returnedFormValidation.getGPSPositionBottomRightX() + ", " + returnedFormValidation.getGPSPositionBottomRightY() + ")\n");
                        //TODO: info about related objects - if any.

                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid input. Please enter valid numbers for Latitude and Longitude.");
            }
        });

        /**
         * Delete button action listener
         */
        deleteButton.addActionListener(e -> {
            try {
                double deleteTopLeftLat = Double.parseDouble(deleteLatitudeText.getText());
                double deleteTopLeftLong = Double.parseDouble(deleteLongitudeText.getText());

                GUIObject dummyGuiObject = null;
                boolean canDelete = false;

                if (parcelaCheckBox.isSelected()) {
                    dummyGuiObject = new GUIObject(0, "", deleteTopLeftLat, deleteTopLeftLong, 0, 0, DELETE, PARCELA);
                }
                if (nehnutelnostCheckBox.isSelected()) {
                    dummyGuiObject = new GUIObject(0, "", deleteTopLeftLat, deleteTopLeftLong, 0, 0, DELETE, PROPERTY);
                }

                GUIObject returnedFormValidation = this.controller.tryToFindGUIObject(dummyGuiObject);
                if (dummyGuiObject.getId() == returnedFormValidation.getId()) {
                    outputArea.append("Object not found.\n");
                } else {
                    outputArea.append("Object found: ID " + returnedFormValidation.getId() + "\n");
                    outputArea.append("Description: " + returnedFormValidation.getDescription() + "\n");
                    outputArea.append("Top-Left GPS: (" + returnedFormValidation.getGPSPositionTopLeftX() + ", " + returnedFormValidation.getGPSPositionTopLeftY() + ")\n");
                    outputArea.append("Bottom-Right GPS: (" + returnedFormValidation.getGPSPositionBottomRightX() + ", " + returnedFormValidation.getGPSPositionBottomRightY() + ")\n");
                    //TODO: info about related objects - if any.

                    canDelete = true;
                }

                if (canDelete) {
                    boolean success = this.controller.tryToProcess(returnedFormValidation);
                    String message = success ? "Object deleted: ID " + returnedFormValidation.getId() + "\n" : "Deletion failed. Object not found.\n";
                    outputArea.append(message);
                }


            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid input. Please enter valid numbers for Latitude and Longitude.");
            }
        });
    }
}
