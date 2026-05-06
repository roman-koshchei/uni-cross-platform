package ua.edu.unicrossplatform.lab04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class HistoryStore {
    private static final String HEADER = "player,computer,outcome";

    private final Path path;

    public HistoryStore(Path path) {
        this.path = path;
    }

    public void append(Round round) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        if (Files.notExists(path)) {
            Files.writeString(path, HEADER + System.lineSeparator());
        }
        String line = round.playerMove().name() + "," + round.computerMove().name() + "," + round.outcome().name()
                + System.lineSeparator();
        Files.writeString(path, line, java.nio.file.StandardOpenOption.APPEND);
    }

    public List<Round> loadRounds() throws IOException {
        if (Files.notExists(path)) {
            return List.of();
        }

        List<String> lines = Files.readAllLines(path);
        List<Round> rounds = new ArrayList<>();
        for (int index = 1; index < lines.size(); index++) {
            String line = lines.get(index).trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split(",");
            rounds.add(new Round(
                    Move.valueOf(parts[0]),
                    Move.valueOf(parts[1]),
                    Outcome.valueOf(parts[2])
            ));
        }
        return rounds;
    }

    public GameStatistics statistics() throws IOException {
        int playerWins = 0;
        int computerWins = 0;
        int draws = 0;
        for (Round round : loadRounds()) {
            switch (round.outcome()) {
                case PLAYER_WIN -> playerWins++;
                case COMPUTER_WIN -> computerWins++;
                case DRAW -> draws++;
            }
        }
        return new GameStatistics(playerWins, computerWins, draws);
    }
}
