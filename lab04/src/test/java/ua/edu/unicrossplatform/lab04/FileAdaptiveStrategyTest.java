package ua.edu.unicrossplatform.lab04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileAdaptiveStrategyTest {
    @Test
    void shouldAdaptToPersistedHistory(@TempDir Path tempDir) throws IOException {
        Path historyFile = tempDir.resolve("history.csv");
        HistoryStore store = new HistoryStore(historyFile);
        store.append(new Round(Move.SCISSORS, Move.WELL, Outcome.COMPUTER_WIN));
        store.append(new Round(Move.SCISSORS, Move.WELL, Outcome.COMPUTER_WIN));
        store.append(new Round(Move.PAPER, Move.SCISSORS, Outcome.COMPUTER_WIN));

        FileAdaptiveStrategy strategy = new FileAdaptiveStrategy(historyFile, new RandomStrategy(new Random(0)));

        assertEquals(Move.WELL, strategy.chooseMove(List.of()));
    }
}
