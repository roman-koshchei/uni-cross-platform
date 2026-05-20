package ua.edu.unicrossplatform.lab04;

import java.nio.file.Path;
import javax.swing.SwingUtilities;

public final class Lab04Application {
    private Lab04Application() {
    }

    public static void main(String[] args) {
        Path historyFile = Path.of(System.getProperty("user.dir"), "lab04-history.csv");
        SwingUtilities.invokeLater(() -> new SwingGameFrame(historyFile).setVisible(true));
    }
}
