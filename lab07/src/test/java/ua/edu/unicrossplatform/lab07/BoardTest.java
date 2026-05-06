package ua.edu.unicrossplatform.lab07;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {
    @Test
    void detectsHorizontalFive() {
        Board board = new Board();
        for (int col = 1; col <= 5; col++) {
            board = board.place(new Move(3, col), Cell.HUMAN);
        }

        assertTrue(board.hasFiveInRow(Cell.HUMAN));
    }

    @Test
    void detectsDiagonalFive() {
        Board board = new Board();
        for (int index = 0; index < 5; index++) {
            board = board.place(new Move(index + 2, index + 1), Cell.COMPUTER);
        }

        assertTrue(board.hasFiveInRow(Cell.COMPUTER));
    }

    @Test
    void findsWinningMove() {
        Board board = new Board();
        for (int col = 0; col < 4; col++) {
            board = board.place(new Move(4, col), Cell.HUMAN);
        }

        assertEquals(new Move(4, 4), board.findWinningMove(Cell.HUMAN));
    }
}
