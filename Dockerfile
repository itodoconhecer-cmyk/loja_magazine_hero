FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY src ./src
RUN mvn -B -ntp -DskipTests package

# Agente APM oficial, versao fixa. Baixado somente no build, sem credenciais.
ADD https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java-9.5.0.zip /workspace/newrelic-java.zip
RUN mkdir -p /workspace/agent \
    && cd /workspace/agent \
    && jar xf /workspace/newrelic-java.zip \
    && test -f /workspace/agent/newrelic/newrelic.jar

FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app
RUN groupadd --system app && useradd --system --gid app --home-dir /app app \
    && mkdir -p /app/logs/operations /app/logs/security /app/logs/errors \
    && chown -R app:app /app/logs
COPY --from=build --chown=app:app /workspace/target/*.jar /app/application.jar
COPY --from=build --chown=app:app /workspace/agent/newrelic/ /app/newrelic/
COPY --chown=app:app docker/entrypoint.sh /app/entrypoint.sh
RUN chmod 0555 /app/entrypoint.sh
USER app
EXPOSE 8080
ENTRYPOINT ["/app/entrypoint.sh"]
