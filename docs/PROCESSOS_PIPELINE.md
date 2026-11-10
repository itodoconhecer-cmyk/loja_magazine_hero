# Matriz de processos - Pipeline + Strategy

Cada processo e executado pelo `PipelineExecutor`. O executor recebe um `Processo` e um `ProcessContext`, localiza todos os beans que implementam `ProcessStrategy`, filtra pelo processo, ordena pelo metodo `ordem()` e executa as tarefas dentro da mesma transacao.

| Processo | Ordem | Strategy | Responsabilidade |
|---|---:|---|---|
| CALCULAR_FRETE | 10 | ValidarPedidoParaFreteStrategy | Carrega o pedido e valida seu estado |
| CALCULAR_FRETE | 20 | CalcularSubtotalStrategy | Le itens do carrinho, produtos e calcula subtotal |
| CALCULAR_FRETE | 30 | AplicarFreteStrategy | Seleciona a `FreteStrategy`, calcula e persiste total |
| APROVAR_PEDIDO | 10 | ValidarAprovacaoPedidoStrategy | Valida status e se frete/total ja foram calculados |
| APROVAR_PEDIDO | 20 | ReservarEstoquePedidoStrategy | Verifica estoque, baixa estoque e cria snapshot dos itens do pedido |
| APROVAR_PEDIDO | 30 | MarcarPedidoAguardandoPagamentoStrategy | Altera o status para AGUARDANDO_PAGAMENTO |
| APROVACAO_PAGAMENTO | 10 | ValidarPagamentoStrategy | Confere pedido, status e valor do pagamento |
| APROVACAO_PAGAMENTO | 20 | AprovarPagamentoStrategy | Aprova o pagamento |
| APROVACAO_PAGAMENTO | 30 | ConcluirPedidoPagamentoStrategy | Altera o pedido para APROVADO |
| ESVAZIAR_CARRINHO | 10 | ValidarEsvaziarCarrinhoStrategy | Verifica se o carrinho existe |
| ESVAZIAR_CARRINHO | 20 | EsvaziarCarrinhoStrategy | Remove os itens do carrinho |
| CANCELAR_PEDIDO | 10 | ValidarCancelamentoPedidoStrategy | Valida se o pedido pode ser cancelado |
| CANCELAR_PEDIDO | 20 | DevolverEstoqueCancelamentoStrategy | Devolve ao estoque itens previamente reservados |
| CANCELAR_PEDIDO | 30 | MarcarPedidoCanceladoStrategy | Altera o status para CANCELADO |
| EXCLUIR_PEDIDO | 10 | ValidarExclusaoPedidoStrategy | Permite exclusao somente em estados seguros |
| EXCLUIR_PEDIDO | 20 | ExcluirPedidoStrategy | Faz exclusao logica |
| EDITAR_PEDIDO | 10 | ValidarEdicaoPedidoStrategy | Permite edicao apenas em RASCUNHO |
| EDITAR_PEDIDO | 20 | AlterarTipoFretePedidoStrategy | Altera frete e invalida o calculo anterior |
| MOVIMENTACAO_ESTOQUE_ENTRADA | 10 | ValidarMovimentacaoEntradaStrategy | Valida produto e quantidade |
| MOVIMENTACAO_ESTOQUE_ENTRADA | 20 | ExecutarMovimentacaoEntradaStrategy | Atualiza saldo e registra movimento de entrada |
| MOVIMENTACAO_ESTOQUE_SAIDA | 10 | ValidarMovimentacaoSaidaStrategy | Valida produto, quantidade e saldo |
| MOVIMENTACAO_ESTOQUE_SAIDA | 20 | ExecutarMovimentacaoSaidaStrategy | Atualiza saldo e registra movimento de saida |

## Strategy dentro de Strategy

O processo `CALCULAR_FRETE` usa duas ideias de Strategy:

1. Cada etapa do pipeline implementa `ProcessStrategy`.
2. A etapa `AplicarFreteStrategy` escolhe uma implementacao de `FreteStrategy` (`NORMAL` ou `EXPRESSO`).

Assim o aluno consegue enxergar que o padrao Strategy pode ser empregado em granularidades diferentes.

## Estorno de pagamento

O cancelamento de um pedido ja pago nao usa o pipeline simples `CANCELAR_PEDIDO`. Ele passa por um processo proprio para explicitar todas as tarefas necessarias:

| Processo | Ordem | Strategy | Responsabilidade |
|---|---:|---|---|
| ESTORNAR_PAGAMENTO | 10 | ValidarEstornoPagamentoStrategy | Exige pagamento APROVADO e pedido APROVADO |
| ESTORNAR_PAGAMENTO | 20 | EstornarPagamentoStrategy | Altera o pagamento para ESTORNADO |
| ESTORNAR_PAGAMENTO | 30 | DevolverEstoqueEstornoStrategy | Devolve os itens ao estoque e registra movimentacoes |
| ESTORNAR_PAGAMENTO | 40 | CancelarPedidoAposEstornoStrategy | Altera o pedido para CANCELADO |

As quatro tarefas sao executadas pela mesma chamada de `PipelineExecutor`, portanto participam da mesma transacao Spring. Se uma etapa lancar uma excecao, as alteracoes de banco anteriores devem sofrer rollback.
