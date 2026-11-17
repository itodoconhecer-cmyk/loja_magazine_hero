# Implantacao da Loja Magazine Hero: Neon, Render, Vercel

## Arquitetura
- GitHub: codigo-fonte, CI, PRs e release gates
- Neon Free: PostgreSQL, projeto loja_magazine_hero (nao expor a senha)
- Render Free: API Spring Boot 4 com Java 25 em Docker
- Vercel Hobby: frontend React separado; CI propria e deploy por Git

## Condicoes de publicacao
1. O PR deve passar em Spotless, testes Maven e build Docker.
2. Corrigir demais falhas identificadas pela CI antes de fazer merge.
3. Configurar conexao PostgreSQL no Render via DB_URL (JDBC com sslmode=require), DB_USER, DB_PASSWORD.
4. Configurar emissor JWT real: APP_JWT_ISSUER_URI, APP_JWT_JWK_SET_URI, APP_JWT_AUDIENCE.
5. Nunca gravar credenciais ou tokens em Git. Configurar CORS com origem HTTPS do frontend.
6. Priorizar migracoes versionadas Flyway/Liquibase; schema.sql atual roda a cada inicio.
7. Usar stdout para logs no Render Free; arquivos locais nao sao persistentes.
8. Antes de habilitar CD, testar endpoint de saude e fluxos principais.
9. Configurar RENDER_DEPLOY_HOOK_URL como secret de Actions e ENABLE_RENDER_AUTODEPLOY=true como repository variable somente depois que o servico Render estiver pronto.
10. O deploy hook apenas dispara a publicacao; confirmar estado saudavel no Render.

## Sequencia
PR -> CI verde -> merge main -> (CD habilitado) -> Render -> API -> Neon.
Frontend independente: PR -> CI frontend -> Vercel (Git integration).

Este commit prepara CI/CD, nao cria banco, nao faz deploy nem indica que a API esta pronta.
