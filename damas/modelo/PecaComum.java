package damas.modelo;

import damas.excecoes.MovimentoInvalidoException;

public class PecaComum extends Peca implements Movimentavel {

    private static final long serialVersionUID = 1L;


    public PecaComum(Cor cor, Posicao posicao) {
        super(cor, posicao);
    }


    @Override
    public boolean movimentoValido(Tabuleiro tabuleiro, Posicao origem, Posicao destino) throws MovimentoInvalidoException {
        int deltaLinha = destino.getLinha() - origem.getLinha();
        int deltaColuna = destino.getColuna() - origem.getColuna();

        if (Math.abs(deltaLinha) != Math.abs(deltaColuna)) {
            throw new MovimentoInvalidoException("Movimento não é diagonal.");
        }

        if (Math.abs(deltaLinha) > 2) {
            throw new MovimentoInvalidoException("Peça comum só pode mover até duas casas (em caso de captura).");
        }

        if (Math.abs(deltaLinha) == 2) {

            if (getCor() == Cor.BRANCAS && deltaLinha > 0) throw new MovimentoInvalidoException("Brancas só capturam para frente.");
            if (getCor() == Cor.PRETOS  && deltaLinha < 0) throw new MovimentoInvalidoException("Pretas só capturam para frente.");

            int linhaMeio = origem.getLinha() + (deltaLinha / 2);
            int colunaMeio = origem.getColuna() + (deltaColuna / 2);
            Peca pecaMeio = tabuleiro.getPeca(new Posicao(linhaMeio, colunaMeio));
            if (pecaMeio == null || pecaMeio.getCor() == this.getCor()) {
                throw new MovimentoInvalidoException("Captura inválida: não há peça adversária para saltar.");
            }
        } else if (Math.abs(deltaLinha) == 1) {

            if (getCor() == Cor.BRANCAS && deltaLinha >= 0) throw new MovimentoInvalidoException("Brancas movem para frente.");
            if (getCor() == Cor.PRETOS  && deltaLinha <= 0) throw new MovimentoInvalidoException("Pretas movem para frente.");
        }

        if (tabuleiro.getPeca(destino) != null) {
            throw new MovimentoInvalidoException("Casa de destino ocupada.");
        }

        return true;
    }

    @Override
    public String toString() {
        return getCor().name().substring(0, 1) + "C";
    }
}
