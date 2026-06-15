package damas.modelo;

import damas.excecoes.MovimentoInvalidoException;
import damas.excecoes.PecaInexistenteException;
import damas.persistencia.PersistenciaDados;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa o modelo do jogo de damas, contendo a lógica de negócio.
 * Gerencia o tabuleiro, jogadores, turnos e logs.
 */
public class JogoDamasModelo {
    private Tabuleiro tabuleiro;
    private Jogador jogadorAtual;
    private Jogador jogador1;
    private Jogador jogador2;
    private List<String> logPartida;
    private PersistenciaDados persistencia;
    private static final String CAMINHO_ARQUIVO_LOG = "data/log_partida.txt";

    /**
     * Construtor do modelo do jogo.
     * @param jogador1 O primeiro jogador.
     * @param jogador2 O segundo jogador.
     */
    public JogoDamasModelo(Jogador jogador1, Jogador jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        this.tabuleiro = new Tabuleiro();
        this.jogadorAtual = jogador1.getCorPecas() == Cor.BRANCAS ? jogador1 : jogador2; // Brancas sempre começam
        this.logPartida = new ArrayList<>();
        this.persistencia = new PersistenciaDados();
        registrarLog("Jogo iniciado entre " + jogador1.getNome() + " e " + jogador2.getNome() + ". Primeiro a jogar: " + jogadorAtual.getNome());
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    public Jogador getJogador1() {
        return jogador1;
    }

    public Jogador getJogador2() {
        return jogador2;
    }

    /**
     * Realiza um movimento no tabuleiro.
     * @param origem Posição de origem da peça.
     * @param destino Posição de destino da peça.
     * @throws MovimentoInvalidoException Se o movimento for inválido.
     * @throws PecaInexistenteException Se não houver peça na origem.
     */
    public void realizarMovimento(Posicao origem, Posicao destino) throws MovimentoInvalidoException, PecaInexistenteException {
        Peca peca = tabuleiro.getPeca(origem);

        if (peca == null) {
            throw new PecaInexistenteException("Não há peça na posição de origem.");
        }

        if (peca.getCor() != jogadorAtual.getCorPecas()) {
            throw new MovimentoInvalidoException("Não é a vez do jogador " + jogadorAtual.getNome() + ".");
        }

        tabuleiro.moverPeca(origem, destino);
        registrarLog(jogadorAtual.getNome() + " moveu a peça de " + origem + " para " + destino);
        proximoTurno();
    }

    /**
     * Verifica se há um vencedor no jogo.
     * @return O jogador vencedor, ou null se não houver.
     */
    public Jogador verificarVitoria() {
        int pecasBrancas = 0;
        int pecasPretas = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Peca p = tabuleiro.getPeca(new Posicao(i, j));
                if (p != null) {
                    if (p.getCor() == Cor.BRANCAS) {
                        pecasBrancas++;
                    } else {
                        pecasPretas++;
                    }
                }
            }
        }

        if (pecasBrancas == 0) {
            registrarLog(jogador2.getNome() + " (Pretas) venceu o jogo!");
            return jogador2; // Jogador com peças pretas venceu
        } else if (pecasPretas == 0) {
            registrarLog(jogador1.getNome() + " (Brancas) venceu o jogo!");
            return jogador1; // Jogador com peças brancas venceu
        }
        return null;
    }

    /**
     * Passa o turno para o próximo jogador.
     */
    public void proximoTurno() {
        jogadorAtual = (jogadorAtual == jogador1) ? jogador2 : jogador1;
        registrarLog("Próximo turno: " + jogadorAtual.getNome());
    }

    /**
     * Registra uma entrada no log da partida.
     * @param entrada A mensagem a ser logada.
     */
    private void registrarLog(String entrada) {
        logPartida.add(entrada);
        try {
            persistencia.registrarLog(CAMINHO_ARQUIVO_LOG, entrada);
        } catch (IOException e) {
            System.err.println("Erro ao registrar log: " + e.getMessage());
        }
    }

    public List<String> getLogPartida() {
        return logPartida;
    }
}