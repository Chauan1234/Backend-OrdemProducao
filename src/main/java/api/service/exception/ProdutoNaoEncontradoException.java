package api.service.exception;

/**
 * Representa a ausência de um produto solicitado.
 */

public class ProdutoNaoEncontradoException extends RecursoNaoEncontradoException {

    public ProdutoNaoEncontradoException(Object identificador) {
        super("Produto", identificador);
    }
}
