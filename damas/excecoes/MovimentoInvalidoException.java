package damas.excecoes;

/**
 * Exceção personalizada para indicar um movimento inválido no jogo de damas.
 * Herda de `java.lang.Exception`.
 * Requisito: Exceções Personalizadas.
 */
public class MovimentoInvalidoException extends Exception {
    public MovimentoInvalidoException(String message) {
        super(message);
    }
}