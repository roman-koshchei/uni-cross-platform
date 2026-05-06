package ua.edu.unicrossplatform.lab04;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class GameSession {
    private final HistoryStore historyStore;
    private final List<Round> sessionHistory = new ArrayList<>();

    public GameSession(HistoryStore historyStore) {
        this.historyStore = historyStore;
    }

    public Round playRound(Move playerMove, GameStrategy strategy) throws IOException {
        Move computerMove = strategy.chooseMove(List.copyOf(sessionHistory));
        Outcome outcome = playerMove.playAgainst(computerMove);
        Round round = new Round(playerMove, computerMove, outcome);
        sessionHistory.add(round);
        if (outcome != Outcome.DRAW) {
            historyStore.append(round);
        }
        return round;
    }

    public List<Round> sessionHistory() {
        return List.copyOf(sessionHistory);
    }
}
