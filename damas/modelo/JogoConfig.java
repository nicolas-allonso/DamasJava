package damas.modelo;

import java.io.Serializable;

public class JogoConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private Jogador jogador1;
    private Jogador jogador2;


    public JogoConfig(Jogador jogador1, Jogador jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
    }


    public Jogador getJogador1() {
        return jogador1;
    }


    public Jogador getJogador2() {
        return jogador2;
    }
}