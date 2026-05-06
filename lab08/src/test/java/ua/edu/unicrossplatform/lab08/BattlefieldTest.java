package ua.edu.unicrossplatform.lab08;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattlefieldTest {
    @Test
    void autoPlacementCreatesValidFleet() {
        Battlefield battlefield = Battlefield.autoPlace(new Random(7L));

        assertTrue(battlefield.isValidFleet());
    }

    @Test
    void reportsHitAndSunk() {
        Battlefield battlefield = new Battlefield(List.of(
            new Ship(List.of(new Coordinate(1, 1), new Coordinate(1, 2)))
        ));

        assertEquals(ShotResult.HIT, battlefield.shoot(new Coordinate(1, 1)));
        assertEquals(ShotResult.SUNK, battlefield.shoot(new Coordinate(1, 2)));
        assertTrue(battlefield.allShipsSunk());
    }
}
