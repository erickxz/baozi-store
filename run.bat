@echo off
REM Sobe a API Baozi Store em http://localhost:8080 usando o Maven Wrapper
REM (o mvnw.cmd baixa o Maven sozinho na primeira execucao).
cd /d "%~dp0"
call "%~dp0mvnw.cmd" spring-boot:run
