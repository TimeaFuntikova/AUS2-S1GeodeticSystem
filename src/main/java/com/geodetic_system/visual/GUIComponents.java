package com.geodetic_system.visual;

import javax.swing.*;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Trieda GUIComponents obsahuje metódy pre vytvorenie GUI komponentov.
 * GUI komponenty sú vytvorené pomocou triedy JPanel.
 * GUI komponenty obsahujú textové polia, tlačidlá, checkboxy a textové oblasti.
 */
public class GUIComponents {

    private final JTextField userText;
    private final JTextField latitudeText;
    private final JTextField longitudeText;

    private final JTextField bottomRightLatText;
    private final JTextField bottomRightLongText;
    private final JTextField findLatitudeText;

    private final JTextField findLongitudeText;
    private final JTextField deleteLatitudeText;
    private final JTextField deleteLongitudeText;

    private final JCheckBox parcelaCheckBox;
    private final JCheckBox nehnutelnostCheckBox;
    private final JTextArea outputArea;

    private final JButton insertButton;
    private final JButton findButton;
    private final JButton deleteButton;

    public GUIComponents(JPanel panel) {
        userText = new JTextField(20);
        latitudeText = new JTextField(10);
        longitudeText = new JTextField(10);
        bottomRightLatText = new JTextField(10);
        bottomRightLongText = new JTextField(10);
        findLatitudeText = new JTextField(10);
        findLongitudeText = new JTextField(10);
        deleteLatitudeText = new JTextField(10);
        deleteLongitudeText = new JTextField(10);

        parcelaCheckBox = new JCheckBox("Parcela");
        nehnutelnostCheckBox = new JCheckBox("Nehnutelnost");

        insertButton = new JButton("Insert");
        findButton = new JButton("Find");
        deleteButton = new JButton("Delete");

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        panel.setLayout(null);

        JLabel userLabel = new JLabel("ID:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel latitudeLabel = new JLabel("Top-Left Lat/Long:");
        latitudeLabel.setBounds(10, 50, 120, 25);
        panel.add(latitudeLabel);

        latitudeText.setBounds(140, 50, 75, 25);
        panel.add(latitudeText);

        longitudeText.setBounds(225, 50, 75, 25);
        panel.add(longitudeText);

        JLabel bottomRightLabel = new JLabel("Bottom-Right Lat/Long:");
        bottomRightLabel.setBounds(10, 80, 140, 25);
        panel.add(bottomRightLabel);

        bottomRightLatText.setBounds(160, 80, 75, 25);
        panel.add(bottomRightLatText);

        bottomRightLongText.setBounds(245, 80, 75, 25);
        panel.add(bottomRightLongText);

        parcelaCheckBox.setBounds(10, 110, 80, 25);
        panel.add(parcelaCheckBox);

        nehnutelnostCheckBox.setBounds(100, 110, 120, 25);
        panel.add(nehnutelnostCheckBox);

        JButton insertButton = new JButton("Insert");
        insertButton.setBounds(10, 140, 150, 25);
        panel.add(insertButton);

        JLabel findByLatLongLabel = new JLabel("Find by Top-Left GPS:");
        findByLatLongLabel.setBounds(10, 180, 150, 25);
        panel.add(findByLatLongLabel);

        findLatitudeText.setBounds(160, 180, 75, 25);
        panel.add(findLatitudeText);

        findLongitudeText.setBounds(245, 180, 75, 25);
        panel.add(findLongitudeText);

        JButton findButton = new JButton("Find");
        findButton.setBounds(330, 180, 100, 25);
        panel.add(findButton);

        JLabel deleteByLatLongLabel = new JLabel("Delete by Top-Left GPS:");
        deleteByLatLongLabel.setBounds(10, 220, 150, 25);
        panel.add(deleteByLatLongLabel);

        deleteLatitudeText.setBounds(160, 220, 75, 25);
        panel.add(deleteLatitudeText);

        deleteLongitudeText.setBounds(245, 220, 75, 25);
        panel.add(deleteLongitudeText);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(10, 260, 150, 25);
        panel.add(deleteButton);

        outputArea.setEditable(false);

        scrollPane.setBounds(10, 300, 460, 150);
        panel.add(scrollPane);

        panel.revalidate();
        panel.repaint();

    }

    public JTextField getUserText() {
        return userText;
    }

    public JTextArea getOutputArea() {
        return outputArea;
    }

    public JButton getInsertButton() {
        return insertButton;
    }

    public JButton getFindButton() {
        return findButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public GUIObject createGUIObject(ActionType actionType) {
        try {
            if (userText.getText().isEmpty() || latitudeText.getText().isEmpty() || longitudeText.getText().isEmpty() ||
                    bottomRightLatText.getText().isEmpty() || bottomRightLongText.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            int id = Integer.parseInt(userText.getText());
            double topLeftLat = Double.parseDouble(latitudeText.getText());
            double topLeftLong = Double.parseDouble(longitudeText.getText());
            double bottomRightLat = Double.parseDouble(bottomRightLatText.getText());
            double bottomRightLong = Double.parseDouble(bottomRightLongText.getText());

            GUIObject guiObject = null;

            if (parcelaCheckBox.isSelected() && nehnutelnostCheckBox.isSelected()) {
                int random = new Random().nextInt(2);
                guiObject = new GUIObject(
                        id,
                        (random == 0 ? "PROPERTY" : "PARCELA") + id,
                        topLeftLat,
                        topLeftLong,
                        bottomRightLat,
                        bottomRightLong,
                        actionType.name(),
                        random == 0 ? "PROPERTY" : "PARCELA"
                );
            } else if (parcelaCheckBox.isSelected()) {
                guiObject = new GUIObject(
                        id,
                        "PARCELA" + id,
                        topLeftLat,
                        topLeftLong,
                        bottomRightLat,
                        bottomRightLong,
                        actionType.name(),
                        "PARCELA"
                );
            } else if (nehnutelnostCheckBox.isSelected()) {
                guiObject = new GUIObject(
                        id,
                        "PROPERTY" + id,
                        topLeftLat,
                        topLeftLong,
                        bottomRightLat,
                        bottomRightLong,
                        actionType.name(),
                        "PROPERTY"
                );
            }

            return guiObject;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public GUIObject createGUIObjectForInsert() {
        return this.createGUIObject(ActionType.valueOf("INSERT"));
    }

    public GUIObject createGUIObjectForFind() {
       return this.createGUIObject(ActionType.valueOf("FIND")); }

    public GUIObject createGUIObjectForDelete() {
        return this.createGUIObject(ActionType.valueOf("DELETE"));
    }

    public void showDeleteSelectionDialog(List<GUIObject> foundObjects, Consumer<List<GUIObject>> onSelection) {
        JList<GUIObject> list = new JList<>(foundObjects.toArray(new GUIObject[0]));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        int result = JOptionPane.showConfirmDialog(null, new JScrollPane(list),
                "Select objects to delete", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            onSelection.accept(list.getSelectedValuesList());
        }
    }

    public void clearInputFields() {
        userText.setText("");
        latitudeText.setText("");
        longitudeText.setText("");
        bottomRightLatText.setText("");
        bottomRightLongText.setText("");
        parcelaCheckBox.setSelected(false);
        nehnutelnostCheckBox.setSelected(false);
    }
}
