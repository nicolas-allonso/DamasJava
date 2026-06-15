package damas.main;

import damas.modelo.Jogador;
import damas.modelo.JogoConfig;
import damas.persistencia.PersistenciaDados;
import java.io.File;
import java.io.IOException;

public class MainP1 {

    private static final String CAMINHO_CSV_ENTRADA = "damas/main/jogadores.csv";
    private static final String CAMINHO_ARQUIVO_BINARIO = "data/dados_jogo.dat";

    public static void main(String[] args) {
        PersistenciaDados persistencia = new PersistenciaDados();

        new File("data").mkdirs();

        try {


            Jogador[] jogadores = persistencia.lerJogadoresCSV(CAMINHO_CSV_ENTRADA);
            JogoConfig jogoConfig = new JogoConfig(jogadores[0], jogadores[1]);


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

    }
}