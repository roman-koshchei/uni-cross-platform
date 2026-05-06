package ua.edu.unicrossplatform.lab04;

import java.util.List;

public final class SessionAdaptiveStrategy implements GameStrategy {
    private final GameStrategy fallback;

    public SessionAdaptiveStrategy(GameStrategy fallback) {
        this.fallback = fallback;
    }

    @Override
    public String name() {
        return "Адаптивна до сеансу";
    }

    @Override
    public Move chooseMove(List<Round> sessionHistory) {
        Move predicted = StrategySupport.mostFrequentPlayerMove(sessionHistory);
        return predicted == null ? fallback.chooseMove(sessionHistory) : predicted.counterMove();
    }
}
