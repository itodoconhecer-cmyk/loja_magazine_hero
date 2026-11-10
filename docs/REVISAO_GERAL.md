# Revisao geral - Loja Magazine Hero

## Resumo executivo

A arquitetura didatica esta coerente para ensinar Pipeline + Strategy: os 9 processos declarados possuem strategies, ha 22 tarefas de processo, nao existem ordens duplicadas atualmente e os dois tipos de frete possuem implementacao. O fluxo feliz pedido -> frete -> aprovacao -> pagamento funciona na simulacao de regras.

O principal risco nao esta no padrao Pipeline/Strategy em si, mas nas **invariantes de negocio entre um processo e outro**. O sistema permite que o carrinho mude depois do calculo, que o frete seja recalculado depois de reservar/pagar, que um pedido pago seja cancelado sem tratar o pagamento e que o estoque seja alterado fora do pipeline de movimentacao.

Foram simulados 20 cenarios. O baseline passou e 19 cenarios evidenciaram gaps ou protecoes ausentes. Parte deles sao deliberadamente cenarios de abuso/concorrencia, importantes para uma aula de evolucao arquitetural.

## Pontos positivos

1. Separacao clara Controller -> UseCase -> PipelineExecutor -> ProcessStrategy -> Repository.
2. Pipeline generico, sem `if` por processo dentro do executor.
3. Strategies pequenas e com responsabilidade facil de explicar.
4. Ordem explicita por `ordem()`.
5. Transacao no `PipelineExecutor`, permitindo rollback quando uma strategy falha dentro do mesmo processo.
6. Strategy de frete separada das strategies de tarefa, excelente para demonstrar o mesmo pattern em granularidades diferentes.
7. Repositorios por contrato e implementacao JDBC.
8. Exclusao logica de entidades principais (com excecao de pagamento, que hoje usa delete fisico).
9. Snapshot de itens no pedido na aprovacao, boa ideia para preservar preco unitario.
10. Banco relacional simples e compreensivel para aula.

## Gaps criticos

### G01 - Carrinho pode mudar depois do calculo do frete
O subtotal/total e calculado a partir do carrinho, mas a aprovacao usa novamente o carrinho atual para criar `item_pedido`. Se o carrinho mudar entre as duas operacoes, o valor do pedido e os itens reservados podem divergir.

**Correcao recomendada:** introduzir versao/hash do carrinho ou criar o snapshot `item_pedido` no momento em que o pedido e fechado/calculado; na aprovacao, validar que a versao nao mudou.

### G02 - Pedido pode ser aprovado sem itens
Se o frete foi calculado anteriormente e depois o carrinho for esvaziado, `ReservarEstoquePedidoStrategy` aceita lista vazia e o pedido vai para `AGUARDANDO_PAGAMENTO` com total antigo.

**Correcao:** validar `itens.isEmpty()` na aprovacao/reserva.

### G03 - CALCULAR_FRETE aceita pedido AGUARDANDO_PAGAMENTO e APROVADO
`ValidarPedidoParaFreteStrategy` bloqueia apenas CANCELADO e EXCLUIDO.

**Correcao:** permitir calculo somente em `RASCUNHO` (ou criar uma politica explicita de recotacao antes da reserva).

### G04 - Cancelamento de pedido pago nao trata o pagamento
O pedido APROVADO pode ser cancelado. O estoque volta, mas o pagamento continua `APROVADO`.

**Correcao:** definir regra: pedido pago nao cancela diretamente; deve executar pipeline de estorno, ou o pipeline de cancelamento precisa conter uma strategy de cancelamento/estorno do pagamento.

### G05 - Pagamento aprovado pode ser editado e excluido pelo CRUD
`PagamentoUseCase.atualizar` nao bloqueia pagamento APROVADO. `delete` e fisico.

**Correcao:** pagamento deve ser imutavel depois de aprovado; substituir delete fisico por status/cancelamento e proteger alteracoes por estado.

### G06 - Concorrencia de estoque
O fluxo atual faz leitura do saldo e depois grava um valor absoluto (`atualizarEstoque(id, novo)`). Duas transacoes concorrentes podem ler o mesmo saldo e ambas serem aceitas.

**Correcao:** usar update atomico/condicional, por exemplo `UPDATE produto SET estoque = estoque - ? WHERE id=? AND estoque >= ?`, conferir linhas afetadas e/ou usar lock pessimista/optimista conforme objetivo didatico.

