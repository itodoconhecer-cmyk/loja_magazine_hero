# Estoque por movimentacoes - modelo profissional

## Decisao arquitetural

`Produto` representa catalogo: identidade, nome, preco e estado ativo/inativo.

**Produto nao armazena quantidade em estoque.**

A fonte de verdade do estoque e a tabela `movimentacao_estoque`, que funciona como um livro-razao imutavel:

```text
saldo = soma(ENTRADA) - soma(SAIDA)
```

Isso elimina duas fontes de verdade e preserva todo o historico de alteracoes.

## Modelo

```text
Produto
  id
  nome
  preco
  ativo

MovimentacaoEstoque
  id
  produto_id -> Produto
  tipo       -> ENTRADA | SAIDA
  quantidade -> sempre positiva
  motivo
  data_hora
```

Uma devolucao ou estorno nao "edita" uma saida antiga. Ele registra uma nova **ENTRADA**. Uma venda nao altera o produto; registra uma nova **SAIDA**.

## Exemplos

Entrada inicial:

```text
ENTRADA  +10  ESTOQUE_INICIAL
```

Venda:

```text
SAIDA     -2  VENDA_PEDIDO:<pedidoId>
```

Cancelamento:

```text
ENTRADA   +2  CANCELAMENTO_PEDIDO:<pedidoId>
```

Saldo final: 10 - 2 + 2 = 10.

## Concorrencia

Apenas calcular o saldo e depois inserir uma saida nao e suficiente em cenarios concorrentes.

Antes de validar uma SAIDA, o sistema executa:

```sql
select id
from produto
where id = ?
for update
```

O lock permanece durante a transacao do `PipelineExecutor`. Em seguida o saldo e recalculado a partir das movimentacoes e a SAIDA so e registrada quando existe saldo suficiente.

No pipeline de aprovacao de pedido, os produtos sao bloqueados em ordem deterministica de UUID para reduzir risco de deadlock.

## APIs

Consulta do saldo calculado:

```http
GET /api/estoque/produtos/{produtoId}/saldo
```

Historico completo:

```http
GET /api/estoque/movimentacoes
```

Historico por produto:

```http
GET /api/estoque/movimentacoes?produtoId={produtoId}
```

Entrada:

```http
POST /api/estoque/entrada
```

Saida:

```http
POST /api/estoque/saida
```

## Regra fundamental

Nenhuma classe, DTO, repository ou tabela de Produto deve possuir ou atualizar um campo `estoque`.

Toda mudanca de saldo precisa ser uma nova movimentacao auditavel.
