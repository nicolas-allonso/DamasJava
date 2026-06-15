package damas.gui;

import damas.modelo.JogoDamasModelo;
import damas.modelo.JogoConfig;
import damas.modelo.Jogador;
import damas.modelo.Posicao;
import damas.persistencia.PersistenciaDados;
import damas.excecoes.MovimentoInvalidoException;
import damas.excecoes.PecaInexistenteException;

import java.io.IOException;
import java.io.FileNotFoundException;

public class JogoDamasController {

    private JogoDamasModelo modelo;
    private JogoDamasGUI view;
    private PersistenciaDados persistencia;
    private Posicao origemSelecionada;

    private static final String CAMINHO_ARQUIVO_BINARIO = "data/dados_jogo.dat";

    public JogoDamasController() {
        this.persistencia = new PersistenciaDados();
        carregarConfiguracaoInicial();
        this.view = new JogoDamasGUI(this);
        iniciarJogo();
    }

    private void carregarConfiguracaoInicial() {
        try {
            JogoConfig config = persistencia.carregarDados(CAMINHO_ARQUIVO_BINARIO);
            this.modelo = new JogoDamasModelo(config.getJogador1(), config.getJogador2());
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo de configuracao nao encontrado. Execute o P1 primeiro. " + e.getMessage());
            System.exit(1);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar configuracao do jogo: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void iniciarJogo() {
        view.atualizarTabuleiro(modelo.getTabuleiro());
        view.exibirMensagem("Turno de " + modelo.getJogadorAtual().getNome() + " (" + modelo.getJogadorAtual().getCorPecas() + ")");
    }


    public void lidarComClique(int linha, int coluna) {
        Posicao clicada = new Posicao(linha, coluna);
        Posicao encadeada = modelo.getPecaComCapturaEncadeada();

        if (origemSelecionada == null) {

            if (encadeada != null) {
                if (!clicada.equals(encadeada)) {
                    view.exibirMensagem("Voce deve continuar capturando com a peca em " + encadeada + ".");
                    view.setSelectedPosicao(encadeada);
                    return;
                }
            }

            if (modelo.getTabuleiro().getPeca(clicada) != null &&
                modelo.getTabuleiro().getPeca(clicada).getCor() == modelo.getJogadorAtual().getCorPecas()) {
                origemSelecionada = clicada;
                view.setSelectedPosicao(origemSelecionada);
                view.exibirMensagem("Peca selecionada em " + origemSelecionada + ". Escolha o destino.");
            } else {
                view.exibirMensagem("Selecione uma peca valida do seu turno.");
            }
        } else {
            try {
                modelo.realizarMovimento(origemSelecionada, clicada);
                origemSelecionada = null;
                view.atualizarTabuleiro(modelo.getTabuleiro());


                Jogador vencedor = modelo.verificarVitoria();
                if (vencedor != null) {
                    modelo.registrarResultadoFinal(vencedor);
                    view.encerrarJogo(vencedor.getNome() + " (" + vencedor.getCorPecas() + ") venceu o jogo!");
                } else if (modelo.getPecaComCapturaEncadeada() != null) {

                    Posicao pendente = modelo.getPecaComCapturaEncadeada();
                    view.setSelectedPosicao(pendente);
                    view.exibirMensagem("Captura encadeada! " + modelo.getJogadorAtual().getNome() +
                                        " deve continuar capturando com a peca em " + pendente + ".");
                } else {
                    view.setSelectedPosicao(null);
                    view.exibirMensagem("Turno de " + modelo.getJogadorAtual().getNome() +
                                        " (" + modelo.getJogadorAtual().getCorPecas() + ")");
                }
            } catch (MovimentoInvalidoException | PecaInexistenteException e) {
                view.exibirMensagem("Movimento invalido: " + e.getMessage());
                view.setSelectedPosicao(null);
                origemSelecionada = null;
            }
        }
    }

    public JogoDamasModelo getModelo() { return modelo; }
}
