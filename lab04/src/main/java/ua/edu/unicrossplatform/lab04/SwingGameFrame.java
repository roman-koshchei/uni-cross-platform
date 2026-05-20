package ua.edu.unicrossplatform.lab04;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public final class SwingGameFrame extends JFrame {
    private final Path historyFile;
    private final HistoryStore historyStore;
    private final Random random = new Random();
    private final SpinnerNumberModel sessionsModel = new SpinnerNumberModel(3, 1, 100, 1);
    private final JComboBox<StrategyMode> strategyCombo = new JComboBox<>(StrategyMode.values());
    private final JLabel statusLabel = new JLabel("Налаштуйте гру та натисніть \"Почати гру\".", SwingConstants.CENTER);
    private final JLabel progressLabel = new JLabel("Сеансів завершено: 0/0", SwingConstants.CENTER);
    private final JLabel playerWinsLabel = new JLabel();
    private final JLabel computerWinsLabel = new JLabel();
    private final JLabel drawsLabel = new JLabel();
    private final JTextArea logArea = new JTextArea(12, 44);
    private final JButton wellButton = moveButton(Move.WELL);
    private final JButton scissorsButton = moveButton(Move.SCISSORS);
    private final JButton paperButton = moveButton(Move.PAPER);

    private GameSession gameSession;
    private GameStrategy gameStrategy;
    private int targetSessions;
    private int completedSessions;

    public SwingGameFrame(Path historyFile) {
        super("Лабораторна 04: Криниця-Ножиці-Папір");
        this.historyFile = historyFile;
        this.historyStore = new HistoryStore(historyFile);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(680, 560));
        setLocationByPlatform(true);
        setLayout(new BorderLayout(12, 12));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        add(createHeader(), BorderLayout.NORTH);
        add(createGamePanel(), BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.EAST);
        add(createLogPanel(), BorderLayout.SOUTH);

        setMoveButtonsEnabled(false);
        refreshStatistics();
        pack();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xD6DCE5)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Гра \"Криниця-Ножиці-Папір\"");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        JLabel subtitle = new JLabel("Оберіть кількість сеансів, режим комп'ютера та робіть ходи кнопками.");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(title, constraints);

        constraints.gridy = 1;
        constraints.insets.top = 4;
        panel.add(subtitle, constraints);
        return panel;
    }

    private JPanel createGamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        settingsPanel.add(new JLabel("Кількість сеансів:"));
        settingsPanel.add(new JSpinner(sessionsModel));
        settingsPanel.add(new JLabel("Режим комп'ютера:"));
        settingsPanel.add(strategyCombo);

        JButton startButton = new JButton("Почати гру");
        startButton.addActionListener(event -> startGame());
        settingsPanel.add(startButton);

        JButton statisticsButton = new JButton("Показати статистику");
        statisticsButton.addActionListener(event -> showStatisticsDialog());
        settingsPanel.add(statisticsButton);

        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 16f));
        progressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel movePanel = new JPanel(new GridLayout(1, 3, 12, 12));
        movePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));
        movePanel.add(wellButton);
        movePanel.add(scissorsButton);
        movePanel.add(paperButton);

        panel.add(settingsPanel);
        panel.add(Box.createVerticalStrut(18));
        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(progressLabel);
        panel.add(Box.createVerticalStrut(18));
        panel.add(movePanel);
        return panel;
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(190, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Статистика з файлу"),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        panel.add(playerWinsLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(computerWinsLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(drawsLabel);
        panel.add(Box.createVerticalStrut(14));

        JLabel fileLabel = new JLabel("Файл: " + historyFile.getFileName());
        fileLabel.setToolTipText(historyFile.toAbsolutePath().toString());
        panel.add(fileLabel);
        return panel;
    }

    private JScrollPane createLogPanel() {
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Хід гри"));
        return scrollPane;
    }

    private JButton moveButton(Move move) {
        JButton button = new JButton(move.displayName());
        button.setFont(button.getFont().deriveFont(Font.BOLD, 16f));
        button.addActionListener(event -> playMove(move));
        return button;
    }

    private void startGame() {
        targetSessions = sessionsModel.getNumber().intValue();
        completedSessions = 0;
        gameSession = new GameSession(historyStore);
        gameStrategy = strategyCombo.getItemAt(strategyCombo.getSelectedIndex()).createStrategy(historyFile, random);
        logArea.setText("");
        appendLog("Почато гру: " + targetSessions + " сеанс(ів), режим - " + gameStrategy.name() + ".");
        statusLabel.setText("Ваш хід: оберіть Криницю, Ножиці або Папір.");
        updateProgress();
        setMoveButtonsEnabled(true);
    }

    private void playMove(Move playerMove) {
        if (gameSession == null || gameStrategy == null) {
            return;
        }

        try {
            Round round = gameSession.playRound(playerMove, gameStrategy);
            appendLog(formatRound(round));

            if (round.outcome() == Outcome.DRAW) {
                statusLabel.setText("Нічия. Сеанс переграється, оберіть знак ще раз.");
            } else {
                completedSessions++;
                statusLabel.setText(outcomeLabel(round.outcome()) + ". Продовжуйте гру.");
                refreshStatistics();
            }

            updateProgress();
            if (completedSessions >= targetSessions) {
                statusLabel.setText("Гру завершено. Можна почати нову гру або переглянути статистику.");
                setMoveButtonsEnabled(false);
                appendLog("Гру завершено: " + completedSessions + " із " + targetSessions + " сеансів.");
            }
        } catch (IOException exception) {
            setMoveButtonsEnabled(false);
            showError("Не вдалося записати результат у файл", exception);
        }
    }

    private void updateProgress() {
        progressLabel.setText("Сеансів завершено: " + completedSessions + "/" + targetSessions);
    }

    private void refreshStatistics() {
        try {
            GameStatistics statistics = historyStore.statistics();
            playerWinsLabel.setText("Перемоги: " + statistics.playerWins());
            computerWinsLabel.setText("Поразки: " + statistics.computerWins());
            drawsLabel.setText("Нічиї: " + statistics.draws());
        } catch (IOException exception) {
            playerWinsLabel.setText("Перемоги: -");
            computerWinsLabel.setText("Поразки: -");
            drawsLabel.setText("Нічиї: -");
            showError("Не вдалося прочитати статистику", exception);
        }
    }

    private void showStatisticsDialog() {
        try {
            GameStatistics statistics = historyStore.statistics();
            JOptionPane.showMessageDialog(
                    this,
                    "Перемоги гравця: " + statistics.playerWins()
                            + "\nПеремоги комп'ютера: " + statistics.computerWins()
                            + "\nНічиї: " + statistics.draws()
                            + "\nФайл історії: " + historyFile.toAbsolutePath(),
                    "Статистика перемог і поразок",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException exception) {
            showError("Не вдалося прочитати статистику", exception);
        }
    }

    private String formatRound(Round round) {
        return "Ви: " + round.playerMove().displayName()
                + ", комп'ютер: " + round.computerMove().displayName()
                + ", результат: " + outcomeLabel(round.outcome());
    }

    private String outcomeLabel(Outcome outcome) {
        return switch (outcome) {
            case PLAYER_WIN -> "перемога гравця";
            case COMPUTER_WIN -> "перемога комп'ютера";
            case DRAW -> "нічия";
        };
    }

    private void appendLog(String message) {
        logArea.append(message + System.lineSeparator());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void setMoveButtonsEnabled(boolean enabled) {
        wellButton.setEnabled(enabled);
        scissorsButton.setEnabled(enabled);
        paperButton.setEnabled(enabled);
    }

    private void showError(String message, Exception exception) {
        JOptionPane.showMessageDialog(
                this,
                message + ": " + exception.getMessage(),
                "Помилка",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private enum StrategyMode {
        RANDOM("Випадкові ходи") {
            @Override
            GameStrategy createStrategy(Path historyFile, Random random) {
                return new RandomStrategy(random);
            }
        },
        SESSION_ADAPTIVE("З урахуванням поточного сеансу") {
            @Override
            GameStrategy createStrategy(Path historyFile, Random random) {
                return new SessionAdaptiveStrategy(new RandomStrategy(random));
            }
        },
        HISTORY_ADAPTIVE("З урахуванням попередніх сеансів") {
            @Override
            GameStrategy createStrategy(Path historyFile, Random random) {
                return new FileAdaptiveStrategy(historyFile, new RandomStrategy(random));
            }
        };

        private final String label;

        StrategyMode(String label) {
            this.label = label;
        }

        abstract GameStrategy createStrategy(Path historyFile, Random random);

        @Override
        public String toString() {
            return label;
        }
    }
}
