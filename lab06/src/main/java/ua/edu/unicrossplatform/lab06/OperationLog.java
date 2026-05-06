package ua.edu.unicrossplatform.lab06;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class OperationLog {
    private final ConcurrentLinkedQueue<OperationLogEntry> entries = new ConcurrentLinkedQueue<>();

    public void record(String accountId, OperationType type, BigDecimal amount, String status) {
        entries.add(new OperationLogEntry(Instant.now(), accountId, type, amount.setScale(2), status));
    }

    public List<OperationLogEntry> entries() {
        return List.copyOf(new ArrayList<>(entries));
    }
}
