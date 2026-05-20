package ua.edu.unicrossplatform.lab05;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public final class SwingCoinGameFrame extends JFrame {
    private static final int MAX_TAKE = 2;
    private static final int MIN_STARTING_COINS = 15;
    private static final int STARTING_COINS_RANGE = 11;

    private final Random random = new Random();
    private final OptimalCoinPlayer computerPlayer = new OptimalCoinPlayer();
    private final JLabel coinsLabel = new JLabel("Монет: -", SwingConstants.CENTER);
    private final JLabel turnLabel = new JLabel("Натисніть \"Нова гра\".", SwingConstants.CENTER);
    private final JLabel firstPlayerLabel = new JLabel("Перший хід: -", SwingConstants.CENTER);
    private final JTextArea logArea = new JTextArea(12, 42);
    private final JButton takeOneButton = new JButton("Взяти 1 монету");
    private final JButton takeTwoButton = new JButton("Взяти 2 монети");

    private int coinsLeft;
    private boolean humanTurn;
    private boolean gameActive;

    public SwingCoinGameFrame() {
        super("Лабораторна 05: Гра в монети");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(620, 520));
        setLocationByPlatform(true);
        setLayout(new BorderLayout(12, 12));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        add(createHeader(), BorderLayout.NORTH);
        add(createGamePanel(), BorderLayout.CENTER);
        add(createLogPanel(), BorderLayout.SOUTH);

        updateMoveButtons();
        pack();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xD6DCE5)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Гра в монети");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        JLabel subtitle = new JLabel("Початкова кількість монет і перший хід визначаються випадково.");

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

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton newGameButton = new JButton("Нова гра");
        newGameButton.addActionListener(event -> startGame());
        JButton algorithmButton = new JButton("Алгоритм комп'ютера");
        algorithmButton.addActionListener(event -> showAlgorithm());
        actionsPanel.add(newGameButton);
        actionsPanel.add(algorithmButton);

        coinsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        coinsLabel.setFont(coinsLabel.getFont().deriveFont(Font.BOLD, 34f));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        turnLabel.setFont(turnLabel.getFont().deriveFont(Font.BOLD, 16f));
        firstPlayerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel movePanel = new JPanel(new GridLayout(1, 2, 12, 12));
        movePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));
        takeOneButton.setFont(takeOneButton.getFont().deriveFont(Font.BOLD, 16f));
        takeTwoButton.setFont(takeTwoButton.getFont().deriveFont(Font.BOLD, 16f));
        takeOneButton.addActionListener(event -> humanMove(1));
        takeTwoButton.addActionListener(event -> humanMove(2));
        movePanel.add(takeOneButton);
        movePanel.add(takeTwoButton);

        panel.add(actionsPanel);
        panel.add(Box.createVerticalStrut(18));
        panel.add(coinsLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(firstPlayerLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(turnLabel);
        panel.add(Box.createVerticalStrut(18));
        panel.add(movePanel);
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

    private void startGame() {
        coinsLeft = MIN_STARTING_COINS + random.nextInt(STARTING_COINS_RANGE);
        humanTurn = random.nextBoolean();
        gameActive = true;

        logArea.setText("");
        appendLog("Початкова кількість монет: " + coinsLeft + ".");
        firstPlayerLabel.setText("Перший хід: " + (humanTurn ? "людина" : "комп'ютер"));
        refreshState();

        if (!humanTurn) {
            computerMove();
        }
    }

    private void humanMove(int takenCoins) {
        if (!gameActive || !humanTurn || takenCoins > Math.min(MAX_TAKE, coinsLeft)) {
            return;
        }

        applyMove("Людина", takenCoins);
        if (gameActive) {
            humanTurn = false;
            refreshState();
            computerMove();
        }
    }

    private void computerMove() {
        if (!gameActive) {
            return;
        }

        int takenCoins = computerPlayer.chooseMove(coinsLeft, MAX_TAKE);
        applyMove("Комп'ютер", takenCoins);
        if (gameActive) {
            humanTurn = true;
            refreshState();
        }
    }

    private void applyMove(String playerName, int takenCoins) {
        coinsLeft -= takenCoins;
        appendLog(playerName + " бере " + takenCoins + ", залишилось " + coinsLeft + ".");

        if (coinsLeft == 0) {
            gameActive = false;
            coinsLabel.setText("Монет: 0");
            turnLabel.setText("Переможець: " + playerName + ".");
            updateMoveButtons();
            appendLog("Переможець: " + playerName + ".");
        }
    }

    private void refreshState() {
        coinsLabel.setText("Монет: " + coinsLeft);
        turnLabel.setText(gameActive
                ? "Хід: " + (humanTurn ? "людина" : "комп'ютер")
                : "Гру завершено.");
        updateMoveButtons();
    }

    private void updateMoveButtons() {
        takeOneButton.setEnabled(gameActive && humanTurn && coinsLeft >= 1);
        takeTwoButton.setEnabled(gameActive && humanTurn && coinsLeft >= 2);
    }

    private void appendLog(String message) {
        logArea.append(message + System.lineSeparator());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void showAlgorithm() {
        JOptionPane.showMessageDialog(
                this,
                "Комп'ютер грає оптимально: після свого ходу він намагається залишити кількість монет, "
                        + "кратну 3.\nЯкщо монет уже кратна 3, гарантованого виграшного ходу немає, тому він бере 1.\n"
                        + "Оскільки за хід можна взяти 1 або 2 монети, після будь-якого ходу людини комп'ютер "
                        + "може доповнити суму взятих монет до 3.",
                "Алгоритм комп'ютера",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
