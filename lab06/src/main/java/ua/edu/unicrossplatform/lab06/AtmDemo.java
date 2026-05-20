package ua.edu.unicrossplatform.lab06;

import javax.swing.SwingUtilities;

public final class AtmDemo {
    private AtmDemo() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingAtmFrame().setVisible(true));
    }
}
