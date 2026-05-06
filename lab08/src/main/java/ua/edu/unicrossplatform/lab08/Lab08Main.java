package ua.edu.unicrossplatform.lab08;

import java.util.Random;
import java.util.Scanner;

public final class Lab08Main {
    private Lab08Main() {
    }

    public static void main(String[] args) {
        NavalBattleGame game = new NavalBattleGame(shouldRunInteractive(args) ? new Random() : new Random(20260404L));
        NavalBattleGame.GameReport report = shouldRunInteractive(args)
                ? game.playInteractive(new Scanner(System.in))
                : game.play();
        System.out.println("Лабораторна 8. Морський бій 10x10");
        System.out.println("Коректність флоту A: " + report.firstFleetValid());
        System.out.println("Коректність флоту B: " + report.secondFleetValid());
        for (String line : report.log()) {
            System.out.println(line);
        }
        System.out.println("Переможець: " + report.winner());
    }

    private static boolean shouldRunInteractive(String[] args) {
        for (String arg : args) {
            if ("--interactive".equalsIgnoreCase(arg)) {
                return true;
            }
            if ("--demo".equalsIgnoreCase(arg)) {
                return false;
            }
        }
        return System.console() != null;
    }
}
