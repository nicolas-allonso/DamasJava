package damas.modelo;

import damas.excecoes.MovimentoInvalidoException;

public class Dama extends Peca implements Movimentavel {

    private static final long serialVersionUID = 1L;


    public Dama(Cor cor, Posicao posicao) {
        super(cor, posicao);
    }


    @Override
    public boolean movimentoValido(Tabuleiro tabuleiro, Posicao origem, Posicao destino) throws MovimentoInvalidoException {
        int deltaLinha = destino.getLinha() - origem.getLinha();
        int deltaColuna = destino.getColuna() - origem.getColuna();

        if (Math.abs(deltaLinha) != Math.abs(deltaColuna)) throw new MovimentoInvalidoException("Não é diagonal.");

        int passoLinha = deltaLinha > 0 ? 1 : -1;
        int passoColuna = deltaColuna > 0 ? 1 : -1;
        int linhaAtual = origem.getLinha() + passoLinha;
        int colunaAtual = origem.getColuna() + passoColuna;

        int pecasNoCaminho = 0;
        while (linhaAtual != destino.getLinha()) {
            Peca p = tabuleiro.getPeca(new Posicao(linhaAtual, colunaAtual));
            if (p != null) {
                if (p.getCor() == this.getCor()) throw new MovimentoInvalidoException("Bloqueado por peça sua.");
                pecasNoCaminho++;
            }
            linhaAtual += passoLinha;
            colunaAtual += passoColuna;
        }

        if (pecasNoCaminho > 1) throw new MovimentoInvalidoException("Dama só pula uma peça.");
        if (tabuleiro.getPeca(destino) != null) throw new MovimentoInvalidoException("Destino ocupado.");

        return true;
    }

    @Override
    public String toString() {
        return getCor().name().substring(0, 1) + "D";
    }
}