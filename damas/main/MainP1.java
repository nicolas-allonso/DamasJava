package damas.main;

import damas.modelo.Jogador;
import damas.modelo.JogoConfig;
import damas.persistencia.PersistenciaDados;
import java.io.IOException;

/**
 * Programa de Inicialização (P1).
 * Responsável por ler dados dos jogadores de um CSV, criar objetos de configuração
 * e serializá-los para um arquivo binário.
 * Requisito: P1 – Programa de Inicialização.
 */
public class MainP1 {

    private static final String CAMINHO_CSV_ENTRADA = "damas/main/jogadores.csv";
    private static final String CAMINHO_ARQUIVO_BINARIO = "data/dados_jogo.dat";

    public static void main(String[] args) {
        PersistenciaDados persistencia = new PersistenciaDados();

        try {
            // 1. Ler um arquivo texto (.txt ou .csv).
            // 2. Criar objetos persistentes contendo Nome do Jogador 1, Nome do Jogador 2, Cor das peças de cada jogador.
            Jogador[] jogadores = persistencia.lerJogadoresCSV(CAMINHO_CSV_ENTRADA);
            JogoConfig jogoConfig = new JogoConfig(jogadores[0], jogadores[1]);

            // 3. Salvar esses objetos em arquivo binário utilizando serialização Java.
            persistencia.salvarDados(CAMINHO_ARQUIVO_BINARIO, jogoConfig);

            System.out.println("Dados do jogo salvos com sucesso em: " + CAMINHO_ARQUIVO_BINARIO);

        } catch (IOException e) {
            System.err.println("Erro de I/O ao processar arquivos: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Erro nos dados de entrada: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        // 4. Encerrar após gerar o arquivo binário. (O programa termina aqui)
    }
}