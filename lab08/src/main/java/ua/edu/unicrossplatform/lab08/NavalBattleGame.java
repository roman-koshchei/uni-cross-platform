package ua.edu.unicrossplatform.lab08;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public final class NavalBattleGame {
    private final Random random;

    public NavalBattleGame(Random random) {
        this.random = random;
    }

    public GameReport play() {
        Battlefield firstField = Battlefield.autoPlace(new Random(random.nextLong()));
        Battlefield secondField = Battlefield.autoPlace(new Random(random.nextLong()));
        ComputerShooter firstShooter = new ComputerShooter(new Random(random.nextLong()));
        ComputerShooter secondShooter = new ComputerShooter(new Random(random.nextLong()));
        String currentPlayer = random.nextBoolean() ? "Гравець A" : "Гравець B";
        List<String> log = new ArrayList<>();

        while (true) {
            Battlefield enemyField = currentPlayer.equals("Гравець A") ? secondField : firstField;
            ComputerShooter shooter = currentPlayer.equals("Гравець A") ? firstShooter : secondShooter;
            Coordinate shot = shooter.nextShot();
            ShotResult result = enemyField.shoot(shot);
            shooter.acceptResult(shot, result);
            log.add(currentPlayer + " стріляє в (" + shot.row() + ", " + shot.col() + ") -> " + result);

            if (enemyField.allShipsSunk()) {
                return new GameReport(currentPlayer, log, firstField.isValidFleet(), secondField.isValidFleet());
            }
            if (result == ShotResult.MISS) {
                currentPlayer = currentPlayer.equals("Гравець A") ? "Гравець B" : "Гравець A";
            }
        }
    }

    public GameReport playInteractive(Scanner scanner) {
        Battlefield humanField = Battlefield.autoPlace(new Random(random.nextLong()));
        Battlefield computerField = Battlefield.autoPlace(new Random(random.nextLong()));
        ComputerShooter computerShooter = new ComputerShooter(new Random(random.nextLong()));
        boolean humanTurn = random.nextBoolean();
        List<String> log = new ArrayList<>();

        System.out.println("Ваше поле:");
        System.out.print(humanField.renderOwn());
        System.out.println("Перший хід: " + (humanTurn ? "користувач" : "комп'ютер"));

        while (true) {
            if (humanTurn) {
                Coordinate shot = readHumanShot(scanner, computerField);
                ShotResult result = computerField.shoot(shot);
                String line = "Користувач стріляє в " + shot.label() + " -> " + label(result);
                log.add(line);
                System.out.println(line);
                System.out.println("Карта пострілів по супернику:");
                System.out.print(computerField.renderOpponent());
                if (computerField.allShipsSunk()) {
                    return new GameReport("Користувач", log, humanField.isValidFleet(), computerField.isValidFleet());
                }
                humanTurn = result != ShotResult.MISS;
            } else {
                Coordinate shot = computerShooter.nextShot();
                ShotResult result = humanField.shoot(shot);
                computerShooter.acceptResult(shot, result);
                String line = "Комп'ютер стріляє в " + shot.label() + " -> " + label(result);
                log.add(line);
                System.out.println(line);
                if (humanField.allShipsSunk()) {
                    return new GameReport("Комп'ютер", log, humanField.isValidFleet(), computerField.isValidFleet());
                }
                humanTurn = result == ShotResult.MISS;
            }
        }
    }

    private Coordinate readHumanShot(Scanner scanner, Battlefield targetField) {
        while (true) {
            System.out.print("Ваш постріл (наприклад A1 або '0 0'): ");
            try {
                Coordinate coordinate = Coordinate.parse(scanner.nextLine());
                if (coordinate.row() < 0 || coordinate.row() >= Battlefield.SIZE
                        || coordinate.col() < 0 || coordinate.col() >= Battlefield.SIZE) {
                    System.out.println("Координата поза полем 10x10.");
                    continue;
                }
                if (targetField.wasShot(coordinate)) {
                    System.out.println("У цю клітинку вже стріляли.");
                    continue;
                }
                return coordinate;
            } catch (RuntimeException exception) {
                System.out.println("Некоректна координата.");
            }
        }
    }

    private String label(ShotResult result) {
        return switch (result) {
            case MISS -> "Мимо";
            case HIT -> "Влучив";
            case SUNK -> "Потопив";
        };
    }

    public record GameReport(String winner, List<String> log, boolean firstFleetValid, boolean secondFleetValid) {
    }
}
