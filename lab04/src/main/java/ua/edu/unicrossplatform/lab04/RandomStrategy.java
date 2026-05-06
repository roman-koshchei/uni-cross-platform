package ua.edu.unicrossplatform.lab04;

import java.util.List;
import java.util.Random;

public final class RandomStrategy implements GameStrategy {
    private final Random random;

    public RandomStrategy(Random random) {
        this.random = random;
    }

    @Override
    public String name() {
        return "Випадкова";
    }

    @Override
    public Move chooseMove(List<Round> sessionHistory) {
        Move[] moves = Move.values();
        return moves[random.nextInt(moves.length)];
    }
}
