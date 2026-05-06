package ua.edu.unicrossplatform.lab07;

import java.util.Comparator;

public final class ComputerStrategy implements PlayerStrategy {
    @Override
    public Move chooseMove(Board board) {
        Move winningMove = findBestCriticalMove(board, Cell.COMPUTER);
        if (winningMove != null) {
            return winningMove;
        }

        Move blockingMove = findBestCriticalMove(board, Cell.HUMAN);
        if (blockingMove != null) {
            return blockingMove;
        }

        return board.emptyCells().stream()
            .max(Comparator
                .comparingInt((Move move) -> board.scoreMove(move, Cell.COMPUTER))
                .thenComparingInt(move -> -Math.abs((Board.SIZE - 1) / 2 - move.row()))
                .thenComparingInt(move -> -Math.abs((Board.SIZE - 1) / 2 - move.col())))
            .orElseThrow(() -> new IllegalStateException("No legal moves left"));
    }

    private Move findBestCriticalMove(Board board, Cell cell) {
        return board.emptyCells().stream()
            .filter(move -> board.place(move, cell).hasFiveInRow(cell))
            .max(Comparator
                .comparingInt((Move move) -> board.scoreMove(move, Cell.COMPUTER))
                .thenComparingInt(move -> -Math.abs((Board.SIZE - 1) / 2 - move.row()))
                .thenComparingInt(move -> -Math.abs((Board.SIZE - 1) / 2 - move.col())))
            .orElse(null);
    }
}
