package damas.modelo;

import damas.excecoes.MovimentoInvalidoException;

public interface Movimentavel {

    boolean movimentoValido(Tabuleiro tabuleiro, Posicao origem, Posicao destino) throws MovimentoInvalidoException;
}