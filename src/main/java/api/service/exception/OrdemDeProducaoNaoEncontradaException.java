package api.service.exception;

/**
 * Representa a ausência de uma ordem de produção solicitada.
 */

public class OrdemDeProducaoNaoEncontradaException extends RecursoNaoEncontradoException {

    public OrdemDeProducaoNaoEncontradaException(Object identificador) {
        super("Ordem de produção", identificador);
    }
}
