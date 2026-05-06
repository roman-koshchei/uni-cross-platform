package ua.edu.unicrossplatform.lab05;

import java.util.List;

public record CoinGameResult(
        int startingCoins,
        String firstPlayer,
        String winner,
        List<CoinMove> moves
) {
}
