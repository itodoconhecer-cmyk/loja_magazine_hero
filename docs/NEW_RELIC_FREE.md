# New Relic Free — instrumentacao APM opcional

Status: **proposta em PR**, desativada por padrao. A instrumentacao so sera ativada em producao depois de aprovacao da CI, merge, confirmacao do plano Free e configuracao das variaveis do Render.

## Escopo e arquitetura

- Aplicacao: `loja-magazine-hero-api`, Java 25, Spring Boot 4, Render Free.
- Agente Java New Relic versao **9.5.0** obtido do Maven Central durante a construcao da imagem Docker.
- O agente e carregado **somente quando** `NEW_RELIC_ENABLED=true` **e** `NEW_RELIC_LICENSE_KEY` estiver presente. Caso contrario, a API inicia sem agente.
- Nao ha instrumentacao no navegador React nesta etapa: RUM exige estudo de consentimento, privacidade, limites e configuracao separada.
- Nenhuma migracao ou alteracao de dados no Neon.

## Configuracao no Render (sem copiar o segredo no GitHub ou no chat)

1. Verifique em New Relic > Administration / Billing que a conta permanece no **Free**, sem pagamento ou upgrade habilitado. Confirme consumo e alertas de ingestao. **Nao contratar plano pago**.
2. No New Relic, em Add more data > Application monitoring > Java, obtenha uma **license key de ingestao** da conta; nao confundir com user API key.
3. Abra Render > `loja-magazine-hero-api` > Environment e defina as variaveis **somente no painel privado**:
   - `NEW_RELIC_LICENSE_KEY`: chave de ingestao (secreta, nunca em commits, imagens, logs ou chat).
   - `NEW_RELIC_APP_NAME=loja-magazine-hero-api`
   - `NEW_RELIC_ENABLED=true`
   - `NEW_RELIC_LOG_FILE_NAME=STDOUT`
4. Garanta que o servico ainda usa Render Free e que nao ha outra ferramenta com agente APM na mesma JVM.
5. Depois de CI, review, merge e deploy, confira `GET /health` e em New Relic > APM & services busque `loja-magazine-hero-api`. Faca duas requisicoes de teste; a ingestao pode demorar alguns minutos.
6. Se houver problema, defina `NEW_RELIC_ENABLED=false` e faca restart/redeploy. A API deve iniciar normalmente sem agente.
7. Revise ingestao e privacidade. Nao enviar JWT, senhas, credenciais, dados de cartao ou payloads sensiveis como atributos/logs. As metricas APM basicas sao suficientes para esta fase.

## Testes e limites

- `sh -n docker/entrypoint.sh` valida a sintaxe do script.
- `docker build -t loja-magazine-hero:apm .` verifica a obtencao e copia do agente.
- Executar o container **sem chave** comprova que o inicio nao depende da New Relic.
- Sem acesso a conta privada New Relic, a Render Environment e verificacao de telemetria real dependem de autenticacao na plataforma.
- O plano gratuito possui cotas e pode variar: acompanhar os termos oficiais antes de habilitar ingestao. Nada neste PR ativa cobranca.

## Referencias

- https://docs.newrelic.com/docs/apm/agents/java-agent/additional-installation/install-new-relic-java-agent-docker/
- https://docs.newrelic.com/docs/apm/agents/java-agent/installation/java-agent-installation-maven/
- https://newrelic.com/pricing
