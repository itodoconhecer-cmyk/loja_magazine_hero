# Validação de entrada/saída, segurança e logs

## Ordem do fluxo

```text
HTTP Request
   |
   v
1. Autorização JWT
   |
   v
2. Permissão do endpoint
   |
   v
3. Bean Validation do Request
   |
   v
4. Validação inicial do fluxo
   |
   v
5. UseCase / Pipeline + Strategy
   |
   v
6. Validação do Response
   |
   v
HTTP Response
```

## 1. Autorização

Todos os endpoints exigem JWT Bearer válido.

A aplicação usa Spring Security Resource Server com HS256 para fins didáticos. O segredo deve ser fornecido por `APP_JWT_SECRET` fora do ambiente local.

## 2. Permissão

Cada endpoint declara a permissão necessária com `@PreAuthorize`.

Exemplos:

- `PRODUTO_LER`
- `PRODUTO_ESCREVER`
- `PEDIDO_APROVAR`
- `PAGAMENTO_ESTORNAR`
- `ESTOQUE_MOVIMENTAR`

O JWT deve possuir o claim:

```json
{
  "permissions": [
    "PRODUTO_LER",
    "PEDIDO_LER"
  ]
}
```

## 3. Validação dos campos

Requests usam Jakarta Bean Validation com mensagens explícitas:

- `@NotNull`
- `@NotBlank`
- `@Size`
- `@Email`
- `@Positive`
- `@PositiveOrZero`
- `@Digits`

## 4. Validação inicial do fluxo

Antes da primeira Strategy, `FlowInitialValidator` valida:

- processo informado;
- contexto presente;
- chaves obrigatórias;
- tipos esperados;
- quantidade positiva;
- motivo obrigatório.

Isso evita iniciar transação de negócio com contexto estruturalmente inválido.

## 5. Exceções personalizadas

Existe uma hierarquia por caso de uso:

- `ClienteUseCaseException`
- `ProdutoUseCaseException`
- `CarrinhoUseCaseException`
- `PedidoUseCaseException`
- `PagamentoUseCaseException`
- `EstoqueUseCaseException`
- `ProcessoUseCaseException`

A resposta de erro usa contrato padronizado com:

- timestamp;
- HTTP status;
- código;
- mensagem;
- path;
- correlationId;
- campos inválidos.

## 6. Validação de Response

`ResponseValidationAdvice` valida DTOs de saída com Bean Validation antes da serialização.

Se a aplicação produzir um response inválido, o cliente recebe HTTP 500 com código `RESP-001`, pois se trata de falha interna do contrato.

## 7. Logs por período

O Logback grava arquivos separados:

```text
logs/
  operations/
  security/
  errors/
```

Por padrão, a rotação é diária:

```properties
app.logging.period-pattern=yyyy-MM-dd
```

Configurações:

- `APP_LOG_DIR`
- `APP_LOG_PERIOD_PATTERN`
- `APP_LOG_MAX_HISTORY`
- `APP_LOG_MAX_FILE_SIZE`

Cada requisição recebe `X-Correlation-Id`, criado automaticamente quando o cliente não envia um.
