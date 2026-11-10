# Revisao apos implementacao do estorno

## Resultado

- Processos declarados: 10
- ProcessStrategy: 26
- Processos sem Strategy: 0
- Ordens duplicadas: 0
- Tipos de frete sem Strategy: 0
- FreteStrategy duplicadas: 0
- Cenarios simulados: 31
- Cenarios aprovados: 31

## Novo fluxo

`ESTORNAR_PAGAMENTO`:

1. ordem 10 - validar pagamento e pedido;
2. ordem 20 - marcar pagamento como ESTORNADO;
3. ordem 30 - devolver estoque e registrar movimentacoes;
4. ordem 40 - cancelar pedido.

O `PipelineExecutor` permanece generico. Nenhum `if` especifico para estorno foi incluido nele.

## Transacao

O metodo `PipelineExecutor.executar` continua anotado com `@Transactional`. Assim, as quatro tarefas participam da mesma transacao de banco. Se uma etapa falhar por excecao de runtime, as alteracoes JDBC anteriores devem ser revertidas pelo Spring.

## Limite didatico assumido

O estorno e local. Nao ha gateway de pagamento real. Em producao, a etapa `EstornarPagamentoStrategy` deveria conversar com um provedor externo e tratar idempotencia externa, timeout, retries controlados, webhook e reconciliacao.

## Validacoes realizadas

O script `tools/post_fix_gap_tests.py` passou em 31/31 cenarios. A auditoria `tools/structural_audit.py` confirmou os 10 processos e 26 strategies. As quatro novas classes do estorno tambem foram compiladas isoladamente com Java 21 usando apenas um stub da anotacao `@Component`, validando sintaxe, tipos do dominio e contratos internos.
