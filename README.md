# Loja Magazine Hero - Pipeline + Strategy

Backend didatico de e-commerce em Java 25 + Spring Boot 4.1.2 + PostgreSQL. O projeto segue a linguagem de pacotes do repositório de aulas (`DTO`, `UseCase`, `controller`, `mapper`, `model`, `repository`) e adiciona `pipeline` e `strategy`.

## Objetivo didatico

Separar CRUD de processo de negocio. CRUD mantem entidades. Processos executam uma sequencia ordenada de `ProcessStrategy`. O `PipelineExecutor` descobre os beans Spring, filtra pelo processo, ordena por `ordem()` e executa dentro de uma transacao.

## Processos implementados

- Aprovacao de Pagamento
- Esvaziar Carrinho
- Cancelar Pedido
- Aprovar Pedido
- Excluir Pedido
- Calcular Frete
- Editar Pedido
- Movimentacao de Estoque Entrada
- Movimentacao de Estoque Saida

## Exemplo do pipeline

`CALCULAR_FRETE`: validar pedido -> gerar snapshot dos itens/calcular subtotal -> selecionar `FreteStrategy` -> persistir total.

`APROVAR_PEDIDO`: validar snapshot -> reservar estoque de forma atomica -> registrar movimentacoes -> alterar status para AGUARDANDO_PAGAMENTO.

`APROVACAO_PAGAMENTO`: validar pagamento -> aprovar pagamento -> aprovar pedido.

## Executar

1. Instale JDK 25.
2. `docker compose up -d`
3. Execute `mvn spring-boot:run` ou rode `LojaMagazineHeroApplication` diretamente pelo IntelliJ.
4. Use `requests.http` no IntelliJ.

Banco padrao: `jdbc:postgresql://localhost:5432/loja_magazine_hero`, usuario/senha `postgres`. Pode alterar com `DB_URL`, `DB_USER`, `DB_PASSWORD`.

## Estrutura

```text
aulas.umc.oo
  DTO
  UseCase
  controller
  mapper
  model
  repository
  pipeline
  strategy
    frete
    processo
```

## Pipeline + Strategy

```java
public interface ProcessStrategy {
    Processo processo();
    int ordem();
    String nome();
    void executar(ProcessContext context);
}
```

Para adicionar uma tarefa a um processo, crie outro `@Component` que implemente `ProcessStrategy`, indique o mesmo `Processo` e escolha a ordem. O executor nao precisa ser alterado.

## Observacao didatica

O projeto usa JDBC explicito, em vez de JPA, para deixar o fluxo controller -> UseCase -> repository -> SQL visivel ao aluno, como no projeto de referencia.

## Revisao de robustez

A versao revisada inclui protecoes de concorrencia de estoque, snapshot de itens do pedido, auditoria de movimentacoes, bloqueio de alteracoes indevidas em pagamento/carrinho e validacao fail-fast das strategies. Veja `docs/CORRECOES_GAPS.md` e `docs/RESULTADO_POS_CORRECAO.txt`.

## Evolucao: estorno de pagamento

A versao atual possui tambem o processo `ESTORNAR_PAGAMENTO`. Ele demonstra um pipeline com quatro strategies ordenadas: validacao, estorno, devolucao de estoque e cancelamento do pedido. O cancelamento direto continua recusando pedidos ja pagos.

Endpoint: `POST /api/pagamentos/{id}/processos/estornar`.

Consulte `docs/EVOLUCAO_ESTORNO.md` e `docs/RESULTADO_POS_ESTORNO.txt`.
