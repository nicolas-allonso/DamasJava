package damas.gui;

import damas.modelo.Cor;
import damas.modelo.Peca;
import damas.modelo.Posicao;
import damas.modelo.Tabuleiro;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * Classe que representa a interface gráfica do jogo de damas.
 * Requisito: Interface Swing.
 */
public class JogoDamasGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel tabuleiroPanel;
    private JLabel statusLabel;
    private JogoDamasController controller;

    private Posicao selectedPosicao = null;

    /**
     * Construtor da interface gráfica do jogo.
     * @param controller O controlador do jogo.
     */
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

    /**
     * Desenha o tabuleiro e as peças.
     * @param g O contexto gráfico.
     */
    private void drawTabuleiro(Graphics g) {
        Tabuleiro tabuleiro = controller.getModelo().getTabuleiro();
        int cellSize = tabuleiroPanel.getWidth() / 8;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // Desenha as casas do tabuleiro
                if ((row + col) % 2 == 0) {
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);

                // Desenha as peças
                Peca peca = tabuleiro.getPeca(new Posicao(row, col));
                if (peca != null) {
                    if (peca.getCor() == Cor.BRANCAS) {
                        g.setColor(Color.WHITE);
                    } else {
                        g.setColor(Color.BLACK);
                    }
                    g.fillOval(col * cellSize + 5, row * cellSize + 5, cellSize - 10, cellSize - 10);

                    if (peca instanceof damas.modelo.Dama) {
                        g.setColor(Color.RED); // Coroa para a Dama
                        g.drawOval(col * cellSize + 10, row * cellSize + 10, cellSize - 20, cellSize - 20);
                    }
                }

                // Destaca a peça selecionada
                if (selectedPosicao != null && selectedPosicao.getLinha() == row && selectedPosicao.getColuna() == col) {
                    g.setColor(Color.BLUE);
                    g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    /**
     * Atualiza a exibição do tabuleiro.
     * @param tabuleiro O tabuleiro a ser exibido.
     */
    public void atualizarTabuleiro(Tabuleiro tabuleiro) {
        tabuleiroPanel.repaint();
    }

    /**
     * Exibe uma mensagem na barra de status.
     * @param mensagem A mensagem a ser exibida.
     */
    public void exibirMensagem(String mensagem) {
        statusLabel.setText(mensagem);
    }

    /**
     * Define a posição da peça selecionada para destaque visual.
     * @param pos A posição da peça selecionada.
     */
    public void setSelectedPosicao(Posicao pos) {
        this.selectedPosicao = pos;
        tabuleiroPanel.repaint();
    }
}