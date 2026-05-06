package ua.edu.unicrossplatform.lab01;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TextStatisticsApp {
    private final TextStatisticsAnalyzer analyzer = new TextStatisticsAnalyzer();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TextStatisticsApp().show());
    }

    private void show() {
        JFrame frame = new JFrame("lab01 - Статистичний аналіз тексту");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea outputArea = new JTextArea(20, 60);
        outputArea.setEditable(false);

        JButton chooseButton = new JButton("Вибрати текстовий файл");
        chooseButton.addActionListener(event -> openAndAnalyzeFile(frame, outputArea));

        JPanel topPanel = new JPanel();
        topPanel.add(chooseButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openAndAnalyzeFile(JFrame frame, JTextArea outputArea) {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(frame);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path path = chooser.getSelectedFile().toPath();
        try {
            String text = Files.readString(path);
            TextStatistics statistics = analyzer.analyze(text);
            outputArea.setText("Файл: " + path + System.lineSeparator() + System.lineSeparator()
                    + TextStatisticsFormatter.format(statistics));
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(frame, "Не вдалося прочитати файл: " + exception.getMessage(),
                    "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
