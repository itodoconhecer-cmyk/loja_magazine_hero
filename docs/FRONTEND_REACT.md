# Magazine Hero — Integração React / Java / PostgreSQL

## Escopo desta entrega

Frontend React 19 + TypeScript + Material UI com organização Atomic Design, catálogo, cadastro, carrinhos, pedidos, pagamentos e dashboard. O frontend acessa somente a API, nunca o PostgreSQL diretamente.

## Contratos conferidos

Os DTOs e controllers do backend atual definem os recursos em `/api/produtos`, `/api/clientes`, `/api/carrinhos`, `/api/pedidos` e `/api/pagamentos`. As ações de frete, aprovação, cancelamento e estorno invocam pipelines existentes. ProdutoResponse não oferece imagem, descrição ou categoria, portanto o catálogo utiliza um placeholder visual, sem inventar mídia de produto.

## Autenticação

Todos os endpoints Java exigem JWT com permissões específicas. Esta versão permite colar temporariamente um token válido em memória para integração/teste local. Não configura login OIDC/PKCE de produção. Nunca salvar token no repositório, no localStorage ou em logs.

## Executar

Na raiz, `docker compose up -d` para PostgreSQL e `mvn spring-boot:run` para Java 25. Em `frontend/`, `npm install && npm run dev`. O proxy Vite aponta `/api` para `http://localhost:8080`, alterável via `API_PROXY_TARGET`.

## Limitações da entrega

- Não foi possível instalar npm no ambiente de autoria; portanto build e testes de browser ainda não estão certificados.
- O React não possui login OIDC/PKCE ou gateway de pagamento real.
- A visualização do catálogo depende de JWT com PRODUTO_LER; não foi aberta API pública para visitantes anônimos.
- Banco e backend existentes permanecem inalterados. Evolução por migrations será uma etapa independente.

## Próximos critérios de aceitação

CI verde, login com PKCE, controle de permissões na interface, E2E com Postgres real, tratamento de dinheiro sem conversão de precisão insegura, feedback e bloqueio de ações por status dos pedidos.
