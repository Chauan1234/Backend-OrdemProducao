package api.service;

// CAMADA: Service
// RESPONSABILIDADE: representar uma violação de regra de negócio.
// REGRAS: o contrato atual da API converte a exceção em HTTP 409.
// ROTAS ATENDIDAS: rotas de produtos, máquinas, ordens e apontamentos.
// SITUAÇÃO: implementada.
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String message) {
        super(message);
    }
}
