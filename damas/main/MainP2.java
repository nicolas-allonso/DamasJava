package damas.main;

import damas.gui.JogoDamasController;

import javax.swing.SwingUtilities;

/**
 * Programa Principal (P2) do Jogo de Damas.
 * Responsável por iniciar a interface gráfica e o fluxo do jogo.
 * Requisito: P2 – Programa de Jogo.
 */
public class MainP2 {

    public static void main(String[] args) {
        // Garante que a interface gráfica seja criada e atualizada na thread de despacho de eventos do Swing.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JogoDamasController();
            }
        });
    }
}