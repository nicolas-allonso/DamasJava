package damas.modelo;

import damas.excecoes.MovimentoInvalidoException;
import damas.excecoes.PecaInexistenteException;
import java.io.Serializable;

/**
 * Classe que representa o tabuleiro do jogo de damas.
 * Requisito: Encapsulamento (atributos privados com getters/setters).
 */
public class Tabuleiro implements Serializable {
    private static final long serialVersionUID = 1L;
    private Peca[][] pecas;
    private static final int TAMANHO = 8;

    /**
     * Construtor do Tabuleiro. Inicializa o tabuleiro com as peças nas posições iniciais.
     */
    public Tabuleiro() {
        this.pecas = new Peca[TAMANHO][TAMANHO];
        inicializarTabuleiro();
    }

    /**
     * Inicializa o tabuleiro com as peças nas posições padrão.
     */
    private void inicializarTabuleiro() {
        // Peças Pretas
        for (int i = 0; i < 3; i++) {
            for (int j = (i % 2 == 0) ? 1 : 0; j < TAMANHO; j += 2) {
                pecas[i][j] = new PecaComum(Cor.PRETOS, new Posicao(i, j));
            }
        }

        // Peças Brancas
        for (int i = TAMANHO - 3; i < TAMANHO; i++) {
            for (int j = (i % 2 == 0) ? 1 : 0; j < TAMANHO; j += 2) {
                pecas[i][j] = new PecaComum(Cor.BRANCAS, new Posicao(i, j));
            }
        }
    }

    /**
     * Retorna a peça em uma determinada posição.
     * @param pos A posição a ser verificada.
     * @return A peça na posição, ou null se não houver peça.
     */
    public Peca getPeca(Posicao pos) {
        if (!isPosicaoValida(pos)) {
            return null; // Ou lançar uma exceção, dependendo da regra de negócio
        }
        return pecas[pos.getLinha()][pos.getColuna()];
    }

    /**
     * Define uma peça em uma determinada posição.
     * @param pos A posição onde a peça será colocada.
     * @param peca A peça a ser colocada.
     */
    public void setPeca(Posicao pos, Peca peca) {
        if (!isPosicaoValida(pos)) {
            // Lançar exceção ou tratar erro
            return;
        }
        pecas[pos.getLinha()][pos.getColuna()] = peca;
        if (peca != null) {
            peca.setPosicao(pos);
        }
    }

    /**
     * Move uma peça de uma posição de origem para uma de destino.
     * Requisito: Polimorfismo (chama movimentoValido da Peca).
     * @param origem A posição de origem da peça.
     * @param destino A posição de destino da peça.
     * @throws MovimentoInvalidoException se o movimento for inválido.
     * @throws PecaInexistenteException se não houver peça na origem.
     */
    public void moverPeca(Posicao origem, Posicao destino) throws MovimentoInvalidoException, PecaInexistenteException {
        if (!isPosicaoValida(origem) || !isPosicaoValida(destino)) {
            throw new MovimentoInvalidoException("Posição de origem ou destino inválida.");
        }

        Peca peca = getPeca(origem);
        if (peca == null) {
            throw new PecaInexistenteException("Não há peça na posição de origem.");
        }

        // Valida o movimento usando o polimorfismo
        if (peca.movimentoValido(this, origem, destino)) {
            // Lógica de captura (simplificada por enquanto)
            int deltaLinha = destino.getLinha() - origem.getLinha();
            int deltaColuna = destino.getColuna() - origem.getColuna();

            if (Math.abs(deltaLinha) == 2 && Math.abs(deltaColuna) == 2) { // Possível captura
                int linhaMeio = origem.getLinha() + (deltaLinha / 2);
                int colunaMeio = origem.getColuna() + (deltaColuna / 2);
                Posicao posPecaCapturada = new Posicao(linhaMeio, colunaMeio);
                Peca pecaCapturada = getPeca(posPecaCapturada);

                if (pecaCapturada != null && pecaCapturada.getCor() != peca.getCor()) {
                    setPeca(posPecaCapturada, null); // Remove a peça capturada
                } else {
                    throw new MovimentoInvalidoException("Não é possível capturar peça ali ou peça da mesma cor.");
                }
            }

            setPeca(destino, peca); // Move a peça para o destino
            setPeca(origem, null);  // Limpa a posição de origem

            // Lógica de promoção para Dama
            if (peca instanceof PecaComum) {
                if ((peca.getCor() == Cor.BRANCAS && destino.getLinha() == 0) ||
                    (peca.getCor() == Cor.PRETOS && destino.getLinha() == TAMANHO - 1)) {
                    setPeca(destino, new Dama(peca.getCor(), destino));
                }
            }
        }
    }

    /**
     * Verifica se uma posição está dentro dos limites do tabuleiro.
     * @param pos A posição a ser verificada.
     * @return true se a posição for válida, false caso contrário.
     */
    public boolean isPosicaoValida(Posicao pos) {
        return pos.getLinha() >= 0 && pos.getLinha() < TAMANHO &&
               pos.getColuna() >= 0 && pos.getColuna() < TAMANHO;
    }

    /**
     * Imprime o tabuleiro no console para depuração.
     */
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