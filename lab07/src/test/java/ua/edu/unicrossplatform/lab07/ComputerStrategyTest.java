package ua.edu.unicrossplatform.lab07;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComputerStrategyTest {
    @Test
    void choosesImmediateWinningMove() {
        Board board = new Board();
        for (int row = 1; row < 5; row++) {
            board = board.place(new Move(row, 5), Cell.COMPUTER);
        }

        Move move = new ComputerStrategy().chooseMove(board);

        assertEquals(new Move(5, 5), move);
    }

    @Test
    void blocksHumanWinningMove() {
        Board board = new Board();
        for (int col = 2; col < 6; col++) {
            board = board.place(new Move(6, col), Cell.HUMAN);
        }

        Move move = new ComputerStrategy().chooseMove(board);

        assertEquals(new Move(6, 6), move);
    }
}
