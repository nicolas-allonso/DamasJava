package damas.modelo;

import damas.excecoes.MovimentoInvalidoException;

/**
 * Representa uma peça comum no jogo de damas.
 * Herda de `Peca` e implementa `Movimentavel`.
 * Requisito: Generalização e Especialização, Polimorfismo, Interface.
 */
public class PecaComum extends Peca implements Movimentavel {

    private static final long serialVersionUID = 1L;

    /**
     * Construtor da PecaComum.
     * @param cor A cor da peça.
     * @param posicao A posição inicial da peça.
     */
    public PecaComum(Cor cor, Posicao posicao) {
        super(cor, posicao);
    }

    /**
     * Verifica se um movimento é válido para uma peça comum.
     * Requisito: Polimorfismo (implementação específica de movimentoValido).
     * @param tabuleiro O tabuleiro atual do jogo.
     * @param origem A posição de origem da peça.
     * @param destino A posição de destino desejada.
     * @return true se o movimento for válido, false caso contrário.
     * @throws MovimentoInvalidoException se o movimento for inválido.
     */
    @Override
    public boolean movimentoValido(Tabuleiro tabuleiro, Posicao origem, Posicao destino) throws MovimentoInvalidoException {
        // Uma peça comum só pode se mover uma casa na diagonal para frente.
        // A direção 'frente' depende da cor da peça.
        int deltaLinha = destino.getLinha() - origem.getLinha();
        int deltaColuna = destino.getColuna() - origem.getColuna();

        // Movimento deve ser na diagonal (abs(deltaLinha) == abs(deltaColuna))
        if (Math.abs(deltaLinha) != Math.abs(deltaColuna)) {
            throw new MovimentoInvalidoException("Movimento não é diagonal.");
        }

        // Movimento de apenas uma casa
        if (Math.abs(deltaLinha) != 1) {
            throw new MovimentoInvalidoException("Peça comum só pode mover uma casa.");
        }

        // Peças brancas movem para frente (linha diminui)
        if (getCor() == Cor.BRANCAS && deltaLinha >= 0) {
            throw new MovimentoInvalidoException("Peças brancas só podem mover para frente.");
        }
        // Peças pretas movem para frente (linha aumenta)
        if (getCor() == Cor.PRETOS && deltaLinha <= 0) {
            throw new MovimentoInvalidoException("Peças pretas só podem mover para frente.");
        }

        // A casa de destino deve estar vazia
        if (tabuleiro.getPeca(destino) != null) {
            throw new MovimentoInvalidoException("Casa de destino ocupada.");
        }

        // Lógica para captura (ainda não implementada completamente aqui, será no Tabuleiro/Modelo)
        // Por enquanto, apenas movimentos simples de uma casa.

        return true;
    }

    @Override
    public String toString() {
        return getCor().name().substring(0, 1) + "C"; // Ex: BC para Peça Comum Branca, PC para Peça Comum Preta
    }
}