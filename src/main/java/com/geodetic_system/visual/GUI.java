package com.geodetic_system.visual;

import com.geodetic_system.controller.Controller;
import javax.swing.*;


public class GUI {
    private final Controller controller;
    private final JTextArea outputArea;
    private final GUIComponents components;


    public GUI(Controller controller) {
        this.controller = controller;


        JFrame frame = new JFrame("KDTree App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        GUIComponents components = new GUIComponents(panel);
        outputArea = components.getOutputArea();
        this.components = new GUIComponents(panel);

        frame.add(panel);
        frame.setVisible(true);

        setupActions(components);
    }

    public GUIComponents getComponents() {
        return components;
    }

    private void setupActions(GUIComponents components) {
        components.getInsertButton().addActionListener(e -> controller.handleInsert());
        components.getFindButton().addActionListener(e -> controller.handleFind());
        components.getDeleteButton().addActionListener(e -> controller.handleDelete());
    }

    public void appendOutput(String message) {
        SwingUtilities.invokeLater(() -> outputArea.append(message + "\n"));
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

/*


    private final String INSERT = "INSERT";
    private final String FIND = "FIND";
    private final String DELETE = "DELETE";
    private final String PARCELA = "PARCELA";
    private final String PROPERTY = "PROPERTY";

    private static final int POCET_DIMENZII = 2;

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

                List<GUIObject> returnedFormValidation = this.controller.tryToFindGUIObject(dummyGuiObject);
                if (returnedFormValidation.isEmpty()) {
                    outputArea.append("Objects not found.\n");
                } else {
                    for (GUIObject guiObject : returnedFormValidation) {
                        outputArea.append("Object found: ID " + guiObject.getId() + "\n");
                        outputArea.append("Description: " + guiObject.getDescription() + "\n");
                        outputArea.append("Top-Left GPS: (" + guiObject.getGPSPositionTopLeftX() + ", " + guiObject.getGPSPositionTopLeftY() + ")\n");
                        outputArea.append("Bottom-Right GPS: (" + guiObject.getGPSPositionBottomRightX() + ", " + guiObject.getGPSPositionBottomRightY() + ")\n");
                        //TODO: info about related objects - if any.
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid input. Please enter valid numbers for Latitude and Longitude.");
            }
        });


        deleteButton.addActionListener(e -> {
            try {
                double deleteTopLeftLat = Double.parseDouble(deleteLatitudeText.getText());
                double deleteTopLeftLong = Double.parseDouble(deleteLongitudeText.getText());

                GUIObject dummyGuiObject = null;

                if (parcelaCheckBox.isSelected()) {
                    dummyGuiObject = new GUIObject(0, "", deleteTopLeftLat, deleteTopLeftLong, 0, 0, DELETE, PARCELA);
                } else if (nehnutelnostCheckBox.isSelected()) {
                    dummyGuiObject = new GUIObject(0, "", deleteTopLeftLat, deleteTopLeftLong, 0, 0, DELETE, PROPERTY);
                }

                List<GUIObject> foundObjects = this.controller.tryToFindGUIObject(dummyGuiObject);
                if (foundObjects.isEmpty()) {
                    outputArea.append("Object not found.\n");
                } else {
                    GUIObject[] objectsArray = foundObjects.toArray(new GUIObject[0]);
                    JList<GUIObject> objectList = new JList<>(objectsArray);
                    objectList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

                    int result = JOptionPane.showConfirmDialog(panel, new JScrollPane(objectList),
                            "Select objects to delete", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        List<GUIObject> selectedObjects = objectList.getSelectedValuesList();

                        for (GUIObject guiObject : selectedObjects) {
                            boolean success = this.controller.tryToProcess(guiObject);
                            String message = success ? "Object deleted: ID " + guiObject.getId() + "\n" : "Deletion failed for ID: " + guiObject.getId() + "\n";
                            outputArea.append(message);
                        }
                    } else {
                        outputArea.append("Deletion canceled by user.\n");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid input. Please enter valid numbers for Latitude and Longitude.");
            }
        });
    }
    private void executeCommand(Command command) {
        command.execute();
    }

    public void appendOutput(String message) {
        outputArea.append(message + "\n");
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

 */

