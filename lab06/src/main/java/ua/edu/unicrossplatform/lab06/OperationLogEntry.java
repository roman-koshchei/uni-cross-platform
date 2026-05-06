package ua.edu.unicrossplatform.lab06;

import java.math.BigDecimal;
import java.time.Instant;

public record OperationLogEntry(
        Instant timestamp,
        String accountId,
        OperationType type,
        BigDecimal amount,
        String status
) {
}
