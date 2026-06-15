package damas.modelo;

import java.io.Serializable;

public class Jogador implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private Cor corPecas;


    public Jogador(String nome, Cor corPecas) {
        this.nome = nome;
        this.corPecas = corPecas;
    }


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