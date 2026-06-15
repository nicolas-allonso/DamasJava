package damas.modelo;

import damas.excecoes.MovimentoInvalidoException;
import java.io.Serializable;

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


    public abstract boolean movimentoValido(Tabuleiro tabuleiro, Posicao origem, Posicao destino) throws MovimentoInvalidoException;

    @Override
    public String toString() {
        return cor.name().substring(0, 1) + "P";
    }
}