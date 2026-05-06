package ua.edu.unicrossplatform.lab05;

import java.util.Scanner;

public final class ConsoleCoinPlayer implements CoinPlayer {
    private final Scanner scanner;

    public ConsoleCoinPlayer(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public int chooseMove(int coinsLeft, int maxTake) {
        int allowedMax = Math.min(maxTake, coinsLeft);
        while (true) {
            System.out.print("Залишилось монет: " + coinsLeft + ". Скільки взяти (1-" + allowedMax + "): ");
            String input = scanner.nextLine().trim();
            try {
                int move = Integer.parseInt(input);
                if (move >= 1 && move <= allowedMax) {
                    return move;
                }
            } catch (NumberFormatException ignored) {
                // retry below
            }
            System.out.println("Некоректний хід. Можна взяти від 1 до " + allowedMax + " монет.");
        }
    }
}
