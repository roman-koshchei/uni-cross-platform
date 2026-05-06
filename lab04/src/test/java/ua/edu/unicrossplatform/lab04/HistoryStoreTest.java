package ua.edu.unicrossplatform.lab04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class HistoryStoreTest {
    @Test
    void shouldPersistRoundsAndBuildStatistics(@TempDir Path tempDir) throws IOException {
        Path historyFile = tempDir.resolve("history.csv");
        HistoryStore store = new HistoryStore(historyFile);
        store.append(new Round(Move.WELL, Move.SCISSORS, Outcome.PLAYER_WIN));
        store.append(new Round(Move.PAPER, Move.SCISSORS, Outcome.COMPUTER_WIN));
        store.append(new Round(Move.PAPER, Move.PAPER, Outcome.DRAW));

        List<Round> rounds = store.loadRounds();
        GameStatistics statistics = store.statistics();

        assertEquals(3, rounds.size());
        assertEquals(1, statistics.playerWins());
        assertEquals(1, statistics.computerWins());
        assertEquals(1, statistics.draws());
    }
}
