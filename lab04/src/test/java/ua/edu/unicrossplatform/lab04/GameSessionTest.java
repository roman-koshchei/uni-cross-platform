package ua.edu.unicrossplatform.lab04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GameSessionTest {
    @Test
    void shouldNotPersistDrawAsCompletedSession(@TempDir Path tempDir) throws IOException {
        HistoryStore store = new HistoryStore(tempDir.resolve("history.csv"));
        GameSession session = new GameSession(store);
        GameStrategy paperStrategy = new GameStrategy() {
            @Override
            public String name() {
                return "paper";
            }

            @Override
            public Move chooseMove(List<Round> sessionHistory) {
                return Move.PAPER;
            }
        };

        Round draw = session.playRound(Move.PAPER, paperStrategy);
        Round decisive = session.playRound(Move.SCISSORS, paperStrategy);

        assertEquals(Outcome.DRAW, draw.outcome());
        assertEquals(Outcome.PLAYER_WIN, decisive.outcome());
        assertEquals(List.of(decisive), store.loadRounds());
    }
}
