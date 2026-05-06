package ua.edu.unicrossplatform.lab03;

public final class LifeEngine {
    public LifeBoard nextGeneration(LifeBoard board) {
        boolean[][] next = new boolean[board.rows()][board.cols()];
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                int neighbors = board.aliveNeighbors(row, col);
                boolean alive = board.isAlive(row, col);
                next[row][col] = neighbors == 3 || (alive && neighbors == 2);
            }
        }
        return new LifeBoard(next);
    }
}
