package ua.edu.unicrossplatform.lab03;

import java.util.Arrays;

public final class LifeBoard {
    private final boolean[][] cells;

    public LifeBoard(boolean[][] cells) {
        this.cells = copyOf(cells);
    }

    public int rows() {
        return cells.length;
    }

    public int cols() {
        return rows() == 0 ? 0 : cells[0].length;
    }

    public boolean isAlive(int row, int col) {
        return cells[row][col];
    }

    public int aliveNeighbors(int row, int col) {
        int neighbors = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }

                int neighborRow = row + rowOffset;
                int neighborCol = col + colOffset;
                if (neighborRow < 0 || neighborRow >= rows() || neighborCol < 0 || neighborCol >= cols()) {
                    continue;
                }

                if (cells[neighborRow][neighborCol]) {
                    neighbors++;
                }
            }
        }
        return neighbors;
    }

    public boolean[][] toArray() {
        return copyOf(cells);
    }

    private static boolean[][] copyOf(boolean[][] source) {
        boolean[][] copy = new boolean[source.length][];
        for (int row = 0; row < source.length; row++) {
            copy[row] = Arrays.copyOf(source[row], source[row].length);
        }
        return copy;
    }
}
