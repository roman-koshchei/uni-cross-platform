package ua.edu.unicrossplatform.lab07;

import javax.swing.SwingUtilities;

public final class Lab07Main {
    private Lab07Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingGomokuFrame().setVisible(true));
    }
}
