package ua.edu.unicrossplatform.lab06;

import java.math.BigDecimal;

public record AccountSnapshot(String accountId, BigDecimal balance, boolean active) {
}
