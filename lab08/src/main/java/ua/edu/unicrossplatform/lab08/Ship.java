package ua.edu.unicrossplatform.lab08;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Ship {
    private final List<Coordinate> cells;
    private final Set<Coordinate> hits = new HashSet<>();

    public Ship(List<Coordinate> cells) {
        this.cells = List.copyOf(cells);
    }

    public List<Coordinate> cells() {
        return cells;
    }

    public int size() {
        return cells.size();
    }

    public boolean occupies(Coordinate coordinate) {
        return cells.contains(coordinate);
    }

    public void hit(Coordinate coordinate) {
        if (occupies(coordinate)) {
            hits.add(coordinate);
        }
    }

    public boolean isSunk() {
        return hits.size() == cells.size();
    }
}
