package api.service;

// CAMADA: Service
// RESPONSABILIDADE: representar a ausência de um recurso do domínio.
// REGRAS: preserva a mensagem histórica usada pela API.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Object identificador) {
        super(recurso + " não encontrado: " + identificador);
    }
}
