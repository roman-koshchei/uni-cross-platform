package ua.edu.unicrossplatform.lab03;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class LifeEngineTest {
    private final PatternLoader loader = new PatternLoader();
    private final LifeEngine engine = new LifeEngine();

    @Test
    void shouldEvolveBlinkerWithoutWrapping() {
        LifeBoard initial = loader.fromStrings(List.of(
                ".....",
                "..*..",
                "..*..",
                "..*..",
                "....."
        ));

        LifeBoard next = engine.nextGeneration(initial);

        assertArrayEquals(new boolean[][] {
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, true, true, true, false},
                {false, false, false, false, false},
                {false, false, false, false, false}
        }, next.toArray());
    }

    @Test
    void shouldNotTreatOppositeEdgesAsNeighbors() {
        LifeBoard initial = loader.fromStrings(List.of(
                "*..",
                "...",
                "..*"
        ));

        LifeBoard next = engine.nextGeneration(initial);

        assertArrayEquals(new boolean[][] {
                {false, false, false},
                {false, false, false},
                {false, false, false}
        }, next.toArray());
    }
}
