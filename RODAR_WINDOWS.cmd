@echo off
setlocal
where java >nul 2>nul || (echo Java nao encontrado. Instale JDK 25.& exit /b 1)
where mvn >nul 2>nul || (echo Maven nao encontrado.& exit /b 1)
docker compose up -d
mvn spring-boot:run
