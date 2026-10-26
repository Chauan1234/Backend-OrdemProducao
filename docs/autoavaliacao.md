# Autoavaliação arquitetural

## Atendido

- O banco é alterado somente por migrations Flyway.
- As regras de negócio ficam nos Services.
- Controllers trabalham com DTOs.
- Repositories concentram o acesso ao banco.
- O tratamento de erros é centralizado em `ApiExceptionHandler`.
- Todas as entidades possuem model, repository, DTOs, mapper e service.
- `ProductionRecord` possui Controller próprio, mantendo suas rotas aninhadas.
- A aplicação possui configuração OpenAPI em `api/config`.
- A estrutura de código segue as camadas `controller`, `service`, `mapper`, `repository`, `model`, `dto` e `config`.

## Divergências conscientes

- O pacote raiz é `api`, conforme a estrutura de referência, mas o contrato mantém o prefixo `/api/v1`.
- As rotas existentes permanecem no singular para não quebrar consumidores.
- Regras de negócio continuam retornando `409 Conflict`, conforme o contrato original do projeto, embora a referência sugira `400 Bad Request`.
- A API mantém paginação com `Page<>` nas listagens.
- As mensagens antigas de não encontrado são preservadas literalmente, inclusive a concordância gramatical original.

## Melhorias futuras

- Avaliar com o professor a migração para rotas plurais sem versionamento.
- Confirmar se regras de negócio devem passar de `409` para `400`.
- Corrigir a concordância gramatical das mensagens sem compatibilidade retroativa.
