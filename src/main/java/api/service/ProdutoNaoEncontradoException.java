package api.service;

// CAMADA: Service
// RESPONSABILIDADE: indicar que um produto solicitado não existe.
// REGRAS: mantém a mensagem de recurso não encontrado da API.
// ROTAS ATENDIDAS: as rotas de produtos e ordens que consultam produtos.
// SITUAÇÃO: implementada.
public class ProdutoNaoEncontradoException extends RecursoNaoEncontradoException {

    public ProdutoNaoEncontradoException(Object identificador) {
        super("Produto", identificador);
    }
}
