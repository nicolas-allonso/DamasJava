package damas.modelo;

import damas.excecoes.MovimentoInvalidoException;
import damas.excecoes.PecaInexistenteException;
import java.io.Serializable;

public class Tabuleiro implements Serializable {
    private static final long serialVersionUID = 1L;
    private Peca[][] pecas;
    private static final int TAMANHO = 8;


    public Tabuleiro() {
        this.pecas = new Peca[TAMANHO][TAMANHO];
        inicializarTabuleiro();
    }


    private void inicializarTabuleiro() {
        for (int i = 0; i < 3; i++) {
            for (int j = (i % 2 == 0) ? 1 : 0; j < TAMANHO; j += 2) {
                pecas[i][j] = new PecaComum(Cor.PRETOS, new Posicao(i, j));
            }
        }
        for (int i = TAMANHO - 3; i < TAMANHO; i++) {
            for (int j = (i % 2 == 0) ? 1 : 0; j < TAMANHO; j += 2) {
                pecas[i][j] = new PecaComum(Cor.BRANCAS, new Posicao(i, j));
            }
        }
    }


    public Peca getPeca(Posicao pos) {
        if (!isPosicaoValida(pos)) return null;
        return pecas[pos.getLinha()][pos.getColuna()];
    }


    public void setPeca(Posicao pos, Peca peca) {
        if (!isPosicaoValida(pos)) return;
        pecas[pos.getLinha()][pos.getColuna()] = peca;
        if (peca != null) peca.setPosicao(pos);
    }


    public void moverPeca(Posicao origem, Posicao destino) throws MovimentoInvalidoException, PecaInexistenteException {
        Peca peca = getPeca(origem);
        if (peca == null) throw new PecaInexistenteException("Origem vazia.");




        Movimentavel movimentavel = (Movimentavel) peca;
        if (movimentavel.movimentoValido(this, origem, destino)) {
            int deltaLinha = destino.getLinha() - origem.getLinha();
            int passoLinha = deltaLinha > 0 ? 1 : -1;
            int passoColuna = (destino.getColuna() - origem.getColuna()) > 0 ? 1 : -1;

            int l = origem.getLinha() + passoLinha;
            int c = origem.getColuna() + passoColuna;


            while (l != destino.getLinha()) {
                setPeca(new Posicao(l, c), null);
                l += passoLinha;
                c += passoColuna;
            }

            setPeca(destino, peca);
            setPeca(origem, null);


            if (peca instanceof PecaComum) {
                if ((peca.getCor() == Cor.BRANCAS && destino.getLinha() == 0) ||
                    (peca.getCor() == Cor.PRETOS  && destino.getLinha() == TAMANHO - 1)) {
                    setPeca(destino, new Dama(peca.getCor(), destino));
                }
            }
        }
    }


    public boolean temCapturaDisponivel(Posicao origem) {
        Peca peca = getPeca(origem);
        if (peca == null) return false;

        int[] deltas = {-2, 2};
        for (int dl : deltas) {
            for (int dc : deltas) {
                Posicao destino = new Posicao(origem.getLinha() + dl, origem.getColuna() + dc);
                if (!isPosicaoValida(destino)) continue;
                try {
                    Movimentavel mov = (Movimentavel) peca;
                    mov.movimentoValido(this, origem, destino);
                    return true;
                } catch (MovimentoInvalidoException e) {

                }
            }
        }
        return false;
    }


    public boolean temMovimentoDisponivel(Cor cor) {
        int[] d1 = {-1, 1, -2, 2};
        int[] d2 = {-1, 1, -2, 2};

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                Peca peca = getPeca(new Posicao(i, j));
                if (peca == null || peca.getCor() != cor) continue;

                Posicao origem = new Posicao(i, j);
                for (int dl : d1) {
                    for (int dc : d2) {

                        if (Math.abs(dl) != Math.abs(dc)) continue;
                        Posicao destino = new Posicao(i + dl, j + dc);
                        if (!isPosicaoValida(destino)) continue;
                        try {
                            Movimentavel mov = (Movimentavel) peca;
                            mov.movimentoValido(this, origem, destino);
                            return true;
                        } catch (MovimentoInvalidoException e) {

                        }
                    }
                }
            }
        }
        return false;
    }


    public boolean isPosicaoValida(Posicao pos) {
        return pos.getLinha() >= 0 && pos.getLinha() < TAMANHO &&
               pos.getColuna() >= 0 && pos.getColuna() < TAMANHO;
    }


    public void imprimirTabuleiro() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                Peca p = getPeca(new Posicao(i, j));
                System.out.print((p == null ? "--" : p.toString()) + " ");
            }
            System.out.println();
        }
    }
}
