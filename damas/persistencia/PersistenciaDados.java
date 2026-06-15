package damas.persistencia;

import damas.modelo.Cor;
import damas.modelo.Jogador;
import damas.modelo.JogoConfig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PersistenciaDados {


    public void salvarDados(String caminhoArquivo, JogoConfig jogoConfig) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(caminhoArquivo)))) {
            oos.writeObject(jogoConfig);
        }
    }


    public JogoConfig carregarDados(String caminhoArquivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(caminhoArquivo)))) {
            return (JogoConfig) ois.readObject();
        }
    }


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


    public void registrarLog(String caminhoArquivo, String logEntry) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(caminhoArquivo), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            bw.write(timestamp + " - " + logEntry);
            bw.newLine();
        }
    }
}