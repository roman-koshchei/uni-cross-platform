package ua.edu.unicrossplatform.lab08;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public record GameReport(String winner, List<String> log, boolean firstFleetValid, boolean secondFleetValid) {
    }
}
