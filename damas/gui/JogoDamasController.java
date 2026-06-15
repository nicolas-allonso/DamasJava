package damas.gui;

import damas.modelo.JogoDamasModelo;
import damas.modelo.JogoConfig;
import damas.modelo.Posicao;
import damas.persistencia.PersistenciaDados;
import damas.excecoes.MovimentoInvalidoException;
import damas.excecoes.PecaInexistenteException;

import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Controlador do jogo de damas, responsável por gerenciar a interação entre o modelo (lógica do jogo)
 * e a visão (interface gráfica).
 */
public class JogoDamasController {

    private JogoDamasModelo modelo;
    private JogoDamasGUI view;
    private PersistenciaDados persistencia;
    private Posicao origemSelecionada;

    private static final String CAMINHO_ARQUIVO_BINARIO = "data/dados_jogo.dat";

    /**
     * Construtor do controlador.
     */
    public JogoDamasController() {
        this.persistencia = new PersistenciaDados();
        carregarConfiguracaoInicial();
        this.view = new JogoDamasGUI(this);
        iniciarJogo();
    }

    /**
     * Carrega a configuração inicial do jogo (jogadores) do arquivo binário.
     */
    private void carregarConfiguracaoInicial() {
        try {
            JogoConfig config = persistencia.carregarDados(CAMINHO_ARQUIVO_BINARIO);
            this.modelo = new JogoDamasModelo(config.getJogador1(), config.getJogador2());
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo de configuração não encontrado. Execute o P1 primeiro. " + e.getMessage());
            System.exit(1);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar configuração do jogo: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Inicia o jogo, atualizando a interface gráfica e exibindo a mensagem inicial.
     */
    public void iniciarJogo() {
        view.atualizarTabuleiro(modelo.getTabuleiro());
        view.exibirMensagem("Turno de " + modelo.getJogadorAtual().getNome() + " (" + modelo.getJogadorAtual().getCorPecas() + ")");
    }

    /**
     * Lida com o clique do mouse em uma célula do tabuleiro.
     * @param linha A linha da célula clicada.
     * @param coluna A coluna da célula clicada.
     */
    public void lidarComClique(int linha, int coluna) {
        Posicao clicada = new Posicao(linha, coluna);

        if (origemSelecionada == null) {
            // Nenhuma peça selecionada, tentar selecionar uma
            if (modelo.getTabuleiro().getPeca(clicada) != null && modelo.getTabuleiro().getPeca(clicada).getCor() == modelo.getJogadorAtual().getCorPecas()) {
                origemSelecionada = clicada;
                view.setSelectedPosicao(origemSelecionada);
                view.exibirMensagem("Peça selecionada em " + origemSelecionada + ". Escolha o destino.");
            } else {
                view.exibirMensagem("Selecione uma peça válida do seu turno.");
            }
        } else {
            // Uma peça já está selecionada, tentar mover para a posição clicada
            try {
                modelo.realizarMovimento(origemSelecionada, clicada);
                view.setSelectedPosicao(null); // Limpa a seleção
                origemSelecionada = null;
                view.atualizarTabuleiro(modelo.getTabuleiro());

                // Verifica se há um vencedor
                if (modelo.verificarVitoria() != null) {
                    view.exibirMensagem(modelo.verificarVitoria().getNome() + " venceu o jogo!");
                    // Desabilitar interação ou mostrar opção de novo jogo
                } else {
                    view.exibirMensagem("Turno de " + modelo.getJogadorAtual().getNome() + " (" + modelo.getJogadorAtual().getCorPecas() + ")");
                }
            } catch (MovimentoInvalidoException | PecaInexistenteException e) {
                view.exibirMensagem("Movimento inválido: " + e.getMessage());
                view.setSelectedPosicao(null); // Limpa a seleção em caso de erro
                origemSelecionada = null;
            }
        }
    }

    public JogoDamasModelo getModelo() {
        return modelo;
    }
}