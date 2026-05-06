package ua.edu.unicrossplatform.lab04;

public enum Move {
    WELL("Криниця"),
    SCISSORS("Ножиці"),
    PAPER("Папір");

    private final String displayName;

    Move(String displayName) {
        this.displayName = displayName;
    }

    public Outcome playAgainst(Move other) {
        if (this == other) {
            return Outcome.DRAW;
        }
        if ((this == WELL && other == SCISSORS)
                || (this == SCISSORS && other == PAPER)
                || (this == PAPER && other == WELL)) {
            return Outcome.PLAYER_WIN;
        }
        return Outcome.COMPUTER_WIN;
    }

    public Move counterMove() {
        return switch (this) {
            case WELL -> PAPER;
            case SCISSORS -> WELL;
            case PAPER -> SCISSORS;
        };
    }

    public String displayName() {
        return displayName;
    }

    public static Move parseMenuChoice(String value) {
        return switch (value.trim()) {
            case "1" -> WELL;
            case "2" -> SCISSORS;
            case "3" -> PAPER;
            default -> throw new IllegalArgumentException("Невідомий вибір: " + value);
        };
    }
}
