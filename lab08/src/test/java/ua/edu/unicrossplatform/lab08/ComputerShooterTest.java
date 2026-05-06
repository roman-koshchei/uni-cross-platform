package ua.edu.unicrossplatform.lab08;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ComputerShooterTest {
    @Test
    void neverRepeatsShots() {
        ComputerShooter shooter = new ComputerShooter(new Random(11L));
        Set<Coordinate> seen = new HashSet<>();

        for (int index = 0; index < Battlefield.SIZE * Battlefield.SIZE; index++) {
            Coordinate shot = shooter.nextShot();
            assertTrue(seen.add(shot));
            shooter.acceptResult(shot, ShotResult.MISS);
        }
    }

    @Test
    void targetsNeighborsAfterHit() {
        ComputerShooter shooter = new ComputerShooter(new Random(3L));
        Coordinate initial = shooter.nextShot();
        shooter.acceptResult(initial, ShotResult.HIT);

        Coordinate next = shooter.nextShot();
        int distance = Math.abs(initial.row() - next.row()) + Math.abs(initial.col() - next.col());

        assertTrue(distance == 1);
    }
}
