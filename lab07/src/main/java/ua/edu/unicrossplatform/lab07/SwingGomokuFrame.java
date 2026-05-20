package ua.edu.unicrossplatform.lab07;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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

public final class SwingGomokuFrame extends JFrame {
    private final ComputerStrategy computerStrategy = new ComputerStrategy();
    private final JButton[][] cells = new JButton[Board.SIZE][Board.SIZE];
    private final JLabel statusLabel = new JLabel("Ваш хід: поставте X у порожню клітинку.", SwingConstants.CENTER);
    private final JTextArea logArea = new JTextArea(8, 44);

    private Board board = new Board();
    private boolean gameActive = true;

    public SwingGomokuFrame() {
        super("Лабораторна 07: П'ять в ряд");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(720, 760));
        setLocationByPlatform(true);
        setLayout(new BorderLayout(12, 12));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        add(createHeader(), BorderLayout.NORTH);
        add(createBoardPanel(), BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.EAST);
        add(createLogPanel(), BorderLayout.SOUTH);

        refreshBoard();
        pack();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xD6DCE5)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Гра \"П'ять в ряд\"");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        JLabel subtitle = new JLabel("Гравець ставить X, комп'ютер ставить O. Перемагає перша лінія з п'яти символів.");

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

    private JPanel createBoardPanel() {
        JPanel panel = new JPanel(new GridLayout(Board.SIZE, Board.SIZE, 3, 3));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                JButton button = createCellButton(row, col);
                cells[row][col] = button;
                panel.add(button);
            }
        }
        return panel;
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(210, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Керування"));

        JButton newGameButton = new JButton("Нова гра");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(event -> startNewGame());

        JButton algorithmButton = new JButton("Алгоритм комп'ютера");
        algorithmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        algorithmButton.addActionListener(event -> showAlgorithm());

        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 14f));

        panel.add(Box.createVerticalStrut(8));
        panel.add(statusLabel);
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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Хід гри"));
        return scrollPane;
    }

    private JButton createCellButton(int row, int col) {
        JButton button = new JButton();
        button.setFont(button.getFont().deriveFont(Font.BOLD, 18f));
        button.setFocusPainted(false);
        button.setToolTipText("Рядок " + row + ", стовпець " + col);
        button.addActionListener(event -> humanMove(row, col));
        return button;
    }

    private void startNewGame() {
        board = new Board();
        gameActive = true;
        logArea.setText("");
        statusLabel.setText("Ваш хід: поставте X у порожню клітинку.");
        refreshBoard();
    }

    private void humanMove(int row, int col) {
        if (!gameActive || !board.isEmpty(row, col)) {
            return;
        }

        Move humanMove = new Move(row, col);
        board = board.place(humanMove, Cell.HUMAN);
        appendLog("Людина X -> (" + row + ", " + col + ")");
        refreshBoard();

        if (finishIfNeeded(Cell.HUMAN)) {
            return;
        }
        if (board.isFull()) {
            finishDraw();
            return;
        }

        Move computerMove = computerStrategy.chooseMove(board);
        board = board.place(computerMove, Cell.COMPUTER);
        appendLog("Комп'ютер O -> (" + computerMove.row() + ", " + computerMove.col() + ")");
        refreshBoard();

        if (!finishIfNeeded(Cell.COMPUTER) && board.isFull()) {
            finishDraw();
        }
    }

    private boolean finishIfNeeded(Cell cell) {
        if (!board.hasFiveInRow(cell)) {
            statusLabel.setText("Ваш хід: поставте X у порожню клітинку.");
            return false;
        }

        gameActive = false;
        String winner = cell == Cell.HUMAN ? "людина" : "комп'ютер";
        statusLabel.setText("Переможець: " + winner + ".");
        appendLog("Переможець: " + winner + ".");
        refreshBoard();
        return true;
    }

    private void finishDraw() {
        gameActive = false;
        statusLabel.setText("Нічия: поле заповнене.");
        appendLog("Нічия: поле заповнене.");
        refreshBoard();
    }

    private void refreshBoard() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Cell cell = board.get(row, col);
                JButton button = cells[row][col];
                button.setText(cell == Cell.EMPTY ? "" : String.valueOf(cell.symbol()));
                button.setEnabled(gameActive && cell == Cell.EMPTY);
                button.setBackground(colorFor(cell));
                button.setOpaque(true);
            }
        }
    }

    private Color colorFor(Cell cell) {
        return switch (cell) {
            case HUMAN -> new Color(0xDCEBFF);
            case COMPUTER -> new Color(0xFFE7DC);
            case EMPTY -> Color.WHITE;
        };
    }

    private void appendLog(String message) {
        logArea.append(message + System.lineSeparator());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void showAlgorithm() {
        JOptionPane.showMessageDialog(
                this,
                "Комп'ютер використовує доцільну евристику для поля 10x10:\n"
                        + "1. Якщо може одразу отримати п'ять O в ряд, робить цей хід.\n"
                        + "2. Якщо людина може виграти наступним ходом, блокує цю клітинку.\n"
                        + "3. Інакше обирає клітинку з найкращою оцінкою: близькість до центру, сусідство "
                        + "з уже поставленими символами та довжина потенційної лінії.",
                "Алгоритм комп'ютера",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
