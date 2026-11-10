# Correcoes apos revisao geral

Esta versao corrige os gaps encontrados na primeira bateria de testes simulados sem alterar a proposta didatica Pipeline + Strategy.

## Correcoes aplicadas

1. O pedido passa a receber um snapshot de `item_pedido` durante `CALCULAR_FRETE`.
2. `APROVAR_PEDIDO` reserva estoque a partir do snapshot, e nao do carrinho vivo.
3. Carrinho convertido em pedido nao pode mais ser alterado, excluido ou receber itens.
4. Pedido valida se o carrinho pertence ao mesmo cliente.
5. Um carrinho pode originar apenas um pedido ativo, com protecao na aplicacao e constraint no banco.
6. Frete so pode ser calculado em pedido `RASCUNHO`.
7. Alterar o tipo de frete invalida subtotal, frete, total e snapshot, obrigando novo calculo.
8. Aprovacao exige snapshot nao vazio.
9. Reserva e saida de estoque usam UPDATE atomico com `estoque >= quantidade`.
10. Reserva e cancelamento geram `MovimentacaoEstoque`.
11. Cancelamento de pedido `APROVADO` foi bloqueado; pedido pago exige um futuro processo explicito de estorno.
12. Cancelamento devolve estoque mesmo que o produto tenha sido desativado depois da venda.
13. Pagamento so pode ser aprovado se estiver `PENDENTE`.
14. Pagamento aprovado/cancelado nao pode ser editado ou excluido pelo CRUD.
15. Criacao de pagamento exige pedido `AGUARDANDO_PAGAMENTO`, valor exato e um unico pagamento por pedido.
16. PUT de produto nao altera saldo de estoque; saldo muda apenas por processos de estoque.
17. Estoque inicial de produto gera movimentacao de entrada auditavel.
18. Banco ganhou CHECKs para valores/quantidades nao negativos e quantidades positivas.
19. `PipelineExecutor` falha na inicializacao se duas strategies tiverem o mesmo `(processo, ordem)`.
20. `FreteStrategyFactory` falha na inicializacao se houver strategy duplicada ou tipo de frete sem implementacao.
21. Erros de integridade/repository sao traduzidos para resposta HTTP controlada.

## Resultado pos-correcao

- 24/24 cenarios simulados passaram.
- 9 processos declarados possuem strategies.
- 22 strategies de processo encontradas.
- Nenhuma ordem duplicada.
- NORMAL e EXPRESSO possuem uma unica `FreteStrategy` cada.
- Todo o `src/main/java` compilou sem erros de sintaxe/tipos na verificacao estatica com stubs das APIs Spring/Jakarta.

Os testes simulados podem ser repetidos com:

```bash
python3 tools/post_fix_gap_tests.py
python3 tools/structural_audit.py
```
