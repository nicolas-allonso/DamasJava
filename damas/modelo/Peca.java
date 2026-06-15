package damas.modelo;

import damas.excecoes.MovimentoInvalidoException;
import java.io.Serializable;

/**
 * Classe abstrata que representa uma peça genérica no jogo de damas.
 * Demonstra Generalização e Especialização, e Classe Abstrata.
 * Requisito: Encapsulamento (atributos privados com getters/setters).
 */
public abstract class Peca implements Serializable {
    private static final long serialVersionUID = 1L;
    private Cor cor;
    private Posicao posicao;

    public Peca(Cor cor, Posicao posicao) {
        this.cor = cor;
        this.posicao = posicao;
    }

    public Cor getCor() { return cor; }
    public Posicao getPosicao() { return posicao; }
    public void setPosicao(Posicao posicao) { this.posicao = posicao; }

    /**
     * Método abstrato para verificar se um movimento é válido.
     * Requisito: Classe Abstrata (método abstrato).
     * @throws MovimentoInvalidoException Requisito: Métodos com Throws.
     */
    public abstract boolean movimentoValido(Tabuleiro tabuleiro, Posicao origem, Posicao destino) throws MovimentoInvalidoException;

    @Override
    public String toString() {
        return cor.name().substring(0, 1) + "P";
    }
}