package api.service.exception;

/**
 * Representa uma violação de regra de negócio.
 */

public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String message) {
        super(message);
    }
}
