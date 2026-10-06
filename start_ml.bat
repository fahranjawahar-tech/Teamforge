@echo off
title TeamForge AI - Python ML Microservice (Port 5000)
echo ========================================================
echo   Starting TeamForge AI - Python ML Microservice
echo ========================================================
cd /d "%~dp0"
python ml_service\app.py
pause
