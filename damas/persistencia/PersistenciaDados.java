package damas.persistencia;

import damas.modelo.Cor;
import damas.modelo.Jogador;
import damas.modelo.JogoConfig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe responsável pela persistência de dados do jogo, incluindo serialização,
 * leitura de CSV e gravação de logs.
 * Requisito: Serialização, Leitura de CSV, Gravação de Logs, Métodos com Throws.
 */
public class PersistenciaDados {

    /**
     * Salva um objeto JogoConfig em um arquivo binário usando serialização.
     * Requisito: Serialização, Métodos com Throws.
     * @param caminhoArquivo O caminho completo do arquivo onde o objeto será salvo.
     * @param jogoConfig O objeto JogoConfig a ser serializado.
     * @throws IOException Se ocorrer um erro de I/O durante a escrita do arquivo.
     */
    public void salvarDados(String caminhoArquivo, JogoConfig jogoConfig) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(caminhoArquivo)))) {
            oos.writeObject(jogoConfig);
        }
    }

    /**
     * Carrega um objeto JogoConfig de um arquivo binário usando desserialização.
     * Requisito: Serialização, Métodos com Throws.
     * @param caminhoArquivo O caminho completo do arquivo de onde o objeto será lido.
     * @return O objeto JogoConfig desserializado.
     * @throws IOException Se ocorrer um erro de I/O durante a leitura do arquivo.
     * @throws ClassNotFoundException Se a classe do objeto serializado não for encontrada.
     */
    public JogoConfig carregarDados(String caminhoArquivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(caminhoArquivo)))) {
            return (JogoConfig) ois.readObject();
        }
    }

    /**
     * Lê os dados dos jogadores de um arquivo CSV.
     * Requisito: Leitura de CSV.
     * @param caminhoArquivo O caminho completo do arquivo CSV.
     * @return Um array de Jogador contendo os dois jogadores lidos do CSV.
     * @throws IOException Se ocorrer um erro de I/O durante a leitura do arquivo.
     * @throws IllegalArgumentException Se o formato do CSV for inválido.
     */
    public Jogador[] lerJogadoresCSV(String caminhoArquivo) throws IOException, IllegalArgumentException {
        Jogador[] jogadores = new Jogador[2];
        try (BufferedReader br = Files.newBufferedReader(Paths.get(caminhoArquivo))) {
            String linha;
            int count = 0;
            while ((linha = br.readLine()) != null && count < 2) {
                String[] partes = linha.split(",");
                if (partes.length != 2) {
                    throw new IllegalArgumentException("Formato CSV inválido: cada linha deve ter 'Nome,Cor'.");
                }
                String nome = partes[0].trim();
                Cor cor = Cor.valueOf(partes[1].trim().toUpperCase());
                jogadores[count++] = new Jogador(nome, cor);
            }
            if (count < 2) {
                throw new IllegalArgumentException("Arquivo CSV deve conter dados para dois jogadores.");
            }
        }
        return jogadores;
    }

    /**
     * Registra uma entrada de log em um arquivo.
     * Requisito: Gravação de Logs.
     * @param caminhoArquivo O caminho completo do arquivo de log.
     * @param logEntry A mensagem de log a ser registrada.
     * @throws IOException Se ocorrer um erro de I/O durante a escrita do arquivo.
     */
    public void registrarLog(String caminhoArquivo, String logEntry) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(caminhoArquivo), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            bw.write(timestamp + " - " + logEntry);
            bw.newLine();
        }
    }
}