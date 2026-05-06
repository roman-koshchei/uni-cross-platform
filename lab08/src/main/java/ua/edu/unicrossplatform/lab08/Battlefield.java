package ua.edu.unicrossplatform.lab08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public final class Battlefield {
    public static final int SIZE = 10;
    public static final int[] FLEET = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

    private final List<Ship> ships;
    private final Map<Coordinate, Ship> occupiedCells;
    private final Set<Coordinate> shots;

    public Battlefield(List<Ship> ships) {
        this(ships, new HashSet<>());
    }

    private Battlefield(List<Ship> ships, Set<Coordinate> shots) {
        this.ships = List.copyOf(ships);
        this.occupiedCells = new HashMap<>();
        for (Ship ship : ships) {
            for (Coordinate cell : ship.cells()) {
                occupiedCells.put(cell, ship);
            }
        }
        this.shots = shots;
    }

    public static Battlefield autoPlace(Random random) {
        List<Ship> ships = new ArrayList<>();
        Set<Coordinate> blocked = new HashSet<>();
        for (int size : FLEET) {
            Ship ship = placeShip(size, random, blocked);
            ships.add(ship);
            blocked.addAll(neighborhood(ship));
        }
        return new Battlefield(ships);
    }

    public boolean isValidFleet() {
        Map<Integer, Integer> counts = new HashMap<>();
        Set<Coordinate> occupied = new HashSet<>();
        for (Ship ship : ships) {
            counts.merge(ship.size(), 1, Integer::sum);
            for (Coordinate cell : ship.cells()) {
                if (!isInside(cell)) {
                    return false;
                }
                if (!occupied.add(cell)) {
                    return false;
                }
            }
            if (!isStraight(ship.cells())) {
                return false;
            }
        }
        if (!expectedFleetCounts().equals(counts)) {
            return false;
        }
        for (Ship ship : ships) {
            for (Coordinate neighbor : neighborhood(ship)) {
                Ship other = occupiedCells.get(neighbor);
                if (other != null && other != ship) {
                    return false;
                }
            }
        }
        return true;
    }

    public ShotResult shoot(Coordinate coordinate) {
        if (!isInside(coordinate)) {
            throw new IllegalArgumentException("Shot is outside the battlefield");
        }
        if (!shots.add(coordinate)) {
            throw new IllegalArgumentException("Repeated shot");
        }
        Ship ship = occupiedCells.get(coordinate);
        if (ship == null) {
            return ShotResult.MISS;
        }
        ship.hit(coordinate);
        return ship.isSunk() ? ShotResult.SUNK : ShotResult.HIT;
    }

    public boolean wasShot(Coordinate coordinate) {
        return shots.contains(coordinate);
    }

    public boolean allShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public List<Ship> ships() {
        return ships;
    }

    public String renderOwn() {
        return render(true);
    }

    public String renderOpponent() {
        return render(false);
    }

    private String render(boolean revealShips) {
        StringBuilder builder = new StringBuilder();
        builder.append("   A B C D E F G H I J").append(System.lineSeparator());
        for (int row = 0; row < SIZE; row++) {
            if (row + 1 < 10) {
                builder.append(' ');
            }
            builder.append(row + 1).append(' ');
            for (int col = 0; col < SIZE; col++) {
                Coordinate coordinate = new Coordinate(row, col);
                Ship ship = occupiedCells.get(coordinate);
                char symbol = '.';
                if (shots.contains(coordinate)) {
                    symbol = ship == null ? 'o' : 'X';
                } else if (revealShips && ship != null) {
                    symbol = '#';
                }
                builder.append(symbol).append(' ');
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    private static Ship placeShip(int size, Random random, Set<Coordinate> blocked) {
        while (true) {
            Orientation orientation = random.nextBoolean() ? Orientation.HORIZONTAL : Orientation.VERTICAL;
            int rowLimit = orientation == Orientation.HORIZONTAL ? SIZE : SIZE - size + 1;
            int colLimit = orientation == Orientation.HORIZONTAL ? SIZE - size + 1 : SIZE;
            int row = random.nextInt(rowLimit);
            int col = random.nextInt(colLimit);
            List<Coordinate> cells = new ArrayList<>();
            boolean valid = true;
            for (int index = 0; index < size; index++) {
                Coordinate coordinate = orientation == Orientation.HORIZONTAL
                    ? new Coordinate(row, col + index)
                    : new Coordinate(row + index, col);
                if (blocked.contains(coordinate)) {
                    valid = false;
                    break;
                }
                cells.add(coordinate);
            }
            if (valid) {
                return new Ship(cells);
            }
        }
    }

    private static boolean isStraight(List<Coordinate> cells) {
        boolean sameRow = cells.stream().map(Coordinate::row).distinct().count() == 1;
        boolean sameCol = cells.stream().map(Coordinate::col).distinct().count() == 1;
        if (sameRow == sameCol) {
            return cells.size() == 1;
        }
        List<Integer> values = (sameRow ? cells.stream().map(Coordinate::col) : cells.stream().map(Coordinate::row))
            .sorted()
            .toList();
        for (int index = 1; index < values.size(); index++) {
            if (values.get(index) != values.get(index - 1) + 1) {
                return false;
            }
        }
        return true;
    }

    private static Map<Integer, Integer> expectedFleetCounts() {
        Map<Integer, Integer> counts = new HashMap<>();
        for (int size : FLEET) {
            counts.merge(size, 1, Integer::sum);
        }
        return counts;
    }

    private static Set<Coordinate> neighborhood(Ship ship) {
        Set<Coordinate> neighbors = new HashSet<>();
        for (Coordinate cell : ship.cells()) {
            for (int row = cell.row() - 1; row <= cell.row() + 1; row++) {
                for (int col = cell.col() - 1; col <= cell.col() + 1; col++) {
                    Coordinate neighbor = new Coordinate(row, col);
                    if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
                        neighbors.add(neighbor);
                    }
                }
            }
        }
        return neighbors;
    }

    private static boolean isInside(Coordinate coordinate) {
        return coordinate.row() >= 0 && coordinate.row() < SIZE
            && coordinate.col() >= 0 && coordinate.col() < SIZE;
    }
}
