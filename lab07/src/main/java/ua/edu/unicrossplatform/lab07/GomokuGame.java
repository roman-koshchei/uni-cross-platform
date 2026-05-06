package ua.edu.unicrossplatform.lab07;

import java.util.ArrayList;
import java.util.List;

public final class GomokuGame {
    private final PlayerStrategy humanStrategy;
    private final PlayerStrategy computerStrategy;

    public GomokuGame() {
        this(new HumanStrategy(), new ComputerStrategy());
    }

    public GomokuGame(PlayerStrategy humanStrategy, PlayerStrategy computerStrategy) {
        this.humanStrategy = humanStrategy;
        this.computerStrategy = computerStrategy;
    }

    public GameOutcome play() {
        Board board = new Board();
        List<String> log = new ArrayList<>();
        Cell current = Cell.HUMAN;

        while (true) {
            Move move = current == Cell.HUMAN
                ? humanStrategy.chooseMove(board)
                : computerStrategy.chooseMove(board);
            board = board.place(move, current);
            log.add(current + " -> (" + move.row() + ", " + move.col() + ")");

            if (board.hasFiveInRow(current)) {
                return new GameOutcome(current, board, log);
            }
            if (board.isFull()) {
                return new GameOutcome(Cell.EMPTY, board, log);
            }
            current = current.opponent();
        }
    }

    public record GameOutcome(Cell winner, Board board, List<String> log) {
    }
}
