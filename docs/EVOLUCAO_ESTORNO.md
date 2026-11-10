# Evolucao: estorno como Pipeline + Strategy

## Problema

Um pedido que ja recebeu pagamento nao pode ser tratado como um cancelamento simples. Existem pelo menos tres efeitos de negocio: reverter o pagamento, devolver o estoque e cancelar o pedido.

## Solucao aplicada

Foi criado o processo `ESTORNAR_PAGAMENTO`, composto por quatro `ProcessStrategy`:

1. `ValidarEstornoPagamentoStrategy` - garante pagamento `APROVADO` e pedido `APROVADO`.
2. `EstornarPagamentoStrategy` - muda o pagamento para `ESTORNADO`.
3. `DevolverEstoqueEstornoStrategy` - devolve cada item e cria uma movimentacao `ENTRADA` com motivo de estorno.
4. `CancelarPedidoAposEstornoStrategy` - muda o pedido para `CANCELADO`.

## Regras importantes

- Um pagamento `PENDENTE`, `RECUSADO`, `CANCELADO` ou ja `ESTORNADO` nao pode entrar novamente no processo.
- O cancelamento direto continua bloqueando pedidos `APROVADO`.
- O endpoint didatico e `POST /api/pagamentos/{id}/processos/estornar`.
- Este projeto simula o estorno localmente. Em um e-commerce real, a etapa 20 normalmente chamaria um gateway externo e exigiria tratamento de idempotencia, timeout e reconciliacao.

## Por que isso e didatico

O exemplo mostra que um processo de negocio pode ser uma sequencia de strategies. Cada tarefa tem responsabilidade pequena, ordem explicita e pode evoluir independentemente, enquanto o `PipelineExecutor` permanece generico.
