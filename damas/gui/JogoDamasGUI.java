package damas.gui;

import damas.modelo.Cor;
import damas.modelo.Peca;
import damas.modelo.Posicao;
import damas.modelo.Tabuleiro;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class JogoDamasGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel tabuleiroPanel;
    private JLabel statusLabel;
    private JogoDamasController controller;

    private Posicao selectedPosicao = null;
    private boolean jogoEncerrado = false;


    public JogoDamasGUI(JogoDamasController controller) {
        this.controller = controller;
        setTitle("Jogo de Damas");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        tabuleiroPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTabuleiro(g);
            }
        };
        tabuleiroPanel.setLayout(new GridLayout(8, 8));
        tabuleiroPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jogoEncerrado) return;
                int col = e.getX() / (tabuleiroPanel.getWidth() / 8);
                int row = e.getY() / (tabuleiroPanel.getHeight() / 8);
                controller.lidarComClique(row, col);
            }
        });
        add(tabuleiroPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Bem-vindo ao Jogo de Damas!");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }


    private void drawTabuleiro(Graphics g) {
        Tabuleiro tabuleiro = controller.getModelo().getTabuleiro();
        int cellSize = tabuleiroPanel.getWidth() / 8;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                if ((row + col) % 2 == 0) {
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);


                Peca peca = tabuleiro.getPeca(new Posicao(row, col));
                if (peca != null) {
                    if (peca.getCor() == Cor.BRANCAS) {
                        g.setColor(Color.WHITE);
                    } else {
                        g.setColor(Color.BLACK);
                    }
                    g.fillOval(col * cellSize + 5, row * cellSize + 5, cellSize - 10, cellSize - 10);

                    if (peca instanceof damas.modelo.Dama) {
                        g.setColor(Color.RED);
                        g.drawOval(col * cellSize + 10, row * cellSize + 10, cellSize - 20, cellSize - 20);
                    }
                }


                if (selectedPosicao != null && selectedPosicao.getLinha() == row && selectedPosicao.getColuna() == col) {
                    g.setColor(Color.BLUE);
                    g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
                }
            }
        }
    }


    public void atualizarTabuleiro(Tabuleiro tabuleiro) {
        tabuleiroPanel.repaint();
    }


    public void exibirMensagem(String mensagem) {
        statusLabel.setText(mensagem);
    }


    public void encerrarJogo(String mensagem) {
        jogoEncerrado = true;
        setSelectedPosicao(null);
        statusLabel.setText(mensagem);
        JOptionPane.showMessageDialog(this, mensagem, "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
    }


    public void setSelectedPosicao(Posicao pos) {
        this.selectedPosicao = pos;
        tabuleiroPanel.repaint();
    }
}