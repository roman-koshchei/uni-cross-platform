package ua.edu.unicrossplatform.lab04;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

final class StrategySupport {
    private StrategySupport() {
    }

    static Move mostFrequentPlayerMove(List<Round> rounds) {
        if (rounds.isEmpty()) {
            return null;
        }

        Map<Move, Integer> counts = new EnumMap<>(Move.class);
        for (Move move : Move.values()) {
            counts.put(move, 0);
        }

        for (Round round : rounds) {
            counts.merge(round.playerMove(), 1, Integer::sum);
        }

        Move bestMove = null;
        int bestCount = -1;
        for (Move move : Move.values()) {
            int current = counts.get(move);
            if (current > bestCount) {
                bestCount = current;
                bestMove = move;
            }
        }
        return bestCount <= 0 ? null : bestMove;
    }
}
