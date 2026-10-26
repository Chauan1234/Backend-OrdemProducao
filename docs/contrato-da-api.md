# Contrato da API

O contrato abaixo descreve as rotas implementadas nos controllers, os DTOs usados pela aplicação e os status produzidos pelo tratamento centralizado de exceções.

## Convenções

| Item | Valor |
|---|---|
| Endereço base | `http://localhost:8080` |
| Prefixo | `/api/v1` |
| Formato | JSON na requisição e na resposta |
| Datas | `yyyy-MM-dd` para `LocalDate`; ISO-8601 para `LocalDateTime` e `Instant` |
| Paginação | `page`, `size` e `sort`; listagens retornam `Page<>` |
| Erros | `ErroDTO`, com `campos` somente quando há propriedades inválidas |
| Documentação viva | `http://localhost:8080/swagger-ui/index.html` |

## Rotas

Todas as rotas abaixo estão implementadas nos controllers atuais.

### Produtos — `/api/v1/product`

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| `GET` | `/api/v1/product` | Lista produtos e filtra opcionalmente por `active` | Query `active`, paginação | `Page<ProductResponseDTO>` | `200` | `400` | sim |
| `GET` | `/api/v1/product/code/{code}` | Busca produto pelo código | Path `code` | `ProductResponseDTO` | `200` | `404` | sim |
| `GET` | `/api/v1/product/name/{name}` | Busca produtos por nome, ignorando maiúsculas/minúsculas | Path `name`, paginação | `Page<ProductResponseDTO>` | `200` | `400` | sim |
| `POST` | `/api/v1/product` | Cria produto | `ProductRequestDTO` | `ProductResponseDTO` | `201` | `400` | sim |
| `GET` | `/api/v1/product/{id}` | Busca produto pelo UUID | Path `id` | `ProductResponseDTO` | `200` | `404` | sim |
| `PUT` | `/api/v1/product/{id}` | Atualiza produto | Path `id` e `ProductRequestDTO` | `ProductResponseDTO` | `200` | `400`, `404` | sim |
| `PATCH` | `/api/v1/product/{id}/deactivate` | Desativa produto | Path `id` | `ProductResponseDTO` | `200` | `404` | sim |

### Máquinas — `/api/v1/machine`

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| `GET` | `/api/v1/machine` | Lista máquinas e filtra opcionalmente por `status` | Query `status`, paginação | `Page<MachineResponseDTO>` | `200` | `400` | sim |
| `GET` | `/api/v1/machine/code/{code}` | Busca máquina pelo código | Path `code` | `MachineResponseDTO` | `200` | `404` | sim |
| `GET` | `/api/v1/machine/name/{name}` | Busca máquinas por nome | Path `name`, paginação | `Page<MachineResponseDTO>` | `200` | `400` | sim |
| `GET` | `/api/v1/machine/{id}` | Busca máquina pelo UUID | Path `id` | `MachineResponseDTO` | `200` | `404` | sim |
| `POST` | `/api/v1/machine` | Cria máquina com status inicial `AVAILABLE` | `MachineRequestDTO` | `MachineResponseDTO` | `201` | `400` | sim |
| `PUT` | `/api/v1/machine/{id}` | Atualiza máquina | Path `id` e `MachineRequestDTO` | `MachineResponseDTO` | `200` | `400`, `404` | sim |
| `PATCH` | `/api/v1/machine/{id}/status` | Altera o status da máquina | Path `id` e `UpdateMachineStatusRequestDTO` | `MachineResponseDTO` | `200` | `400`, `404`, `409` | sim |

### Ordens de produção — `/api/v1/production-order`

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| `GET` | `/api/v1/production-order` | Lista ordens com filtros opcionais | Query `status`, `productId`, `machineId`, `deadline`, paginação | `Page<ProductionOrderSummaryDTO>` | `200` | `400` | sim |
| `GET` | `/api/v1/production-order/{id}` | Busca ordem pelo UUID | Path `id` | `ProductionOrderResponseDTO` | `200` | `404` | sim |
| `POST` | `/api/v1/production-order` | Cria ordem para produto e máquina disponíveis | `ProductionOrderRequestDTO` | `ProductionOrderResponseDTO` | `201` | `400`, `404`, `409` | sim |
| `PATCH` | `/api/v1/production-order/{id}/start` | Inicia uma ordem pendente | Path `id` | `ProductionOrderResponseDTO` | `200` | `404`, `409` | sim |
| `PATCH` | `/api/v1/production-order/{id}/complete` | Conclui uma ordem com produção completa | Path `id` | `ProductionOrderResponseDTO` | `200` | `404`, `409` | sim |
| `PATCH` | `/api/v1/production-order/{id}/cancel` | Cancela ordem pendente ou em andamento | Path `id` | `ProductionOrderResponseDTO` | `200` | `404`, `409` | sim |

### Apontamentos — `/api/v1/production-order/{id}/records`

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| `GET` | `/api/v1/production-order/{id}/records` | Lista apontamentos da ordem | Path `id`, paginação | `Page<ProductionRecordResponseDTO>` | `200` | `404` | sim |
| `POST` | `/api/v1/production-order/{id}/records` | Registra quantidade produzida | Path `id` e `ProductionRecordRequestDTO` | `ProductionRecordResponseDTO` | `201` | `400`, `404`, `409` | sim |

## Rotas que não são CRUD

