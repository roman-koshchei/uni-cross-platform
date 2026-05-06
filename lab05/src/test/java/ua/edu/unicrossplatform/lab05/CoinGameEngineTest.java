package ua.edu.unicrossplatform.lab05;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoinGameEngineTest {
    @Test
    void optimalPlayerLeavesMultipleOfThreeWhenPossible() {
        OptimalCoinPlayer player = new OptimalCoinPlayer();

        assertEquals(1, player.chooseMove(10, 2));
        assertEquals(2, player.chooseMove(8, 2));
        assertEquals(1, player.chooseMove(6, 2));
    }

    @Test
    void optimalPlayerWinsFromWinningPosition() {
        CoinGameEngine engine = new CoinGameEngine(2);

        CoinGameResult result = engine.play(
                10,
                "Комп'ютер",
                new OptimalCoinPlayer(),
                "Людина",
                new ScriptedCoinPlayer(List.of(1, 1, 1, 1))
        );

        assertEquals("Комп'ютер", result.winner());
        assertEquals(List.of(1, 1, 2, 1, 2, 1, 2), result.moves().stream().map(CoinMove::takenCoins).toList());
    }

    @Test
    void rejectsIllegalMove() {
        CoinGameEngine engine = new CoinGameEngine(2);

        assertThrows(IllegalArgumentException.class, () -> engine.play(
                5,
                "Людина",
                (coinsLeft, maxTake) -> 3,
                "Комп'ютер",
                new OptimalCoinPlayer()
        ));
    }
}
