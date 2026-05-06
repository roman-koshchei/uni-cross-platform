package ua.edu.unicrossplatform.lab04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MoveTest {
    @Test
    void shouldApplyWellScissorsPaperRules() {
        assertEquals(Outcome.PLAYER_WIN, Move.WELL.playAgainst(Move.SCISSORS));
        assertEquals(Outcome.PLAYER_WIN, Move.SCISSORS.playAgainst(Move.PAPER));
        assertEquals(Outcome.PLAYER_WIN, Move.PAPER.playAgainst(Move.WELL));
        assertEquals(Outcome.COMPUTER_WIN, Move.WELL.playAgainst(Move.PAPER));
        assertEquals(Outcome.DRAW, Move.PAPER.playAgainst(Move.PAPER));
    }
}
