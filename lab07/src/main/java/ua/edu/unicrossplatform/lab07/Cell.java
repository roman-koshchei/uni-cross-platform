package ua.edu.unicrossplatform.lab07;

public enum Cell {
    EMPTY('.'),
    HUMAN('X'),
    COMPUTER('O');

    private final char symbol;

    Cell(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return symbol;
    }

    public Cell opponent() {
        return switch (this) {
            case HUMAN -> COMPUTER;
            case COMPUTER -> HUMAN;
            case EMPTY -> EMPTY;
        };
    }
}
