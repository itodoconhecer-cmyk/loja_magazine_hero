#!/bin/sh
set -eu

# Fail closed for absent New Relic credentials; do not print secret values.
if [ "${NEW_RELIC_ENABLED:-false}" = "true" ] && [ -n "${NEW_RELIC_LICENSE_KEY:-}" ]; then
  exec java -XX:MaxRAMPercentage=70.0 -javaagent:/app/newrelic/newrelic.jar -jar /app/application.jar
fi

exec java -XX:MaxRAMPercentage=70.0 -jar /app/application.jar
