package api.service;

// CAMADA: Service
// RESPONSABILIDADE: indicar que uma máquina solicitada não existe.
// REGRAS: mantém a mensagem de recurso não encontrado da API.
// ROTAS ATENDIDAS: as rotas de máquinas e ordens que consultam máquinas.
// SITUAÇÃO: implementada.
public class MaquinaNaoEncontradaException extends RecursoNaoEncontradoException {

    public MaquinaNaoEncontradaException(Object identificador) {
        super("Máquina", identificador);
    }
}
