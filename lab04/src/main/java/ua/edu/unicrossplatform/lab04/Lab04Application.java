package ua.edu.unicrossplatform.lab04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public final class Lab04Application {
    private Lab04Application() {
    }

    public static void main(String[] args) throws IOException {
        Path historyFile = Path.of(System.getProperty("java.io.tmpdir"), "lab04-history-demo.csv");
        HistoryStore historyStore = new HistoryStore(historyFile);
        ConsoleGameRunner runner = new ConsoleGameRunner();
        RandomStrategy random = new RandomStrategy(new Random(42));

        if (args.length > 0 && "--interactive".equals(args[0])) {
            try (Scanner scanner = new Scanner(System.in)) {
                GameStrategy strategy = chooseStrategy(args, historyFile, random);
                runner.runInteractive(new GameSession(historyStore), strategy, historyStore, scanner);
            }
            return;
        }

        Files.deleteIfExists(historyFile);
        runDemo(new HistoryStore(historyFile), runner, random);
    }

    private static GameStrategy chooseStrategy(String[] args, Path historyFile, RandomStrategy random) {
        if (args.length > 1 && "session".equalsIgnoreCase(args[1])) {
            return new SessionAdaptiveStrategy(random);
        }
        if (args.length > 1 && "history".equalsIgnoreCase(args[1])) {
            return new FileAdaptiveStrategy(historyFile, random);
        }
        return random;
    }

    private static void runDemo(HistoryStore historyStore, ConsoleGameRunner runner, RandomStrategy random)
            throws IOException {
        List<GameStrategy> strategies = List.of(
                random,
                new SessionAdaptiveStrategy(random),
                new FileAdaptiveStrategy(Path.of(System.getProperty("java.io.tmpdir"), "lab04-history-demo.csv"), random)
        );
        List<Move> scriptedMoves = List.of(Move.WELL, Move.PAPER, Move.PAPER, Move.SCISSORS, Move.WELL, Move.PAPER);

        for (GameStrategy strategy : strategies) {
            GameSession session = new GameSession(historyStore);
            System.out.println("=== Демонстрація: " + strategy.name() + " ===");
            for (Move scriptedMove : scriptedMoves) {
                Round round = session.playRound(scriptedMove, strategy);
                System.out.println(runner.formatRound(round, strategy.name()));
            }
            System.out.println(runner.formatStatistics(historyStore.statistics()));
            System.out.println();
        }
    }
}
