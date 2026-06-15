package damas.modelo;

import damas.excecoes.MovimentoInvalidoException;

/**
 * Representa uma peça Dama no jogo de damas.
 * Herda de `Peca` e implementa `Movimentavel`.
 * Requisito: Generalização e Especialização, Polimorfismo, Interface.
 */
public class Dama extends Peca implements Movimentavel {

    private static final long serialVersionUID = 1L;

    /**
     * Construtor da Dama.
     * @param cor A cor da peça.
     * @param posicao A posição inicial da peça.
     */
    public Dama(Cor cor, Posicao posicao) {
        super(cor, posicao);
    }

    /**
     * Verifica se um movimento é válido para uma peça Dama.
     * Requisito: Polimorfismo (implementação específica de movimentoValido).
     * @param tabuleiro O tabuleiro atual do jogo.
     * @param origem A posição de origem da peça.
     * @param destino A posição de destino desejada.
     * @return true se o movimento for válido, false caso contrário.
     * @throws MovimentoInvalidoException se o movimento for inválido.
     */
    @Override
    public boolean movimentoValido(Tabuleiro tabuleiro, Posicao origem, Posicao destino) throws MovimentoInvalidoException {
        // Uma dama pode se mover qualquer número de casas na diagonal, para frente ou para trás.
        int deltaLinha = destino.getLinha() - origem.getLinha();
        int deltaColuna = destino.getColuna() - origem.getColuna();

        // Movimento deve ser na diagonal (abs(deltaLinha) == abs(deltaColuna))
        if (Math.abs(deltaLinha) != Math.abs(deltaColuna)) {
            throw new MovimentoInvalidoException("Movimento não é diagonal para uma dama.");
        }

        // Verificar se o caminho está livre (sem peças bloqueando)
        int passoLinha = deltaLinha > 0 ? 1 : -1;
        int passoColuna = deltaColuna > 0 ? 1 : -1;

        int linhaAtual = origem.getLinha() + passoLinha;
        int colunaAtual = origem.getColuna() + passoColuna;

        while (linhaAtual != destino.getLinha() && colunaAtual != destino.getColuna()) {
            if (tabuleiro.getPeca(new Posicao(linhaAtual, colunaAtual)) != null) {
                throw new MovimentoInvalidoException("Caminho bloqueado por outra peça.");
            }
            linhaAtual += passoLinha;
            colunaAtual += passoColuna;
        }

        // A casa de destino deve estar vazia
        if (tabuleiro.getPeca(destino) != null) {
            throw new MovimentoInvalidoException("Casa de destino ocupada.");
        }

        return true;
    }

    @Override
    public String toString() {
        return getCor().name().substring(0, 1) + "D"; // Ex: BD para Dama Branca, PD para Dama Preta
    }
}