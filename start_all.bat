@echo off
title TeamForge AI - Full-Stack Application Launcher
echo ===================================================================
echo   Starting TeamForge AI - Full Stack Platform (Java + Python ML)
echo   Smart Team Formation Optimiser for College Students
echo ===================================================================

echo [1/2] Starting Python ML Microservice on port 5000...
start "TeamForge AI - Python ML Service" cmd /k "start_ml.bat"

echo [2/2] Starting Java Spring Boot Backend on port 8080...
start "TeamForge AI - Spring Boot Backend" cmd /k "start_backend.bat"

echo.
echo ===================================================================
echo   Both services are launching in background windows!
echo   Once Spring Boot displays 'Started TeamForgeAiApplication', open:
echo   http://localhost:8080
echo ===================================================================
echo.
timeout /t 5 >nul
echo Attempting to open browser at http://localhost:8080...
start http://localhost:8080
echo.
pause
