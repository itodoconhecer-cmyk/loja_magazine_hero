# Endpoints principais

## CRUD

- `POST/GET /api/clientes`
- `GET/PUT/DELETE /api/clientes/{id}`
- `POST/GET /api/produtos`
- `GET/PUT/DELETE /api/produtos/{id}`
- `POST/GET /api/carrinhos`
- `GET/PUT/DELETE /api/carrinhos/{id}`
- `POST /api/carrinhos/{id}/itens`
- `POST/GET /api/pedidos`
- `GET /api/pedidos/{id}`
- `POST/GET /api/pagamentos`
- `GET/PUT/DELETE /api/pagamentos/{id}`
- `GET /api/estoque/movimentacoes`

## Processos Pipeline + Strategy

- `POST /api/pedidos/{id}/processos/calcular-frete`
- `POST /api/pedidos/{id}/processos/aprovar`
- `POST /api/pedidos/{id}/processos/cancelar`
- `POST /api/pedidos/{id}/processos/excluir`
- `PUT /api/pedidos/{id}/processos/editar`
- `POST /api/pagamentos/{id}/processos/aprovar`
- `POST /api/carrinhos/{id}/processos/esvaziar`
- `POST /api/estoque/entrada`
- `POST /api/estoque/saida`

A edicao e a exclusao de pedido foram mantidas como processos, e nao como operacoes diretas, justamente para que as regras sejam executadas pelo pipeline.

## Estorno de pagamento

`POST /api/pagamentos/{pagamentoId}/processos/estornar`

Pre-condicoes:
- pagamento deve estar `APROVADO`;
- pedido relacionado deve estar `APROVADO`.

Efeitos, na mesma transacao:
1. pagamento -> `ESTORNADO`;
2. itens do pedido retornam ao estoque;
3. cada devolucao gera `movimentacao_estoque` do tipo `ENTRADA`;
4. pedido -> `CANCELADO`.
