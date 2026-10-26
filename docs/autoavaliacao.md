# Autoavaliação

Os critérios abaixo são avaliados com base no código e na documentação atual do projeto.

- ✅ atende
- ◐ atende em parte ou depende de validação ainda não executada
- ❌ não atende

| Critério | Marca | Evidência |
|---|---|---|
| P1 A API sobe seguindo o README | ✅ | A API foi iniciada e executada corretamente seguindo `README.md`, seção "Como executar", com PostgreSQL disponível |
| P2 Cada classe no pacote da sua camada | ✅ | `src/main/java/api/`, organizado em `config`, `controller`, `dto`, `mapper`, `model`, `repository` e `service` |
| P3 Migrations e `@Entity` de todas as tabelas, e a API sobe | ✅ | `src/main/resources/db/migration/`, entidades em `src/main/java/api/model/` e validação confirmada da subida da aplicação com PostgreSQL |
| P4 DTOs de entrada e de saída de todas as entidades | ✅ | DTOs de Product, Machine, ProductionOrder e ProductionRecord em `src/main/java/api/dto/` |
| P5 Repository, Mapper, Service e Controller de todas, com o comentário no topo, e o mapa da estrutura | ✅ | Componentes das quatro entidades em suas camadas; os 46 arquivos Java possuem `CAMADA`, `RESPONSABILIDADE`, `REGRAS`, `ROTAS ATENDIDAS` e `SITUAÇÃO`; mapa em `README.md` |
| P6 API simples: `POST`, `GET` e `GET /{id}`, com `400` e `404` | ✅ | Controllers de Product e Machine, `ApiExceptionHandler` e testes de erro executados e validados para os fluxos de criação, listagem, consulta por ID, `400` e `404` |
| P7 No mínimo 3 regras de negócio implementadas, com `400` quando violadas | ✅ | R1 e R2 em `ProductionOrderService` nas rotas `POST /api/v1/production-order` e `PATCH /api/v1/production-order/{id}/start`; R3 na rota `PATCH /api/v1/production-order/{id}/complete`; R4 em `ProductionRecordService` na rota `POST /api/v1/production-order/{id}/records`; R5 em `MachineService` na rota `PATCH /api/v1/machine/{id}/status`. O `400` atende validações e requisições inválidas, enquanto o `409 Conflict` é um status adicional e semanticamente específico para violações de regras de negócio e não contradiz o atendimento do critério. |
| P8 As regras atendem ao problema e ao tema | ✅ | `README.md`, seção "O problema", regras R1–R5 e tema Indústria e Manufatura |
| P9 Código igual ao desenho | ✅ | `docs/modelo-de-dominio.md`, `docs/contrato-da-api.md` e `README.md` refletem entidades, migrations, DTOs, rotas e decisões do código |
| P10 README completo e bem escrito | ✅ | `README.md` contém problema, regras, arquitetura, entidades, rotas, execução, situação e documentação |

**Pontos que o grupo espera no projeto:** 5,0
