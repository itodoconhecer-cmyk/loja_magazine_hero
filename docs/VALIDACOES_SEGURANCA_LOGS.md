# Segurança, validação, exceções e observabilidade

## Fluxo de entrada e saída

```text
HTTP Request
   |
   v
1. Autenticação JWT (Resource Server)
   |
   v
2. Autorização por permissão (@PreAuthorize)
   |
   v
3. Validação sintática/estrutural do Request
   |
   v
4. Validação inicial do fluxo
   |
   v
5. UseCase / Pipeline + Strategy
   |
   v
6. Validação do contrato de Response
   |
   v
HTTP Response
```

## Autenticação JWT

A API funciona como OAuth2 Resource Server.

Em produção devem ser configurados:

```properties
APP_JWT_ISSUER_URI=https://idp.exemplo.com
APP_JWT_JWK_SET_URI=https://idp.exemplo.com/.well-known/jwks.json
APP_JWT_AUDIENCE=loja-magazine-hero
```

Não existe segredo JWT padrão embutido na aplicação.

## Permissões

As permissões são lidas do claim `permissions` do JWT e validadas com
`@PreAuthorize`.

Exemplo:

```json
{
  "permissions": [
    "PRODUTO_LER",
    "PEDIDO_LER",
    "PEDIDO_APROVAR"
  ]
}
```

## Validação de Request

Os DTOs usam Jakarta Bean Validation.

Além disso, o handler trata:

- JSON malformado ou enum invalido;
- parametro de URL com tipo invalido;
- parametro obrigatorio ausente;
- violacoes de Bean Validation.

## Validação inicial do Pipeline

`FlowInitialValidator` executa antes da primeira Strategy e verifica a estrutura
minima do contexto.

A validacao de contexto nao substitui regras de dominio. Regras como estado do
pedido, saldo ou possibilidade de estorno continuam nas Strategies.

## Exceções de caso de uso

As excecoes carregam:

- caso de uso;
- categoria;
- codigo estavel;
- mensagem.

Categorias:

- `NOT_FOUND` -> HTTP 404;
- `CONFLICT` -> HTTP 409;
- `BUSINESS_RULE` -> HTTP 422.

A camada de aplicacao nao depende de `HttpStatus`; a traducao HTTP e feita pelo
`ApiExceptionHandler`.

## Validação de Response

Os DTOs de response possuem constraints e sao validados antes da serializacao.

A validacao esta restrita aos controllers da API para nao interferir com
respostas internas do Spring Security.

## PipelineResponse

O contexto interno do Pipeline nao e devolvido integralmente ao cliente.

`publicSnapshot()` expoe apenas valores simples e seguros. Objetos de dominio
internos ficam restritos ao processamento.

## Logs e correlation id

Arquivos separados:

```text
logs/
  operations/
  security/
  errors/
```

A rotacao e diaria por padrao e configuravel.

`X-Correlation-Id` e propagado quando valido. Valores fora do formato aceito
sao substituidos por UUID gerado pela aplicacao, evitando injecao de conteudo
nos logs.

Nunca devem ser gravados token JWT, senha, Authorization header ou dados
sensíveis no log operacional.
