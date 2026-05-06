package ua.edu.unicrossplatform.lab04;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class FileAdaptiveStrategy implements GameStrategy {
    private final Path historyFile;
    private final GameStrategy fallback;

    public FileAdaptiveStrategy(Path historyFile, GameStrategy fallback) {
        this.historyFile = historyFile;
        this.fallback = fallback;
    }

    @Override
    public String name() {
        return "Адаптивна до історії";
    }

    @Override
    public Move chooseMove(List<Round> sessionHistory) {
        try {
            Move predicted = StrategySupport.mostFrequentPlayerMove(new HistoryStore(historyFile).loadRounds());
            return predicted == null ? fallback.chooseMove(sessionHistory) : predicted.counterMove();
        } catch (IOException exception) {
            return fallback.chooseMove(sessionHistory);
        }
    }
}
