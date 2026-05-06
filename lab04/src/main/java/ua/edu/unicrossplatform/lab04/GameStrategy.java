package ua.edu.unicrossplatform.lab04;

import java.util.List;

public interface GameStrategy {
    String name();

    Move chooseMove(List<Round> sessionHistory);
}
