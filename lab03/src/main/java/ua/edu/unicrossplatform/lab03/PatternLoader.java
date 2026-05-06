package ua.edu.unicrossplatform.lab03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class PatternLoader {
    public LifeBoard fromStrings(List<String> lines) {
        if (lines.isEmpty()) {
            return new LifeBoard(new boolean[0][0]);
        }

        int columns = lines.getFirst().length();
        boolean[][] cells = new boolean[lines.size()][columns];
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            if (line.length() != columns) {
                throw new IllegalArgumentException("All pattern rows must have equal length");
            }

            for (int col = 0; col < columns; col++) {
                cells[row][col] = isAliveMarker(line.charAt(col));
            }
        }
        return new LifeBoard(cells);
    }

    public LifeBoard fromFile(Path path) throws IOException {
        return fromStrings(Files.readAllLines(path));
    }

    private boolean isAliveMarker(char symbol) {
        return switch (symbol) {
            case '*', '#', 'O', 'X', '1' -> true;
            default -> false;
        };
    }
}
