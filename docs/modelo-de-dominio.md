# Modelo de domínio

## Contexto

A API gerencia produtos, máquinas, ordens de produção e apontamentos realizados durante a fabricação.

## Entidades

- `Product`: produto que pode ser fabricado e desativado.
- `Machine`: máquina disponível, em manutenção ou inativa.
- `ProductionOrder`: ordem associada a um produto e a uma máquina, com ciclo de vida próprio.
- `ProductionRecord`: apontamento de quantidade produzida associado a uma ordem.

## Relacionamentos

```text
Product 1 ───< ProductionOrder >─── 1 Machine
                     1
                     │
                     ∧
              ProductionRecord
```

As tabelas são criadas e atualizadas exclusivamente pelas migrations Flyway:

- `V1__create_production_schema.sql`: tipos, tabelas, chaves e índices;
- `V2__seed_demo_data.sql`: dados de demonstração;
- `V3__convert_ids_to_uuid.sql`: conversão dos identificadores para UUID.

## Organização do código

O código Java está organizado por camada em `src/main/java/api`:

```text
api/
├── Application.java
├── config/
├── controller/
├── service/
├── mapper/
├── repository/
├── model/
└── dto/
```
