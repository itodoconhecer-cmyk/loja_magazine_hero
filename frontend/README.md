# Magazine Hero — React

Frontend React 19 + TypeScript + Material UI, estruturado por Atomic Design e integrado aos contratos REST existentes do backend Java.

## Executar

1. Na raiz do repositório: `docker compose up -d` (PostgreSQL).
2. Inicie o backend: `mvn spring-boot:run` (Java 25).
3. Execute `cd frontend && npm install && npm run dev`.
4. Acesse http://localhost:5173.

O Vite encaminha /api para http://localhost:8080. Para outro endereço, defina API_PROXY_TARGET no terminal. Um JWT válido de provedor OAuth2/OIDC é obrigatório em todos os endpoints existentes. O botão "Configurar JWT" aceita um token de desenvolvimento em memória; **não é um fluxo de login de produção**. A autenticação OIDC com PKCE ainda precisa ser implementada.

## Segurança

Nunca grave tokens em arquivos, localStorage ou repositório. Use HTTPS em produção e configure corretamente issuer, audience e JWKS no backend. Os botões da interface não substituem a autorização Spring Security.

## Estado

Esta entrega adiciona a interface e cliente de API. A compilação, autenticação real e testes ponta a ponta ainda não foram validados neste ambiente. O banco de dados só é acessado pelo backend.
