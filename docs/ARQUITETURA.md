# Arquitetura didatica

## Fluxo CRUD
Controller -> DTO -> UseCase -> Mapper -> Repository -> PostgreSQL

## Fluxo de processo
Controller -> ProcessoPedidoUseCase -> PipelineExecutor -> N ProcessStrategy ordenadas -> Repositories -> PostgreSQL

## Dois usos de Strategy
1. `ProcessStrategy`: representa uma tarefa intercambiavel dentro de um processo.
2. `FreteStrategy`: representa um algoritmo de frete (NORMAL ou EXPRESSO).

Isso permite ensinar que Strategy descreve uma intencao de projeto, nao um nome fixo de classe.
