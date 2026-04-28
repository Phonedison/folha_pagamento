@echo off
REM Script de compilação com UTF-8 e compatibilidade Java 17
REM Limpa o diretório bin
if exist bin rmdir /s /q bin
mkdir bin

REM Compila o projeto com encoding UTF-8
javac -encoding UTF-8 -source 17 -target 17 -d bin -sourcepath src src/sistema/app/Main.java

if %errorlevel% equ 0 (
    echo.
    echo Compilação concluída com sucesso!
    echo.
    echo Para executar o programa, use:
    echo   java -cp bin sistema.app.Main
) else (
    echo Erro na compilação!
)
