package ua.edu.unicrossplatform.lab03;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PatternLoaderTest {
    private final PatternLoader loader = new PatternLoader();

    @Test
    void shouldLoadPatternFromStrings() {
        LifeBoard board = loader.fromStrings(List.of(
                ".*.",
                "#..",
                "..O"
        ));

        assertArrayEquals(new boolean[][] {
                {false, true, false},
                {true, false, false},
                {false, false, true}
        }, board.toArray());
    }

    @Test
    void shouldLoadPatternFromFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("pattern.txt");
        Files.writeString(file, "..X" + System.lineSeparator() + "1.." + System.lineSeparator() + "...");

        LifeBoard board = loader.fromFile(file);

        assertArrayEquals(new boolean[][] {
                {false, false, true},
                {true, false, false},
                {false, false, false}
        }, board.toArray());
    }
}
