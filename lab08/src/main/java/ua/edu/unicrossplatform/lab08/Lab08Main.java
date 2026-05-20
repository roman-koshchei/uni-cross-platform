package ua.edu.unicrossplatform.lab08;

import javax.swing.SwingUtilities;

public final class Lab08Main {
    private Lab08Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingNavalBattleFrame().setVisible(true));
    }
}
