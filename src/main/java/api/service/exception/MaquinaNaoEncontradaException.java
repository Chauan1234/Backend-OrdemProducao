package api.service.exception;

/**
 * Representa a ausência de uma máquina solicitada.
 */

public class MaquinaNaoEncontradaException extends RecursoNaoEncontradoException {

    public MaquinaNaoEncontradaException(Object identificador) {
        super("Máquina", identificador);
    }
}
