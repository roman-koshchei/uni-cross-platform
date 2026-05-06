package ua.edu.unicrossplatform.lab06;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class AtmService {
    private final BigDecimal withdrawLimit;
    private final ConcurrentMap<String, BankAccount> accounts = new ConcurrentHashMap<>();
    private final OperationLog operationLog;

    public AtmService(BigDecimal withdrawLimit, OperationLog operationLog) {
        this.withdrawLimit = normalizePositive(withdrawLimit);
        this.operationLog = Objects.requireNonNull(operationLog, "operationLog");
    }

    public AccountSnapshot openAccount(String accountId, BigDecimal initialBalance) {
        BigDecimal normalizedBalance = normalizeNonNegative(initialBalance);
        BankAccount account = new BankAccount(accountId, normalizedBalance);
        BankAccount previous = accounts.putIfAbsent(accountId, account);
        if (previous != null) {
            operationLog.record(accountId, OperationType.REJECTED, normalizedBalance, "Рахунок вже існує");
            throw new IllegalStateException("Account already exists");
        }
        operationLog.record(accountId, OperationType.OPEN_ACCOUNT, normalizedBalance, "Рахунок відкрито");
        return account.snapshot();
    }

    public void closeAccount(String accountId) {
        BankAccount account = requireAccount(accountId);
        try {
            account.close();
            accounts.remove(accountId, account);
            operationLog.record(accountId, OperationType.CLOSE_ACCOUNT, BigDecimal.ZERO.setScale(2), "Рахунок закрито");
        } catch (RuntimeException ex) {
            operationLog.record(accountId, OperationType.REJECTED, BigDecimal.ZERO.setScale(2), ex.getMessage());
            throw ex;
        }
    }

    public AccountSnapshot deposit(String accountId, BigDecimal amount) {
        BankAccount account = requireAccount(accountId);
        BigDecimal normalizedAmount = normalizePositive(amount);
        try {
            BigDecimal balance = account.deposit(normalizedAmount);
            operationLog.record(accountId, OperationType.DEPOSIT, normalizedAmount, "Баланс: " + balance);
            return account.snapshot();
        } catch (RuntimeException ex) {
            operationLog.record(accountId, OperationType.REJECTED, normalizedAmount, ex.getMessage());
            throw ex;
        }
    }

    public AccountSnapshot withdraw(String accountId, BigDecimal amount) {
        BankAccount account = requireAccount(accountId);
        BigDecimal normalizedAmount = normalizePositive(amount);
        if (normalizedAmount.compareTo(withdrawLimit) > 0) {
            operationLog.record(accountId, OperationType.REJECTED, normalizedAmount, "Перевищено ліміт зняття");
            throw new IllegalArgumentException("Withdraw limit exceeded");
        }
        try {
            BigDecimal balance = account.withdraw(normalizedAmount);
            operationLog.record(accountId, OperationType.WITHDRAW, normalizedAmount, "Баланс: " + balance);
            return account.snapshot();
        } catch (RuntimeException ex) {
            operationLog.record(accountId, OperationType.REJECTED, normalizedAmount, ex.getMessage());
            throw ex;
        }
    }

    public AccountSnapshot getAccount(String accountId) {
        return requireAccount(accountId).snapshot();
    }

    public List<OperationLogEntry> operationHistory() {
        return operationLog.entries();
    }

    private BankAccount requireAccount(String accountId) {
        BankAccount account = accounts.get(accountId);
        if (account == null) {
            operationLog.record(accountId, OperationType.REJECTED, BigDecimal.ZERO.setScale(2), "Рахунок не знайдено");
            throw new IllegalStateException("Account not found");
        }
        return account;
    }

    private static BigDecimal normalizePositive(BigDecimal amount) {
        BigDecimal normalized = Objects.requireNonNull(amount, "amount").setScale(2);
        if (normalized.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        return normalized;
    }

    private static BigDecimal normalizeNonNegative(BigDecimal amount) {
        BigDecimal normalized = Objects.requireNonNull(amount, "amount").setScale(2);
        if (normalized.signum() < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        return normalized;
    }
}
