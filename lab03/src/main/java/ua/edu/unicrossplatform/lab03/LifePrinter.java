package ua.edu.unicrossplatform.lab03;

public final class LifePrinter {
    public String render(LifeBoard board, int generation) {
        StringBuilder builder = new StringBuilder();
        builder.append("Generation ").append(generation).append(System.lineSeparator());
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                builder.append(board.isAlive(row, col) ? '*' : '.');
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
}
