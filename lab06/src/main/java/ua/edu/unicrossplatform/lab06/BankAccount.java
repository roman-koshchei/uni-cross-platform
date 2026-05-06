package ua.edu.unicrossplatform.lab06;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

final class BankAccount {
    private final String accountId;
    private final ReentrantLock lock = new ReentrantLock();
    private BigDecimal balance;
    private boolean active = true;

    BankAccount(String accountId, BigDecimal initialBalance) {
        this.accountId = Objects.requireNonNull(accountId, "accountId");
        this.balance = normalize(initialBalance);
    }

    String accountId() {
        return accountId;
    }

    AccountSnapshot snapshot() {
        lock.lock();
        try {
            return new AccountSnapshot(accountId, balance, active);
        } finally {
            lock.unlock();
        }
    }

    BigDecimal deposit(BigDecimal amount) {
        lock.lock();
        try {
            ensureActive();
            balance = balance.add(normalizePositive(amount));
            return balance;
        } finally {
            lock.unlock();
        }
    }

    BigDecimal withdraw(BigDecimal amount) {
        lock.lock();
        try {
            ensureActive();
            BigDecimal normalizedAmount = normalizePositive(amount);
            if (balance.compareTo(normalizedAmount) < 0) {
                throw new IllegalStateException("Insufficient funds");
            }
            balance = balance.subtract(normalizedAmount);
            return balance;
        } finally {
            lock.unlock();
        }
    }

    void close() {
        lock.lock();
        try {
            ensureActive();
            active = false;
        } finally {
            lock.unlock();
        }
    }

    private void ensureActive() {
        if (!active) {
            throw new IllegalStateException("Account is closed");
        }
    }

    private static BigDecimal normalizePositive(BigDecimal amount) {
        BigDecimal normalized = normalize(amount);
        if (normalized.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        return normalized;
    }

    private static BigDecimal normalize(BigDecimal amount) {
        return Objects.requireNonNull(amount, "amount").setScale(2);
    }
}
