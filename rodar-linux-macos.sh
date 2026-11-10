#!/usr/bin/env sh
set -eu
docker compose up -d
mvn spring-boot:run
