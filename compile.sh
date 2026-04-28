#!/bin/bash
# Script de compilação com UTF-8 e compatibilidade Java 17
# Remove o diretório bin
rm -rf bin
mkdir -p bin

# Compila o projeto com encoding UTF-8
javac -encoding UTF-8 -source 17 -target 17 -d bin -sourcepath src src/sistema/app/Main.java

if [ $? -eq 0 ]; then
    echo ""
    echo "Compilação concluída com sucesso!"
    echo ""
    echo "Para executar o programa, use:"
    echo "  java -cp bin sistema.app.Main"
else
    echo "Erro na compilação!"
fi
