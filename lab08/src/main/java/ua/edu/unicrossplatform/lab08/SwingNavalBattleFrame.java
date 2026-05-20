package ua.edu.unicrossplatform.lab08;

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

public final class SwingNavalBattleFrame extends JFrame {
    private final Random random = new Random();
    private final JButton[][] humanCells = new JButton[Battlefield.SIZE][Battlefield.SIZE];
    private final JButton[][] computerCells = new JButton[Battlefield.SIZE][Battlefield.SIZE];
    private final JLabel statusLabel = new JLabel("Натисніть \"Нова гра\".", SwingConstants.CENTER);
    private final JLabel fleetLabel = new JLabel("Флот: 1x4, 2x3, 3x2, 4x1", SwingConstants.CENTER);
    private final JTextArea logArea = new JTextArea(9, 60);

    private Battlefield humanField;
    private Battlefield computerField;
    private ComputerShooter computerShooter;
    private boolean humanTurn;
    private boolean gameActive;

    public SwingNavalBattleFrame() {
        super("Лабораторна 08: Морський бій");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(930, 760));
        setLocationByPlatform(true);
        setLayout(new BorderLayout(12, 12));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        add(createHeader(), BorderLayout.NORTH);
        add(createBoardsPanel(), BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.EAST);
        add(createLogPanel(), BorderLayout.SOUTH);

