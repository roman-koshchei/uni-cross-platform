package ua.edu.unicrossplatform.lab05;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CoinGameEngine {
    private final int maxTake;

    public CoinGameEngine(int maxTake) {
        if (maxTake < 1) {
            throw new IllegalArgumentException("maxTake must be positive");
        }
        this.maxTake = maxTake;
    }

    public int maxTake() {
        return maxTake;
    }

    public CoinGameResult play(int startingCoins, String firstPlayerName, CoinPlayer firstPlayer,
                               String secondPlayerName, CoinPlayer secondPlayer) {
        if (startingCoins < 1) {
            throw new IllegalArgumentException("startingCoins must be positive");
        }
        Objects.requireNonNull(firstPlayerName, "firstPlayerName");
        Objects.requireNonNull(secondPlayerName, "secondPlayerName");
        Objects.requireNonNull(firstPlayer, "firstPlayer");
        Objects.requireNonNull(secondPlayer, "secondPlayer");

        int coinsLeft = startingCoins;
        List<CoinMove> moves = new ArrayList<>();
        String currentName = firstPlayerName;
        CoinPlayer currentPlayer = firstPlayer;
        String otherName = secondPlayerName;
        CoinPlayer otherPlayer = secondPlayer;

        while (coinsLeft > 0) {
            int move = currentPlayer.chooseMove(coinsLeft, maxTake);
            validateMove(move, coinsLeft);
            coinsLeft -= move;
            moves.add(new CoinMove(currentName, move, coinsLeft));

            if (coinsLeft == 0) {
                return new CoinGameResult(startingCoins, firstPlayerName, currentName, List.copyOf(moves));
            }

            String nextName = currentName;
            currentName = otherName;
            otherName = nextName;

            CoinPlayer nextPlayer = currentPlayer;
            currentPlayer = otherPlayer;
            otherPlayer = nextPlayer;
        }

        throw new IllegalStateException("Game ended unexpectedly");
    }

    private void validateMove(int move, int coinsLeft) {
        if (move < 1 || move > Math.min(maxTake, coinsLeft)) {
            throw new IllegalArgumentException("Invalid move: " + move);
        }
    }
}
