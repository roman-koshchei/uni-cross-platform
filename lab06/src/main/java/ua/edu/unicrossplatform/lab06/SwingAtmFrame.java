package ua.edu.unicrossplatform.lab06;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public final class SwingAtmFrame extends JFrame {
    private static final BigDecimal WITHDRAW_LIMIT = new BigDecimal("200.00");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
            .withZone(ZoneId.systemDefault());

    private final OperationLog operationLog = new OperationLog();
    private final AtmService atmService = new AtmService(WITHDRAW_LIMIT, operationLog);
    private final ExecutorService executor = Executors.newFixedThreadPool(4, new AtmThreadFactory());
    private final Set<String> knownAccounts = Collections.synchronizedSet(new LinkedHashSet<>());

    private final JTextField accountField = new JTextField("ACC-1001", 12);
    private final JTextField amountField = new JTextField("100.00", 10);
    private final JLabel statusLabel = new JLabel("Оберіть операцію або запустіть одночасну симуляцію.", SwingConstants.CENTER);
    private final JLabel limitLabel = new JLabel("Ліміт одноразового зняття: " + WITHDRAW_LIMIT + " грн", SwingConstants.CENTER);
    private final JTextArea accountsArea = new JTextArea(8, 26);
    private final JTextArea logArea = new JTextArea(16, 56);

    public SwingAtmFrame() {
        super("Лабораторна 06: Симуляція банкомату");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(780, 620));
        setLocationByPlatform(true);
        setLayout(new BorderLayout(12, 12));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                executor.shutdownNow();
            }
        });

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createLogPanel(), BorderLayout.SOUTH);

        accountsArea.setEditable(false);
        logArea.setEditable(false);
        refreshViews();
        pack();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xD6DCE5)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Симуляція роботи банкомату");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        JLabel subtitle = new JLabel("Операції виконуються у пулі потоків, тому кілька користувачів можуть працювати одночасно.");

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

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.add(createControlsPanel(), BorderLayout.CENTER);
        panel.add(createAccountsPanel(), BorderLayout.EAST);
        return panel;
    }

    private JPanel createControlsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        inputPanel.add(new JLabel("Рахунок:"));
        inputPanel.add(accountField);
        inputPanel.add(new JLabel("Сума:"));
        inputPanel.add(amountField);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.add(operationButton("Відкрити рахунок", () -> openAccount(accountId(), amount())));
        buttonPanel.add(operationButton("Закрити рахунок", () -> closeAccount(accountId())));
        buttonPanel.add(operationButton("Поповнити", () -> deposit(accountId(), amount())));
        buttonPanel.add(operationButton("Зняти", () -> withdraw(accountId(), amount())));
        buttonPanel.add(operationButton("Оновити журнал", this::refreshViews));
        buttonPanel.add(operationButton("Одночасна симуляція", this::runConcurrentSimulation));

        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 15f));
        limitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);
        panel.add(Box.createVerticalStrut(16));
        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(limitLabel);
        return panel;
    }

    private JPanel createAccountsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(260, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Рахунки"));
        accountsArea.setLineWrap(true);
        accountsArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(accountsArea), BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane createLogPanel() {
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Журнал операцій"));
        return scrollPane;
    }

    private JButton operationButton(String label, Runnable action) {
        JButton button = new JButton(label);
        button.addActionListener(event -> {
            try {
                action.run();
            } catch (IllegalArgumentException ignored) {
                // Validation errors are shown next to the field or in a dialog.
            }
        });
        return button;
    }

    private void openAccount(String accountId, BigDecimal amount) {
        submit("Відкриття рахунку " + accountId, () -> {
            atmService.openAccount(accountId, amount);
            knownAccounts.add(accountId);
        });
    }

    private void closeAccount(String accountId) {
        submit("Закриття рахунку " + accountId, () -> {
            atmService.closeAccount(accountId);
            knownAccounts.remove(accountId);
        });
    }

    private void deposit(String accountId, BigDecimal amount) {
        submit("Поповнення рахунку " + accountId, () -> atmService.deposit(accountId, amount));
    }

    private void withdraw(String accountId, BigDecimal amount) {
        submit("Зняття з рахунку " + accountId, () -> atmService.withdraw(accountId, amount));
    }

    private void runConcurrentSimulation() {
        statusLabel.setText("Запущено одночасну симуляцію для кількох користувачів...");
        openIfMissing("ACC-1001", new BigDecimal("500.00"));
        openIfMissing("ACC-1002", new BigDecimal("300.00"));
        refreshViews();
        submit("Користувач 1 поповнює ACC-1001", () -> atmService.deposit("ACC-1001", new BigDecimal("50.00")));
        submit("Користувач 2 знімає ACC-1001", () -> atmService.withdraw("ACC-1001", new BigDecimal("120.00")));
        submit("Користувач 3 поповнює ACC-1002", () -> atmService.deposit("ACC-1002", new BigDecimal("80.00")));
        submit("Користувач 4 знімає ACC-1002", () -> atmService.withdraw("ACC-1002", new BigDecimal("70.00")));
    }

    private void openIfMissing(String accountId, BigDecimal amount) {
        if (knownAccounts.contains(accountId)) {
            return;
        }
        try {
            atmService.openAccount(accountId, amount);
            knownAccounts.add(accountId);
        } catch (IllegalStateException ignored) {
            knownAccounts.add(accountId);
        }
    }

    private void submit(String description, Runnable action) {
        executor.submit(() -> {
            try {
                action.run();
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText(description + " виконано.");
                    refreshViews();
                });
            } catch (RuntimeException exception) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText(description + " відхилено: " + userMessage(exception));
                    refreshViews();
                });
            }
        });
    }

    private void refreshViews() {
        refreshAccounts();
        refreshLog();
    }

    private void refreshAccounts() {
        StringBuilder builder = new StringBuilder();
        synchronized (knownAccounts) {
            for (String accountId : knownAccounts) {
                try {
                    AccountSnapshot snapshot = atmService.getAccount(accountId);
                    builder.append(snapshot.accountId())
                            .append(" | баланс: ")
                            .append(snapshot.balance())
                            .append(snapshot.active() ? " | активний" : " | закритий")
                            .append(System.lineSeparator());
                } catch (RuntimeException ignored) {
                    // The account may have been closed by another concurrent operation.
                }
            }
        }
        accountsArea.setText(builder.isEmpty() ? "Немає відкритих рахунків." : builder.toString());
    }

    private void refreshLog() {
        List<OperationLogEntry> entries = atmService.operationHistory();
        StringBuilder builder = new StringBuilder();
        for (OperationLogEntry entry : entries) {
            builder.append(TIME_FORMATTER.format(entry.timestamp()))
                    .append(" | ")
                    .append(entry.accountId())
                    .append(" | ")
                    .append(operationLabel(entry.type()))
                    .append(" | ")
                    .append(entry.amount())
                    .append(" | ")
                    .append(entry.status())
                    .append(System.lineSeparator());
        }
        logArea.setText(builder.toString());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private String accountId() {
        String accountId = accountField.getText().trim();
        if (accountId.isEmpty()) {
            throwInputError("Введіть номер рахунку.");
        }
        return accountId;
    }

    private BigDecimal amount() {
        try {
            return new BigDecimal(amountField.getText().trim());
        } catch (NumberFormatException exception) {
            throwInputError("Введіть коректну суму, наприклад 100.00.");
            throw exception;
        }
    }

    private void throwInputError(String message) {
        JOptionPane.showMessageDialog(this, message, "Некоректні дані", JOptionPane.WARNING_MESSAGE);
        throw new IllegalArgumentException(message);
    }

    private String operationLabel(OperationType type) {
        return switch (type) {
            case OPEN_ACCOUNT -> "відкриття";
            case CLOSE_ACCOUNT -> "закриття";
            case DEPOSIT -> "поповнення";
            case WITHDRAW -> "зняття";
            case REJECTED -> "відхилено";
        };
    }

    private String userMessage(RuntimeException exception) {
        return switch (exception.getMessage()) {
            case "Account already exists" -> "рахунок вже існує";
            case "Account not found" -> "рахунок не знайдено";
            case "Withdraw limit exceeded" -> "перевищено ліміт зняття";
            case "Insufficient funds" -> "недостатньо коштів";
            case "Amount must be positive" -> "сума має бути додатною";
            case "Amount must be non-negative" -> "сума не може бути від'ємною";
            default -> exception.getMessage();
        };
    }

    private static final class AtmThreadFactory implements ThreadFactory {
        private final AtomicInteger counter = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "atm-user-" + counter.getAndIncrement());
        }
    }
}
