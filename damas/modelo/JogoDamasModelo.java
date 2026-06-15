package damas.modelo;

import damas.excecoes.MovimentoInvalidoException;
import damas.excecoes.PecaInexistenteException;
import damas.persistencia.PersistenciaDados;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JogoDamasModelo {
    private Tabuleiro tabuleiro;
    private Jogador jogadorAtual;
    private Jogador jogador1;
    private Jogador jogador2;
    private List<String> logPartida;
    private PersistenciaDados persistencia;
    private static final String CAMINHO_ARQUIVO_LOG = "data/log_partida.txt";
    private static final String CAMINHO_RESULTADO_FINAL = "data/resultado_final.txt";


    private Posicao pecaComCapturaEncadeada = null;


    public JogoDamasModelo(Jogador jogador1, Jogador jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        this.tabuleiro = new Tabuleiro();
        this.jogadorAtual = jogador1.getCorPecas() == Cor.BRANCAS ? jogador1 : jogador2;
        this.logPartida = new ArrayList<>();
        this.persistencia = new PersistenciaDados();
        new File("data").mkdirs();
        registrarLog("Jogo iniciado entre " + jogador1.getNome() + " e " + jogador2.getNome() + ". Primeiro a jogar: " + jogadorAtual.getNome());
    }

    public Tabuleiro getTabuleiro() { return tabuleiro; }
    public Jogador getJogadorAtual() { return jogadorAtual; }
    public Jogador getJogador1() { return jogador1; }
    public Jogador getJogador2() { return jogador2; }


    public Posicao getPecaComCapturaEncadeada() { return pecaComCapturaEncadeada; }


    public void realizarMovimento(Posicao origem, Posicao destino) throws MovimentoInvalidoException, PecaInexistenteException {
        Peca peca = tabuleiro.getPeca(origem);

        if (peca == null) {
            throw new PecaInexistenteException("Nao ha peca na posicao de origem.");
        }

        if (peca.getCor() != jogadorAtual.getCorPecas()) {
            throw new MovimentoInvalidoException("Nao e a vez do jogador " + jogadorAtual.getNome() + ".");
        }


        if (pecaComCapturaEncadeada != null && !origem.equals(pecaComCapturaEncadeada)) {
            throw new MovimentoInvalidoException("Voce deve continuar capturando com a peca em " + pecaComCapturaEncadeada + ".");
        }

        boolean eCaptura = Math.abs(destino.getLinha() - origem.getLinha()) == 2;

        tabuleiro.moverPeca(origem, destino);
        registrarLog(jogadorAtual.getNome() + " moveu a peca de " + origem + " para " + destino);


        if (eCaptura && tabuleiro.temCapturaDisponivel(destino)) {
            pecaComCapturaEncadeada = destino;
            registrarLog(jogadorAtual.getNome() + " deve continuar capturando com a peca em " + destino + ".");
        } else {
            pecaComCapturaEncadeada = null;
            proximoTurno();
        }
    }


    public Jogador verificarVitoria() {
        int pecasBrancas = 0;
        int pecasPretas = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Peca p = tabuleiro.getPeca(new Posicao(i, j));
                if (p != null) {
                    if (p.getCor() == Cor.BRANCAS) pecasBrancas++;
                    else pecasPretas++;
                }
            }
        }


        if (pecasBrancas == 0) {
            Jogador vencedor = getJogadorPorCor(Cor.PRETOS);
            registrarLog(vencedor.getNome() + " (Pretas) venceu: todas as pecas brancas foram capturadas!");
            return vencedor;
        }
        if (pecasPretas == 0) {
            Jogador vencedor = getJogadorPorCor(Cor.BRANCAS);
            registrarLog(vencedor.getNome() + " (Brancas) venceu: todas as pecas pretas foram capturadas!");
            return vencedor;
        }


        if (!tabuleiro.temMovimentoDisponivel(jogadorAtual.getCorPecas())) {
            Jogador vencedor = (jogadorAtual == jogador1) ? jogador2 : jogador1;
            registrarLog(vencedor.getNome() + " venceu: " + jogadorAtual.getNome() + " nao possui movimentos possiveis!");
            return vencedor;
        }

        return null;
    }


    public void proximoTurno() {
        jogadorAtual = (jogadorAtual == jogador1) ? jogador2 : jogador1;
        registrarLog("Proximo turno: " + jogadorAtual.getNome());
    }


    private Jogador getJogadorPorCor(Cor cor) {
        if (jogador1.getCorPecas() == cor) return jogador1;
        return jogador2;
    }


    private void registrarLog(String entrada) {
        logPartida.add(entrada);
        try {
            persistencia.registrarLog(CAMINHO_ARQUIVO_LOG, entrada);
        } catch (IOException e) {
            System.err.println("Erro ao registrar log: " + e.getMessage());
        }
    }


    public void registrarResultadoFinal(Jogador vencedor) {
        int totalMovimentos = (int) logPartida.stream()
                .filter(e -> e.contains("moveu a peca"))
                .count();
        String perdedor = (vencedor == jogador1) ? jogador2.getNome() : jogador1.getNome();
        try {
            persistencia.registrarLog(CAMINHO_RESULTADO_FINAL, "=== RESULTADO FINAL ===");
            persistencia.registrarLog(CAMINHO_RESULTADO_FINAL, "Vencedor : " + vencedor.getNome() + " (" + vencedor.getCorPecas() + ")");
            persistencia.registrarLog(CAMINHO_RESULTADO_FINAL, "Perdedor : " + perdedor);
            persistencia.registrarLog(CAMINHO_RESULTADO_FINAL, "Total de movimentos : " + totalMovimentos);
            persistencia.registrarLog(CAMINHO_RESULTADO_FINAL, "=======================");
        } catch (IOException e) {
            System.err.println("Erro ao registrar resultado final: " + e.getMessage());
        }
    }

    public List<String> getLogPartida() { return logPartida; }
}
