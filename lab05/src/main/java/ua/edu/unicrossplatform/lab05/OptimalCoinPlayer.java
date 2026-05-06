package ua.edu.unicrossplatform.lab05;

public final class OptimalCoinPlayer implements CoinPlayer {
    @Override
    public int chooseMove(int coinsLeft, int maxTake) {
        int target = maxTake + 1;
        int remainder = coinsLeft % target;
        if (remainder == 0) {
            return 1;
        }
        return Math.min(remainder, Math.min(maxTake, coinsLeft));
    }
}
