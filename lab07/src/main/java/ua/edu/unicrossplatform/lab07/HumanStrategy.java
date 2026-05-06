package ua.edu.unicrossplatform.lab07;

public final class HumanStrategy implements PlayerStrategy {
    @Override
    public Move chooseMove(Board board) {
        Move winningMove = board.findWinningMove(Cell.HUMAN);
        if (winningMove != null) {
            return winningMove;
        }

        Move blockingMove = board.findWinningMove(Cell.COMPUTER);
        if (blockingMove != null) {
            return blockingMove;
        }

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        for (Move move : board.emptyCells()) {
            int score = board.scoreMove(move, Cell.HUMAN);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        if (bestMove == null) {
            throw new IllegalStateException("No legal moves left");
        }
        return bestMove;
    }
}
