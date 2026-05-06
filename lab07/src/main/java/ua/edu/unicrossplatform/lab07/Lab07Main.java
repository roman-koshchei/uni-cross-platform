package ua.edu.unicrossplatform.lab07;

public final class Lab07Main {
    private Lab07Main() {
    }

    public static void main(String[] args) {
        GomokuGame game = shouldRunInteractive(args)
                ? new GomokuGame(new ConsoleHumanStrategy(), new ComputerStrategy())
                : new GomokuGame();
        GomokuGame.GameOutcome outcome = game.play();
        System.out.println("Лабораторна 7. П'ять в ряд");
        System.out.println("Поле: " + Board.SIZE + "x" + Board.SIZE);
        for (String line : outcome.log()) {
            System.out.println(line);
        }
        System.out.println();
        System.out.print(outcome.board().render());
        if (outcome.winner() == Cell.EMPTY) {
            System.out.println("Результат: нічия");
        } else {
            System.out.println("Переможець: " + outcome.winner());
        }
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
