package api.service.exception;

/**
 * Representa uma falha de domínio causada pela ausência de um recurso.
 */

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Object identificador) {
        super(recurso + " não encontrado: " + identificador);
    }
}
