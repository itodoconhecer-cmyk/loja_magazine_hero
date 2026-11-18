# Backend Spring Boot -> Neon PostgreSQL

O backend usa JDBC (nao JPA). O perfil `neon` desativa a execucao automatica
de `schema.sql` e `data.sql` para preservar as tabelas existentes.

## Configurar no Render -> Web Service -> Environment

- `SPRING_PROFILES_ACTIVE=neon`
- `DB_URL=jdbc:postgresql://<HOST-DIRETO-DO-NEON>:5432/neondb?sslmode=require`
- `DB_USER=<USUARIO-DO-NEON>`
- `DB_PASSWORD=<SENHA-DO-NEON>`

Use o endereco **Direct connection** do painel Neon (Connect, desative
Connection pooling), e a senha atual do papel selecionado. A string
`postgresql://...` exibida pelo Neon **nao** e diretamente uma URL JDBC:
adicione o prefixo `jdbc:` e mantenha o host e banco corretos.
A senha deve ir apenas em `DB_PASSWORD`, nao em `DB_URL`.

## Verificacao

1. Salve as variaveis no Render e realize o deploy do ultimo commit.
2. Consulte os logs do Render por erros de autenticacao, TLS ou conexao.
3. Confirme que a aplicacao iniciou e verifique um endpoint real de leitura
   (por exemplo, o endpoint de produtos documentado em `docs/ENDPOINTS.md`).
4. No pgAdmin, confira se os registros esperados aparecem no mesmo banco.

Nao registre segredos em issues, screenshots, logs, commits ou no frontend.
Se a senha do Neon for alterada, atualize tambem `DB_PASSWORD` no Render.
O perfil `neon` nao cria tabelas novas: mudancas de esquema devem passar
por migrations versionadas, apos backup e revisao.
