# ADR-006 — Evolução de autenticação OIDC/PKCE (SEMPRE FREE)

Status: **proposta; implementação não iniciada**. Data: 2026-11-17. Base auditada: `main` em `b0881234873c2516641c5ee63936db3969c6a855`.

## Contexto verificado
- Frontend React 19, TypeScript, Vite, Material UI, React Query e Atomic Design.
- `frontend/src/app/App.tsx` tem campo manual de JWT para demonstração; `frontend/src/services/api.ts` guarda o token apenas em memória.
- `frontend/vercel.json` encaminha `/api/:path*` para a API no Render.
- API já foi preparada como OAuth2 Resource Server e exige permissões por endpoint; conferir em testes antes de liberar o login.
- Vercel registrou deploy de produção READY no commit acima; GitHub Actions CI principal concluiu success. Isso **não** equivale a teste HTTP de ponta a ponta.
- Neon: PostgreSQL 18, branch `production`, oito tabelas; `produto` não possui coluna saldo.

## Decisão proposta
Preferir **ZITADEL Cloud Free**, sujeito a validação de suas condições no momento de cadastramento e do formato efetivo do access token. A página oficial de preços consultada em 2026-11-17 apresenta US$0/mês e 100 usuários ativos por dia. O provedor suporta Authorization Code + PKCE. Não provisionar instância ou aderir a upgrade automaticamente.

Alternativa: Auth0 Free; a tabela oficial lista até 25.000 usuários ativos mensais, porém **Role Management não está incluído no Free**. Isso exige manter a autorização detalhada no backend e pode dificultar o exercício pedagógico. Reavaliar se o escopo da turma exceder 100 DAU.

Referências:
- https://zitadel.com/pricing
- https://zitadel.com/docs/apis/openidoauth/grant-types
- https://auth0.com/pricing

## Contrato de integração proposto
1. Registrar aplicação SPA **public client**, Authorization Code Flow, PKCE S256, redirecionamentos HTTPS estritos; sem client secret no navegador.
2. Usar biblioteca OIDC consolidada (ex.: `oidc-client-ts` ou wrapper compatível), exigindo `state`, `nonce`, `issuer`, descoberta OIDC e validação padrão dos tokens.
3. Enviar **access token** como Bearer somente à API; nunca usar ID token como autorização da API.
4. Configurar Spring Security Resource Server com `issuer-uri`, JWKS e verificação explícita da `aud` esperada. Confirmar a audiência real no token emitido antes de codificar a regra.
5. Traduzir claims/roles/scopes efetivamente presentes no access token para `GrantedAuthority` por whitelist; não confiar nas permissões informadas pela interface.
6. Bloquear chamadas sensíveis sem permissão. UI pode ocultar botões, mas o backend continua sendo autoridade final.
7. Reduzir persistência de tokens ao mínimo: preferir memória, sem localStorage, logs ou URLs com tokens. Avaliar UX no refresh e controles de renovação conforme comportamento do provedor.
8. Implementar login, callback, expiração, logout federado (quando suportado), limpeza de cache React Query e tratamento separado de HTTP 401 e 403.
9. Não abrir endpoints privados nem remover a segurança para liberar catálogo. Manter `GET /health` público e mínimo.

## Estratégia incremental
- **PR A (este documento):** decisões, riscos e plano de verificação; sem mudança de runtime.
- **PR B:** testes backend com access tokens simulados, JWT issuer/audience/JWKS e mapeamento de permissões; testes 401/403.
- **PR C:** React OIDC/PKCE, rotas de callback, login/logout e substituição do token manual após teste integrado.
- **PR D:** smoke test de produção, tratamento de falhas e atualização do livro.
- Migrações Flyway/Liquibase em PR separado, após baseline do schema atual e estratégia de recuperação. Não executar DDL sobre `production` sem revisão.

## Critérios de aceite
- `GET /health` responde HTTP 200 com payload mínimo.
- `GET /api/produtos` sem Bearer responde HTTP 401, tanto diretamente quanto via rewrite Vercel.
- Token válido, mas sem `PRODUTO_LER`, resulta em 403; com permissão, segue para a camada de domínio.
- Tokens com `iss`, `aud`, `exp` ou assinatura incorretos são rejeitados.
- Fluxo de login exige PKCE S256, `state`, `nonce` e callback registrado; logout apaga token em memória e caches.
- CI dos PRs aprovada; deploy realmente publicado e verificado antes de registrar conclusão.

## Evidências e pendências
- Em 2026-11-17, as tentativas de teste HTTP por ferramentas externas falharam por acesso/rede. **Não** marcar saúde, proxy ou 401/403 como validados.
- Conferir workspace Render selecionado pelo proprietário antes de inspeções administrativas.
- A sessão compartilhada complementar do frontend não pôde ser recuperada; solicitar exportação se decisões ausentes do repositório forem necessárias.
- Sem alteração de variáveis de produção, dados, credenciais ou recursos cobrados neste PR.

## Livro técnico
Converter este ADR em capítulo universitário: fundamentos OAuth2/OIDC e PKCE, autenticação versus autorização, ameaça de roubo do code, diagramas Draw.io editáveis, configuração gratuita, testes e troubleshooting, citando evidências de CI/deploy verificadas.
