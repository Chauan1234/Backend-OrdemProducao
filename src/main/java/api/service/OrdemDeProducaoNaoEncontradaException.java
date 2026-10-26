package api.service;

// CAMADA: Service
// RESPONSABILIDADE: indicar que uma ordem de produção solicitada não existe.
// REGRAS: mantém a mensagem de recurso não encontrado da API.
// ROTAS ATENDIDAS: as rotas de ordens e apontamentos.
// SITUAÇÃO: implementada.
public class OrdemDeProducaoNaoEncontradaException extends RecursoNaoEncontradoException {

    public OrdemDeProducaoNaoEncontradaException(Object identificador) {
        super("Ordem de produção", identificador);
    }
}
