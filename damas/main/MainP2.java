package damas.main;

import damas.gui.JogoDamasController;

import javax.swing.SwingUtilities;

public class MainP2 {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JogoDamasController();
            }
        });
    }
}