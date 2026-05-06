package ua.edu.unicrossplatform.lab03;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class Lab03Application {
    private Lab03Application() {
    }

    public static void main(String[] args) throws IOException {
        PatternLoader loader = new PatternLoader();
        LifeBoard board = args.length > 0
                ? loader.fromFile(Path.of(args[0]))
                : loader.fromStrings(List.of(
                        ".......",
                        "...*...",
                        ".*.*...",
                        "..**...",
                        "......."
                ));

        LifeEngine engine = new LifeEngine();
        LifePrinter printer = new LifePrinter();
        for (int generation = 0; generation <= 5; generation++) {
            System.out.print(printer.render(board, generation));
            if (generation < 5) {
                System.out.println();
            }
            board = engine.nextGeneration(board);
        }
    }
}
