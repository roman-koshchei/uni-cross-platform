package ua.edu.unicrossplatform.lab05;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

public final class ScriptedCoinPlayer implements CoinPlayer {
    private final Deque<Integer> moves;

    public ScriptedCoinPlayer(Collection<Integer> moves) {
        this.moves = new ArrayDeque<>(moves);
    }

    @Override
    public int chooseMove(int coinsLeft, int maxTake) {
        if (!moves.isEmpty()) {
            return moves.removeFirst();
        }
        return Math.min(1, coinsLeft);
    }
}