### G07 - Estoque pode ser alterado pelo CRUD de produto
`ProdutoRequest` contem `estoque` e o PUT de produto altera saldo sem registrar `MovimentacaoEstoque`.

**Correcao:** separar `ProdutoCadastroRequest` de estoque. Apos criacao, saldo so muda pelos pipelines ENTRADA/SAIDA/reserva/devolucao.

## Gaps altos

### G08 - Produto desativado pode impedir cancelamento
`ProdutoRepository.findById` filtra `ativo=true`. Ao cancelar, a devolucao procura o produto por esse metodo. Se o produto foi desativado depois da venda, o cancelamento falha.

**Correcao:** repositorio interno deve ter busca por id incluindo inativos para operacoes historicas/estoque.

### G09 - Pagamento CANCELADO/RECUSADO pode ser aprovado
A validacao confere pedido e valor, mas nao exige `StatusPagamento.PENDENTE`.

**Correcao:** validar transicao de estado do pagamento.

### G10 - Pedido pode usar carrinho de outro cliente
Na criacao, cliente e carrinho sao validados separadamente, mas nao se confere `carrinho.clienteId == clienteId`.

**Correcao:** validar a propriedade do carrinho.

### G11 - Esvaziar carrinho nao considera pedidos vinculados
O processo verifica apenas se o carrinho existe.

**Correcao:** impedir esvaziamento quando houver pedido em estado que dependa daquele carrinho ou, preferencialmente, desacoplar o pedido do carrinho depois do snapshot.

### G12 - Reserva/devolucao de estoque nao gera historico de movimentacao
A aprovacao e o cancelamento alteram `produto.estoque` diretamente.

**Correcao:** toda variacao de saldo deve gerar `MovimentacaoEstoque`, com origem (VENDA, CANCELAMENTO, AJUSTE, COMPRA etc.).

### G13 - Banco sem constraints de dominio basicas
Nao ha `CHECK (estoque >= 0)`, `CHECK (quantidade > 0)`, `CHECK (preco >= 0)` e similares.

**Correcao:** duplicar invariantes criticas na aplicacao e no banco.

## Gaps medios / robustez

### G14 - Multiplos pedidos podem reutilizar o mesmo carrinho
Nao existe regra nem constraint para impedir que o mesmo carrinho seja usado em varios pedidos.

### G15 - Pipeline nao detecta ordem duplicada
Hoje nao ha duplicidade, mas se duas strategies do mesmo processo retornarem a mesma ordem, o resultado dependera da ordem fornecida pelo container.

**Correcao:** validar unicidade `(processo, ordem)` na inicializacao.

### G16 - FreteStrategyFactory nao detecta duplicidade
`EnumMap.put` substitui silenciosamente uma strategy anterior do mesmo `TipoFrete`.

**Correcao:** falhar na inicializacao quando um tipo estiver duplicado ou ausente.

### G17 - Tratamento de erros de persistencia incompleto
`ApiExceptionHandler` cobre IllegalArgumentException/IllegalStateException e validacao, mas nao cobre `RepositoryException`/`DataIntegrityViolationException`.

### G18 - PipelineResponse expoe objetos internos do contexto
`snapshot()` pode conter `Pedido`, `Pagamento`, listas internas etc. Isso torna o contrato da API dependente de detalhes de implementacao.

**Correcao:** resposta explicita por processo ou contexto publico separado do contexto interno.

### G19 - HTTP/status codes podem ser melhorados
Criacoes retornam 200 em vez de 201, deletes nao explicitam 204, e erros de recurso inexistente sao tratados como 400 em varios casos. Para aula de API REST, vale evoluir isso depois.

## Prioridade sugerida

### Fase 1 - Consistencia de negocio
G01, G02, G03, G04, G05, G06, G07.

### Fase 2 - Estoque e auditoria
G08, G09, G10, G11, G12, G13.

### Fase 3 - Robustez da infraestrutura didatica
G14, G15, G16, G17, G18, G19.

## Conclusao

A arquitetura Pipeline + Strategy esta adequada e didaticamente forte. Os gaps encontrados sao majoritariamente **regras entre processos e invariantes de estado**, o que e positivo para a evolucao da aula: a proxima versao pode mostrar que aplicar um Design Pattern nao elimina a necessidade de modelar transacoes, estados, concorrencia, auditoria e consistencia do dominio.
