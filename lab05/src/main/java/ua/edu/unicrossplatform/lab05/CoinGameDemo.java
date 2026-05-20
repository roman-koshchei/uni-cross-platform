package ua.edu.unicrossplatform.lab05;

import javax.swing.SwingUtilities;

public final class CoinGameDemo {
    private CoinGameDemo() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingCoinGameFrame().setVisible(true));
    }
}
