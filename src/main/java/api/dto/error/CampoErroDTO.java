package api.dto.error;

/**
 * Representa um erro de validação associado a um campo.
 */

public record CampoErroDTO(String campo, String mensagem) {
}
