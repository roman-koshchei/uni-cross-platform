package ua.edu.unicrossplatform.lab04;

import java.io.IOException;
import java.util.Scanner;

public final class ConsoleGameRunner {
    public void runInteractive(GameSession session, GameStrategy strategy, HistoryStore historyStore, Scanner scanner)
            throws IOException {
        int targetSessions = readPositiveInt(scanner, "Кількість сеансів: ");
        int completedSessions = 0;
        System.out.println("Гра Криниця-Ножиці-Папір. 1 - Криниця, 2 - Ножиці, 3 - Папір, s - статистика, 0 - вихід");
        while (completedSessions < targetSessions) {
            System.out.print("Сеанс " + (completedSessions + 1) + "/" + targetSessions + ". Ваш вибір: ");
            String input = scanner.nextLine().trim();
            if ("0".equals(input)) {
                break;
            }
            if ("s".equalsIgnoreCase(input)) {
                System.out.println(formatStatistics(historyStore.statistics()));
                continue;
            }

            try {
                Move playerMove = Move.parseMenuChoice(input);
                Round round = session.playRound(playerMove, strategy);
                System.out.println(formatRound(round, strategy.name()));
                if (round.outcome() == Outcome.DRAW) {
                    System.out.println("Нічия: цей сеанс переграється.");
                    continue;
                }
                completedSessions++;
                GameStatistics statistics = historyStore.statistics();
                System.out.println(formatStatistics(statistics));
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
        System.out.println("Завершено сеансів: " + completedSessions + " із " + targetSessions);
    }

    public String formatRound(Round round, String strategyName) {
        return "Стратегія: " + strategyName
                + ", ви: " + round.playerMove().displayName()
                + ", комп'ютер: " + round.computerMove().displayName()
                + ", результат: " + outcomeLabel(round.outcome());
    }

    public String formatStatistics(GameStatistics statistics) {
        return "Статистика -> перемоги: " + statistics.playerWins()
                + ", поразки: " + statistics.computerWins()
                + ", нічиї: " + statistics.draws();
    }

    private String outcomeLabel(Outcome outcome) {
        return switch (outcome) {
            case PLAYER_WIN -> "перемога гравця";
            case COMPUTER_WIN -> "перемога комп'ютера";
            case DRAW -> "нічия";
        };
    }

    private int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // retry below
            }
            System.out.println("Введіть додатне ціле число.");
        }
    }
}