| Verbo | Caminho | O que faz | Regra | Sucesso | Erros |
|---|---|---|---|---|---|
| `PATCH` | `/api/v1/product/{id}/deactivate` | Desativa um produto sem excluí-lo | Produtos são desativados, não removidos | `200` | `404` |
| `PATCH` | `/api/v1/machine/{id}/status` | Altera o estado da máquina | Máquina em execução não pode mudar de estado | `200` | `400`, `404`, `409` |
| `PATCH` | `/api/v1/production-order/{id}/start` | Inicia uma ordem pendente | Produto ativo, máquina disponível e sem outra ordem em andamento | `200` | `404`, `409` |
| `PATCH` | `/api/v1/production-order/{id}/complete` | Conclui uma ordem | Produção deve atingir a quantidade planejada | `200` | `404`, `409` |
| `PATCH` | `/api/v1/production-order/{id}/cancel` | Cancela uma ordem | Somente ordens pendentes ou em andamento | `200` | `404`, `409` |
| `POST` | `/api/v1/production-order/{id}/records` | Registra produção | Ordem em andamento e total produzido dentro do planejado | `201` | `400`, `404`, `409` |

## DTOs

| DTO | Direção | Campos | Validações ou regras |
|---|---|---|---|
| `ProductRequestDTO` | entrada | `name`, `description`, `active` | `name` obrigatório, até 150 caracteres |
| `ProductResponseDTO` | saída | `id`, `code`, `name`, `description`, `active` | Nenhuma |
| `MachineRequestDTO` | entrada | `name` | Obrigatório, até 150 caracteres |
| `MachineResponseDTO` | saída | `id`, `code`, `name`, `status` | Nenhuma |
| `UpdateMachineStatusRequestDTO` | entrada | `status` | Obrigatório; enum de status da máquina |
| `ProductionOrderRequestDTO` | entrada | `productId`, `machineId`, `targetQuantity`, `deadline` | IDs obrigatórios, quantidade positiva, prazo hoje ou futuro |
| `ProductionOrderResponseDTO` | saída | `id`, `product`, `machine`, `targetQuantity`, `producedQuantity`, `status`, `createdAt`, `startedAt`, `completedAt`, `deadline` | Nenhuma |
| `ProductionOrderSummaryDTO` | saída | `id`, códigos e nomes de produto/máquina, quantidades, `status`, `deadline` | Nenhuma |
| `ProductionRecordRequestDTO` | entrada | `quantity`, `notes` | Quantidade obrigatória e positiva |
| `ProductionRecordResponseDTO` | saída | `id`, `productionOrderId`, `quantity`, `registeredAt`, `notes` | Nenhuma |
| `ErroDTO` | saída | `timestamp`, `status`, `error`, `message`, `path`, `campos` | `campos` é omitido quando não se aplica |
| `CampoErroDTO` | saída | `campo`, `mensagem` | Item de erro de validação |

## Exemplos

### Criar produto

`POST /api/v1/product`

```json
{
  "name": "Eixo de transmissão",
  "description": "Eixo usinado para equipamentos industriais",
  "active": true
}
```

Resposta `201 Created`:

```json
{
  "id": "c37c6e28-9ab2-4b48-9d7d-59bb5152a013",
  "code": 12,
  "name": "Eixo de transmissão",
  "description": "Eixo usinado para equipamentos industriais",
  "active": true
}
```

### Criar máquina

`POST /api/v1/machine`

```json
{
  "name": "Torno CNC 01"
}
```

Resposta `201 Created` inclui `status: "AVAILABLE"`.

### Criar ordem

`POST /api/v1/production-order`

```json
{
  "productId": "c37c6e28-9ab2-4b48-9d7d-59bb5152a013",
  "machineId": "6b9d53f4-c6a8-4d05-8ef5-cf91a1ff323c",
  "targetQuantity": 500,
  "deadline": "2026-11-15"
}
```

Resposta `201 Created` retorna `ProductionOrderResponseDTO` com status inicial `PENDING` e `producedQuantity: 0`.

### Registrar produção

`POST /api/v1/production-order/{id}/records`

```json
{
  "quantity": 150,
  "notes": "Lote produzido sem ocorrências."
}
```

Resposta `201 Created` retorna o registro com `productionOrderId`, `registeredAt` e a quantidade registrada.

## Erros

O formato comum é:

```json
{
  "timestamp": "2026-10-26T14:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Produto não encontrado: c37c6e28-9ab2-4b48-9d7d-59bb5152a013",
  "path": "/api/v1/product/c37c6e28-9ab2-4b48-9d7d-59bb5152a013"
}
```

Quando a falha é de validação, `campos` é incluído:

```json
{
  "timestamp": "2026-10-26T14:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "name: não deve estar em branco",
  "path": "/api/v1/product",
  "campos": [
    {
      "campo": "name",
      "mensagem": "não deve estar em branco"
    }
  ]
}
```

| Status | Quando acontece |
|---|---|
| `200` | Consulta, atualização ou transição concluída |
| `201` | Produto, máquina, ordem ou apontamento criado |
| `400` | Validação, JSON inválido ou parâmetro inválido |
| `404` | Recurso inexistente |
| `409` | Regra de negócio ou restrição de integridade violada |

## Decisões e extensões do contrato

O projeto preserva o contrato implementado e registra as seguintes decisões e extensões em relação ao modelo de referência:

- usa o prefixo `/api/v1`;
- usa nomes de recursos no singular;
- mantém `400` para validações e requisições inválidas e adiciona `409 Conflict` como status específico para violações de regras de negócio e integridade;
- usa paginação com `Page<>`;
- usa as chaves de erro `error`, `message` e `path`, enquanto o modelo apresenta `erro`, `mensagem` e `caminho`.
