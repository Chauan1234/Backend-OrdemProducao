package api.dto;

// CAMADA: DTO
// RESPONSABILIDADE: representar uma falha de validação associada a um campo.
// REGRAS: o campo deve identificar o parâmetro ou propriedade inválida.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
public record CampoErroDTO(String campo, String mensagem) {
}
