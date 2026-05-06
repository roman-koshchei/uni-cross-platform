package ua.edu.unicrossplatform.lab07;

import java.util.ArrayList;
import java.util.List;

public final class Board {
    public static final int SIZE = 10;
    private static final int[][] DIRECTIONS = {
        {0, 1},
        {1, 0},
        {1, 1},
        {1, -1}
    };

    private final Cell[][] cells;

    public Board() {
        this.cells = new Cell[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = Cell.EMPTY;
            }
        }
    }

    private Board(Cell[][] cells) {
        this.cells = cells;
    }

    public Cell get(int row, int col) {
        return cells[row][col];
    }

    public boolean isInside(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public boolean isEmpty(int row, int col) {
        return get(row, col) == Cell.EMPTY;
    }

    public Board place(Move move, Cell cell) {
        if (!isInside(move.row(), move.col())) {
            throw new IllegalArgumentException("Move is outside the board");
        }
        if (!isEmpty(move.row(), move.col())) {
            throw new IllegalArgumentException("Cell is already occupied");
        }
        Cell[][] copy = copyCells();
        copy[move.row()][move.col()] = cell;
        return new Board(copy);
    }

    public boolean isFull() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col] == Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasFiveInRow(Cell cell) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col] == cell && completesFive(row, col, cell)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Move findWinningMove(Cell cell) {
        for (Move move : emptyCells()) {
            if (place(move, cell).hasFiveInRow(cell)) {
                return move;
            }
        }
        return null;
    }

    public List<Move> emptyCells() {
        List<Move> moves = new ArrayList<>();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col] == Cell.EMPTY) {
                    moves.add(new Move(row, col));
                }
            }
        }
        return moves;
    }

    public int scoreMove(Move move, Cell cell) {
        int center = (SIZE - 1) / 2;
        int score = 40 - Math.abs(center - move.row()) - Math.abs(center - move.col());
        for (int row = move.row() - 1; row <= move.row() + 1; row++) {
            for (int col = move.col() - 1; col <= move.col() + 1; col++) {
                if (!isInside(row, col) || (row == move.row() && col == move.col())) {
                    continue;
                }
                if (get(row, col) == cell) {
                    score += 12;
                } else if (get(row, col) == cell.opponent()) {
                    score += 5;
                }
            }
        }
        Board future = place(move, cell);
        score += future.longestLine(move, cell) * 20;
        return score;
    }

    public String render() {
        StringBuilder builder = new StringBuilder();
        builder.append("   ");
        for (int col = 0; col < SIZE; col++) {
            builder.append(col).append(' ');
        }
        builder.append(System.lineSeparator());
        for (int row = 0; row < SIZE; row++) {
            if (row < 10) {
                builder.append(' ');
            }
            builder.append(row).append(' ');
            for (int col = 0; col < SIZE; col++) {
                builder.append(cells[row][col].symbol()).append(' ');
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    private boolean completesFive(int row, int col, Cell cell) {
        for (int[] direction : DIRECTIONS) {
            int count = 1 + countDirection(row, col, direction[0], direction[1], cell)
                + countDirection(row, col, -direction[0], -direction[1], cell);
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }

    private int longestLine(Move move, Cell cell) {
        int best = 1;
        for (int[] direction : DIRECTIONS) {
            int count = 1 + countDirection(move.row(), move.col(), direction[0], direction[1], cell)
                + countDirection(move.row(), move.col(), -direction[0], -direction[1], cell);
            best = Math.max(best, count);
        }
        return best;
    }

    private int countDirection(int row, int col, int rowStep, int colStep, Cell cell) {
        int count = 0;
        int currentRow = row + rowStep;
        int currentCol = col + colStep;
        while (isInside(currentRow, currentCol) && get(currentRow, currentCol) == cell) {
            count++;
            currentRow += rowStep;
            currentCol += colStep;
        }
        return count;
    }

    private Cell[][] copyCells() {
        Cell[][] copy = new Cell[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(cells[row], 0, copy[row], 0, SIZE);
        }
        return copy;
    }
}
