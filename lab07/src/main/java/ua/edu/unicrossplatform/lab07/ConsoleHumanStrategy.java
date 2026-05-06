package ua.edu.unicrossplatform.lab07;

import java.util.Scanner;

public final class ConsoleHumanStrategy implements PlayerStrategy {
    private final Scanner scanner;

    public ConsoleHumanStrategy() {
        this(new Scanner(System.in));
    }

    ConsoleHumanStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Move chooseMove(Board board) {
        System.out.print(board.render());
        while (true) {
            System.out.print("Ваш хід, введіть рядок і стовпець через пробіл: ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");
            if (parts.length == 2) {
                try {
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);
                    if (board.isInside(row, col) && board.isEmpty(row, col)) {
                        return new Move(row, col);
                    }
                } catch (NumberFormatException ignored) {
                    // retry below
                }
            }
            System.out.println("Некоректний хід. Вкажіть порожню клітинку в межах поля 10x10.");
        }
    }
}
