package damas.modelo;

import damas.excecoes.MovimentoInvalidoException;

/**
 * Interface que define o comportamento de objetos que podem ser movimentados no tabuleiro.
 * Requisito: Interface.
 */
public interface Movimentavel {
    /**
     * Verifica se um movimento é válido para a peça.
     * @throws MovimentoInvalidoException Requisito: Métodos com Throws.
     */
    boolean movimentoValido(Tabuleiro tabuleiro, Posicao origem, Posicao destino) throws MovimentoInvalidoException;
}