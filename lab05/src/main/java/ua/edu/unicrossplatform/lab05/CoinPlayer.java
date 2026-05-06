package ua.edu.unicrossplatform.lab05;

@FunctionalInterface
public interface CoinPlayer {
    int chooseMove(int coinsLeft, int maxTake);
}