        startNewGame();
        pack();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xD6DCE5)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Гра \"Морський бій\"");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        JLabel subtitle = new JLabel("Стріляйте по карті комп'ютера. Влучив або потопив - робите ще один хід.");

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

    private JPanel createBoardsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 14, 14));
        panel.add(boardWithTitle("Ваше поле", humanCells, false));
        panel.add(boardWithTitle("Поле комп'ютера", computerCells, true));
        return panel;
    }

    private JPanel boardWithTitle(String title, JButton[][] buttons, boolean targetBoard) {
        JPanel wrapper = new JPanel(new BorderLayout(4, 4));
        wrapper.setBorder(BorderFactory.createTitledBorder(title));
        wrapper.add(createCoordinateHeader(), BorderLayout.NORTH);
        wrapper.add(createBoardGrid(buttons, targetBoard), BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createCoordinateHeader() {
        JPanel panel = new JPanel(new GridLayout(1, Battlefield.SIZE + 1, 2, 2));
        panel.add(new JLabel("", SwingConstants.CENTER));
        for (int col = 0; col < Battlefield.SIZE; col++) {
            panel.add(new JLabel(String.valueOf((char) ('A' + col)), SwingConstants.CENTER));
        }
        return panel;
    }

    private JPanel createBoardGrid(JButton[][] buttons, boolean targetBoard) {
        JPanel panel = new JPanel(new GridLayout(Battlefield.SIZE, Battlefield.SIZE + 1, 2, 2));
        for (int row = 0; row < Battlefield.SIZE; row++) {
            panel.add(new JLabel(String.valueOf(row + 1), SwingConstants.CENTER));
            for (int col = 0; col < Battlefield.SIZE; col++) {
                JButton button = createCellButton(row, col, targetBoard);
                buttons[row][col] = button;
                panel.add(button);
            }
        }
        return panel;
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(220, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Керування"));

        JButton newGameButton = new JButton("Нова гра");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(event -> startNewGame());

        JButton algorithmButton = new JButton("Алгоритм комп'ютера");
        algorithmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        algorithmButton.addActionListener(event -> showAlgorithm());

        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 14f));
        fleetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(8));
        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(fleetLabel);
        panel.add(Box.createVerticalStrut(18));
        panel.add(newGameButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(algorithmButton);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JScrollPane createLogPanel() {
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Журнал гри"));
        return scrollPane;
    }

    private JButton createCellButton(int row, int col, boolean targetBoard) {
        JButton button = new JButton();
        button.setFont(button.getFont().deriveFont(Font.BOLD, 13f));
        button.setFocusPainted(false);
        button.setToolTipText(new Coordinate(row, col).label());
        if (targetBoard) {
            button.addActionListener(event -> humanShot(new Coordinate(row, col)));
        }
        return button;
    }

    private void startNewGame() {
        humanField = Battlefield.autoPlace(new Random(random.nextLong()));
        computerField = Battlefield.autoPlace(new Random(random.nextLong()));
        computerShooter = new ComputerShooter(new Random(random.nextLong()));
        humanTurn = random.nextBoolean();
        gameActive = true;
        logArea.setText("");

        appendLog("Флот користувача коректний: " + yesNo(humanField.isValidFleet()) + ".");
        appendLog("Флот комп'ютера коректний: " + yesNo(computerField.isValidFleet()) + ".");
        appendLog("Перший хід: " + (humanTurn ? "користувач" : "комп'ютер") + ".");
        refreshBoards();

        if (humanTurn) {
            statusLabel.setText("Ваш хід: оберіть клітинку на полі комп'ютера.");
        } else {
            statusLabel.setText("Першим ходить комп'ютер.");
            computerTurnUntilMiss();
        }
    }

    private void humanShot(Coordinate coordinate) {
        if (!gameActive || !humanTurn || computerField.wasShot(coordinate)) {
            return;
        }

        ShotResult result = computerField.shoot(coordinate);
        appendLog("Користувач стріляє в " + coordinate.label() + " -> " + resultLabel(result) + ".");
        refreshBoards();

        if (computerField.allShipsSunk()) {
            finishGame("користувач");
            return;
        }
        if (result == ShotResult.MISS) {
            humanTurn = false;
            statusLabel.setText("Мимо. Хід комп'ютера.");
            computerTurnUntilMiss();
        } else {
            statusLabel.setText(resultLabel(result) + ". Ваш додатковий хід.");
        }
    }

    private void computerTurnUntilMiss() {
        while (gameActive && !humanTurn) {
            Coordinate shot = computerShooter.nextShot();
            ShotResult result = humanField.shoot(shot);
            computerShooter.acceptResult(shot, result);
            appendLog("Комп'ютер стріляє в " + shot.label() + " -> " + resultLabel(result) + ".");
            refreshBoards();

            if (humanField.allShipsSunk()) {
                finishGame("комп'ютер");
                return;
            }
            if (result == ShotResult.MISS) {
                humanTurn = true;
                statusLabel.setText("Комп'ютер промахнувся. Ваш хід.");
            } else {
                statusLabel.setText("Комп'ютер " + resultLabel(result).toLowerCase() + " і стріляє ще раз.");
            }
        }
        refreshBoards();
    }

    private void finishGame(String winner) {
        gameActive = false;
        humanTurn = false;
        statusLabel.setText("Переможець: " + winner + ".");
        appendLog("Переможець: " + winner + ".");
        refreshBoards();
    }

    private void refreshBoards() {
        refreshBoard(humanCells, humanField, true);
        refreshBoard(computerCells, computerField, false);
    }

    private void refreshBoard(JButton[][] buttons, Battlefield field, boolean revealShips) {
        for (int row = 0; row < Battlefield.SIZE; row++) {
            for (int col = 0; col < Battlefield.SIZE; col++) {
                Coordinate coordinate = new Coordinate(row, col);
                JButton button = buttons[row][col];
                boolean shot = field.wasShot(coordinate);
                boolean ship = hasShip(field, coordinate);
                boolean sunk = isSunkShipCell(field, coordinate);

                button.setText(cellText(shot, ship, sunk, revealShips));
                button.setBackground(cellColor(shot, ship, sunk, revealShips));
                button.setOpaque(true);
                button.setEnabled(gameActive && !revealShips && humanTurn && !shot);
            }
        }
    }

    private boolean hasShip(Battlefield field, Coordinate coordinate) {
        for (Ship ship : field.ships()) {
            if (ship.occupies(coordinate)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSunkShipCell(Battlefield field, Coordinate coordinate) {
        for (Ship ship : field.ships()) {
            if (ship.occupies(coordinate)) {
                return ship.isSunk();
            }
        }
        return false;
    }

    private String cellText(boolean shot, boolean ship, boolean sunk, boolean revealShips) {
        if (shot && ship) {
            return sunk ? "S" : "X";
        }
        if (shot) {
            return "o";
        }
        if (revealShips && ship) {
            return "#";
        }
        return "";
    }

    private Color cellColor(boolean shot, boolean ship, boolean sunk, boolean revealShips) {
        if (shot && ship) {
            return sunk ? new Color(0x8E1B1B) : new Color(0xFFB3A7);
        }
        if (shot) {
            return new Color(0xD9EAF7);
        }
        if (revealShips && ship) {
            return new Color(0x64748B);
        }
        return Color.WHITE;
    }

    private String resultLabel(ShotResult result) {
        return switch (result) {
            case MISS -> "Мимо";
            case HIT -> "Влучив";
            case SUNK -> "Потопив";
        };
    }

    private String yesNo(boolean value) {
        return value ? "так" : "ні";
    }

    private void appendLog(String message) {
        logArea.append(message + System.lineSeparator());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void showAlgorithm() {
        JOptionPane.showMessageDialog(
                this,
                "Комп'ютер використовує доцільну стратегію пошуку:\n"
                        + "1. Поки немає влучань, стріляє у випадкові ще не перевірені клітинки.\n"
                        + "2. Після результату \"Влучив\" додає сусідні клітинки зверху, знизу, ліворуч і праворуч "
                        + "до черги пріоритетних пострілів.\n"
                        + "3. Після \"Потопив\" очищає чергу цілей і знову переходить до пошуку.\n"
                        + "Алгоритм не повторює постріли та швидко добиває кораблі після першого влучання.",
                "Алгоритм комп'ютера",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
