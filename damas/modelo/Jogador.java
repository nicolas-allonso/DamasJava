package damas.modelo;

import java.io.Serializable;

/**
 * Representa um jogador no jogo de damas.
 * Requisito: Encapsulamento (atributos privados com getters).
 */
public class Jogador implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private Cor corPecas;

    /**
     * Construtor da classe Jogador.
     * @param nome O nome do jogador.
     * @param corPecas A cor das peças que o jogador controla.
     */
    public Jogador(String nome, Cor corPecas) {
        this.nome = nome;
        this.corPecas = corPecas;
    }

    /**
     * Retorna o nome do jogador.
     * @return O nome do jogador.
     */
    public String getNome() {
        return nome;
    }
    public Cor getCorPecas() {
        return corPecas;
    }

    @Override
    public String toString() {
        return nome + " (" + corPecas + ")";
    }
}