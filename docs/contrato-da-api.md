# Contrato da API

## Base URL

Os endpoints atuais usam o prefixo `/api/v1`.

## Recursos

- Produtos: `/api/v1/product`
- Máquinas: `/api/v1/machine`
- Ordens: `/api/v1/production-order`
- Apontamentos: `/api/v1/production-order/{id}/records`

As listagens retornam `Page<>` e aceitam paginação por `page`, `size` e `sort`.

## Status HTTP

- `200`: consultas, atualizações e transições concluídas;
- `201`: criação de produto, máquina, ordem ou apontamento;
- `204`: operações que não retornam corpo, quando aplicável;
- `400`: validação, JSON inválido ou parâmetro inválido;
- `404`: recurso inexistente;
- `409`: violação de regra de negócio ou integridade.

## Erros

O formato comum é:

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

`campos` é omitido quando o erro não está associado a propriedades específicas.

## Compatibilidade

As rotas versionadas, a paginação e o status `409` para regras de negócio são mantidos para preservar o contrato já implementado. A arquitetura de referência sugere rotas plurais sem versão e `400` para regras de negócio; essa diferença está registrada na autoavaliação.
