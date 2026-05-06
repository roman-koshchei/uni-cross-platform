package ua.edu.unicrossplatform.lab08;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public final class ComputerShooter {
    private final Random random;
    private final List<Coordinate> searchPool = new ArrayList<>();
    private final Queue<Coordinate> targetQueue = new ArrayDeque<>();
    private final Set<Coordinate> usedShots = new HashSet<>();

    public ComputerShooter(Random random) {
        this.random = random;
        for (int row = 0; row < Battlefield.SIZE; row++) {
            for (int col = 0; col < Battlefield.SIZE; col++) {
                searchPool.add(new Coordinate(row, col));
            }
        }
    }

    public Coordinate nextShot() {
        while (!targetQueue.isEmpty()) {
            Coordinate candidate = targetQueue.remove();
            if (usedShots.add(candidate)) {
                return candidate;
            }
        }
        while (true) {
            Coordinate candidate = searchPool.remove(random.nextInt(searchPool.size()));
            if (usedShots.add(candidate)) {
                return candidate;
            }
        }
    }

    public void acceptResult(Coordinate shot, ShotResult result) {
        if (result == ShotResult.SUNK) {
            targetQueue.clear();
            return;
        }
        if (result != ShotResult.HIT) {
            return;
        }
        for (Coordinate neighbor : neighbors(shot)) {
            if (!usedShots.contains(neighbor) && !targetQueue.contains(neighbor)) {
                targetQueue.add(neighbor);
            }
        }
    }

    private List<Coordinate> neighbors(Coordinate coordinate) {
        List<Coordinate> neighbors = new ArrayList<>();
        addIfInside(neighbors, coordinate.row() - 1, coordinate.col());
        addIfInside(neighbors, coordinate.row() + 1, coordinate.col());
        addIfInside(neighbors, coordinate.row(), coordinate.col() - 1);
        addIfInside(neighbors, coordinate.row(), coordinate.col() + 1);
        return neighbors;
    }

    private static void addIfInside(List<Coordinate> neighbors, int row, int col) {
        if (row >= 0 && row < Battlefield.SIZE && col >= 0 && col < Battlefield.SIZE) {
            neighbors.add(new Coordinate(row, col));
        }
    }
}
