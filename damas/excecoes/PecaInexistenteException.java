package damas.excecoes;

/**
 * Exceção personalizada para indicar que uma peça não existe na posição especificada.
 * Herda de `java.lang.Exception`.
 * Requisito: Exceções Personalizadas.
 */
public class PecaInexistenteException extends Exception {
    public PecaInexistenteException(String message) {
        super(message);
    }
}