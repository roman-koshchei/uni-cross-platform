package ua.edu.unicrossplatform.lab02;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;
import java.util.List;
import java.util.Locale;

public final class UnitConversionApp {
    private final UnitConverter converter = new UnitConverter();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UnitConversionApp().show());
    }

    private void show() {
        JFrame frame = new JFrame("lab02 - Конвертер величин");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComboBox<MeasurementCategory> categoryBox = new JComboBox<>(MeasurementCategory.values());
        JComboBox<UnitDefinition> fromBox = new JComboBox<>();
        JComboBox<UnitDefinition> toBox = new JComboBox<>();
        JTextField inputField = new JTextField("1.0");
        JTextField resultField = new JTextField();
        resultField.setEditable(false);

        updateUnits(categoryBox, fromBox, toBox);
        categoryBox.addActionListener(event -> updateUnits(categoryBox, fromBox, toBox));

        JButton convertButton = new JButton("Конвертувати");
        convertButton.addActionListener(event -> convert(inputField, resultField, fromBox, toBox, frame));

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Категорія"));
        panel.add(categoryBox);
        panel.add(new JLabel("З"));
        panel.add(fromBox);
        panel.add(new JLabel("У"));
        panel.add(toBox);
        panel.add(new JLabel("Значення"));
        panel.add(inputField);
        panel.add(new JLabel("Результат"));
        panel.add(resultField);
        panel.add(new JLabel());
        panel.add(convertButton);

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void updateUnits(JComboBox<MeasurementCategory> categoryBox,
                             JComboBox<UnitDefinition> fromBox,
                             JComboBox<UnitDefinition> toBox) {
        MeasurementCategory category = (MeasurementCategory) categoryBox.getSelectedItem();
        if (category == null) {
            return;
        }

        List<UnitDefinition> units = converter.unitsFor(category);
        fromBox.removeAllItems();
        toBox.removeAllItems();
        for (UnitDefinition unit : units) {
            fromBox.addItem(unit);
            toBox.addItem(unit);
        }
        if (units.size() > 1) {
            toBox.setSelectedIndex(1);
        }
    }

    private void convert(JTextField inputField,
                         JTextField resultField,
                         JComboBox<UnitDefinition> fromBox,
                         JComboBox<UnitDefinition> toBox,
                         JFrame frame) {
        try {
            double value = Double.parseDouble(inputField.getText().trim());
            UnitDefinition from = (UnitDefinition) fromBox.getSelectedItem();
            UnitDefinition to = (UnitDefinition) toBox.getSelectedItem();
            if (from == null || to == null) {
                return;
            }
            double result = converter.convert(value, from, to);
            resultField.setText(String.format(Locale.ROOT, "%.6f", result));
        } catch (RuntimeException exception) {
            JOptionPane.showMessageDialog(frame, "Помилка конвертації: " + exception.getMessage(),
                    "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
