package ua.edu.unicrossplatform.lab04;

public record GameStatistics(int playerWins, int computerWins, int draws) {
    public int rounds() {
        return playerWins + computerWins + draws;
    }
}
