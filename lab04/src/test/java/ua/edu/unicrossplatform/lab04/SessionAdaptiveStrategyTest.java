package ua.edu.unicrossplatform.lab04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SessionAdaptiveStrategyTest {
    @Test
    void shouldUseOnlySessionHistory() {
        SessionAdaptiveStrategy strategy = new SessionAdaptiveStrategy(new RandomStrategy(new Random(0)));
        List<Round> history = List.of(
                new Round(Move.WELL, Move.PAPER, Outcome.COMPUTER_WIN),
                new Round(Move.WELL, Move.PAPER, Outcome.COMPUTER_WIN),
                new Round(Move.PAPER, Move.SCISSORS, Outcome.COMPUTER_WIN)
        );

        assertEquals(Move.PAPER, strategy.chooseMove(history));
    }
}
