# Monitoramento no plano Free

- A rota `GET /health` responde `{"status":"UP"}` sem JWT, sem retornar dados privados.
- Todas as demais rotas continuam exigindo autenticacao e autorizacao.
- A resposta de `/health` indica que o processo web respondeu; **nao** garante disponibilidade do Neon ou que o login OIDC esteja configurado.
- Opcional: configure o caminho `/health` em Render > Web Service > Settings > Health Check Path.
- O backend Render Free pode suspender por inatividade; aguarde o primeiro acesso.
- Nao configure banco, cron, monitoramento pago, logs persistentes ou extensoes pagas.
- Na Vercel, verifique Deployment Protection. Restricoes de autenticacao do proprio Vercel podem bloquear visitantes anonimos.
- Em producao, o issuer/JWKS precisa ser de um provedor real de identidade. O valor localhost e somente exemplo, nao autentica usuarios externos.
- Continuar usando GitHub Actions para verificacao do Java 25, testes, empacotamento e Docker.
