# Testes simulados de negocio

Os testes abaixo sao testes de mesa executaveis pelo script `tools/review_simulator.py`. Eles reproduzem as regras centrais encontradas nas Strategies atuais para procurar transicoes perigosas. Nao substituem testes Spring/JDBC reais.

| # | Cenario | Resultado atual | Severidade |
|---:|---|---|---|
| 01 | Fluxo feliz pedido -> frete -> aprovacao -> pagamento | PASSOU | baseline |
| 02 | Alterar carrinho depois do calculo do frete | GAP | Critico |
| 03 | Esvaziar carrinho depois do frete e antes de aprovar | GAP | Critico |
| 04 | Recalcular frete em AGUARDANDO_PAGAMENTO | GAP | Critico |
| 05 | Recalcular frete em APROVADO | GAP | Critico |
| 06 | Cancelar pedido com pagamento aprovado | GAP | Critico |
| 07 | Cancelar pedido cujo produto foi desativado | GAP | Alto |
| 08 | Aprovar pagamento previamente CANCELADO | GAP | Alto |
| 09 | Editar pagamento APROVADO pelo CRUD | GAP | Critico |
| 10 | Excluir pagamento APROVADO pelo CRUD | GAP | Critico |
| 11 | Alterar saldo pelo PUT de produto sem movimentacao | GAP | Alto |
| 12 | Duas reservas/saidas concorrentes sobre o mesmo saldo | GAP | Critico |
| 13 | Criar pedido usando carrinho de outro cliente | GAP | Alto |
| 14 | Criar varios pedidos com o mesmo carrinho | GAP | Medio |
| 15 | Esvaziar carrinho vinculado a pedido | GAP | Alto |
| 16 | Inserir duas ProcessStrategy com a mesma ordem | GAP de protecao | Medio |
| 17 | Inserir duas FreteStrategy para o mesmo TipoFrete | GAP de protecao | Medio |
| 18 | Persistir estoque/quantidade negativa diretamente no banco | GAP de protecao | Alto |
| 19 | Reserva/devolucao sem registro de movimentacao | GAP | Alto |
| 20 | Violacao de integridade no banco vira erro HTTP controlado | GAP | Medio |

## Como executar

```bash
python3 tools/review_simulator.py
python3 tools/structural_audit.py
```

Os resultados gravados nesta revisao estao em:

- `docs/RESULTADO_SIMULACAO.txt`
- `docs/RESULTADO_AUDITORIA_ESTRUTURAL.txt`

## Auditoria estrutural

A auditoria estrutural encontrou:

- 9 processos declarados;
- 22 ProcessStrategy;
- nenhum processo sem strategy;
- nenhuma ordem duplicada **na versao atual**;
- 2 tipos de frete;
- uma implementacao para NORMAL e uma para EXPRESSO;
- nenhuma duplicidade de frete **na versao atual**.

Isso mostra que a composicao atual esta completa; os principais gaps sao de regra de negocio e protecao contra futuras configuracoes invalidas.
