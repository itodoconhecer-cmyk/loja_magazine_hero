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

## Cofre de configuracao do Render (Environment / Environment Groups)

**Decisao arquitetural:** o Render sera a fonte de verdade das credenciais e
variaveis de ambiente do backend em producao. Nao armazenar segredos em GitHub,
`application.properties`, `application-neon.properties`, Dockerfile, `render.yaml`,
logs, frontend ou documentacao. O Render fornece variaveis de ambiente, grupos
compartilhados e arquivos secretos; nao presumir um "Vault" independente com
esse nome.

### Como cadastrar variaveis no Dashboard

1. Acesse https://dashboard.render.com/web/srv-dbueat8ae00c73e9hjeg
   (servico `loja-magazine-hero-api`), abra **Environment**.
2. Em **Environment Variables**, selecione **Add Environment Variable**.
3. Configure as chaves abaixo sem divulgar os valores confidenciais:

   | Chave | Valor / origem |
   | --- | --- |
   | `SPRING_PROFILES_ACTIVE` | `neon` |
   | `DB_URL` | URL JDBC direta do Neon, no formato `jdbc:postgresql://HOST:5432/neondb?sslmode=require` |
   | `DB_USER` | Role selecionada no Neon, conforme o painel Connect |
   | `DB_PASSWORD` | Senha atual dessa role, somente no Render |

4. Verifique as outras variaveis do servico antes de salvar: JWT,
   observabilidade/New Relic, CORS e demais configuracoes devem ser preservadas.
5. Escolha a acao de salvamento:
   - **Save only**: grava sem implantar; entra em vigor no proximo deploy.
   - **Save and deploy**: implanta novamente o build existente.
   - **Save, rebuild, and deploy**: recompila o codigo e implanta; use esta
     opcao para incluir o perfil `neon` presente no GitHub.
6. Depois, valide deploy, logs e consultas reais ao banco. O status `live`
   sozinho nao comprova que a conexao JDBC foi testada.

### Como o Spring Boot consome esses valores

O Render disponibiliza os valores ao processo Java como variaveis de ambiente.
O Spring Boot resolve automaticamente os placeholders:
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```
O perfil `neon` e ativado por `SPRING_PROFILES_ACTIVE=neon` e impede que
`schema.sql` ou `data.sql` sejam reexecutados no banco remoto.

### Quando usar Environment Groups

Se varios servicos **do mesmo ambiente e com a mesma necessidade de acesso**
precisarem dessas configuracoes, crie um **Environment Group** pelo Dashboard
e vincule-o aos servicos em **Environment > Linked Environment Groups**.
Para um unico backend, variaveis diretamente no servico sao suficientes.
Uma chave definida diretamente no servico tem prioridade sobre a mesma chave
de um grupo. Evite chaves duplicadas entre grupos, pois a precedencia entre
eles nao e garantida. Nunca exponha credenciais do banco ao frontend Vite,
incluindo variaveis prefixadas com `VITE_`.

### Quando usar Secret Files

O Render tambem aceita **Secret Files**, disponiveis em
`/etc/secrets/<nome-do-arquivo>` no runtime (inclusive Docker, conforme
as regras documentadas). Eles sao adequados para arquivos de certificados
ou chaves que o aplicativo le por caminho. Para JDBC com `DB_PASSWORD`,
prefira a variavel de ambiente: nao ha necessidade de criar um arquivo secreto.

### Rotacao e verificacao de seguranca

- Troque uma senha comprometida no Neon e atualize `DB_PASSWORD` no Render
  no mesmo procedimento coordenado; faca deploy e teste a conexao.
- Restrinja acesso ao painel Render e ao projeto Neon.
- Nunca copie valores secretos para tickets, prints, ferramentas de chat
  ou saidas de CI/CD; registre apenas os **nomes** das chaves.
- Ao usar Blueprints/IaC no futuro, mantenha valores sensiveis fora do
  `render.yaml` e estabeleca qual mecanismo e a fonte de verdade.
- Nao substitua todas as variaveis via API sem inspecionar se a operacao
  atualiza uma chave ou troca a colecao completa.

Referencia oficial (consultada em 18/11/2026):
https://render.com/docs/configure-environment-variables
