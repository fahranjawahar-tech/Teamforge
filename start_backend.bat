@echo off
title TeamForge AI - Java Spring Boot Backend (Port 8080)
echo ========================================================
echo   Starting TeamForge AI - Java Spring Boot Backend
echo ========================================================
cd /d "%~dp0"
if exist "apache-maven-3.9.9\bin\mvn.cmd" (
    apache-maven-3.9.9\bin\mvn.cmd spring-boot:run
) else (
    mvn spring-boot:run
)
pause
