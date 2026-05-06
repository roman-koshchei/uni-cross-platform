package ua.edu.unicrossplatform.lab05;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public final class CoinGameDemo {
    private static final int MAX_TAKE = 2;

    private CoinGameDemo() {
    }

    public static void main(String[] args) {
        boolean interactive = shouldRunInteractive(args);
        Random random = interactive ? new Random() : new Random(5L);
        CoinGameEngine engine = new CoinGameEngine(MAX_TAKE);

        int startingCoins = random.nextInt(11) + 15;
        boolean humanStarts = random.nextBoolean();
        CoinPlayer humanPlayer = interactive
                ? new ConsoleCoinPlayer(new Scanner(System.in))
                : new ScriptedCoinPlayer(buildHumanMoves(random, startingCoins, MAX_TAKE));

        CoinGameResult result = humanStarts
                ? engine.play(startingCoins, "Людина", humanPlayer,
                "Комп'ютер", new OptimalCoinPlayer())
                : engine.play(startingCoins, "Комп'ютер", new OptimalCoinPlayer(),
                "Людина", humanPlayer);

        System.out.println("=== Лабораторна 5. Гра в монети ===");
        System.out.println("Початково монет: " + result.startingCoins());
        System.out.println("Перший гравець: " + result.firstPlayer());
        for (CoinMove move : result.moves()) {
            System.out.println(move.playerName() + " бере " + move.takenCoins()
                    + ", залишилось " + move.coinsLeft());
        }
        System.out.println("Переможець: " + result.winner());
    }

    private static boolean shouldRunInteractive(String[] args) {
        for (String arg : args) {
            if ("--interactive".equalsIgnoreCase(arg)) {
                return true;
            }
            if ("--demo".equalsIgnoreCase(arg)) {
                return false;
            }
        }
        return System.console() != null;
    }

    private static List<Integer> buildHumanMoves(Random random, int startingCoins, int maxTake) {
        int estimatedTurns = Math.max(8, startingCoins);
        List<Integer> moves = new ArrayList<>(estimatedTurns);
        for (int i = 0; i < estimatedTurns; i++) {
            moves.add(random.nextInt(maxTake) + 1);
        }
        return moves;
    }
}
