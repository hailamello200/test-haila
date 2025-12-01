#!/bin/bash

# Caminho do projeto Gatling Maven
PROJETO_DIR=~/gatling/gatling-maven-plugin-demo-java-main

# Pasta onde o Gatling vai gerar os resultados
RESULTS_DIR=$PROJETO_DIR/target/gatling

echo "--------------------------------------------------"
echo "Iniciando execução de testes Gatling via Maven..."
echo "Projeto: $PROJETO_DIR"
echo "Resultados serão salvos em: $RESULTS_DIR"
echo "--------------------------------------------------"

# Certifica que a pasta de resultados existe
mkdir -p $RESULTS_DIR

# Vai para o diretório do projeto
cd $PROJETO_DIR || { echo "Erro: diretório do projeto não encontrado!"; exit 1; }

# Roda os testes Gatling via Maven
mvn gatling:test

# Checa se a execução foi bem sucedida
if [ $? -eq 0 ]; then
    echo "--------------------------------------------------"
    echo "Testes concluídos com sucesso!"
    echo "Relatórios disponíveis em: $RESULTS_DIR"
    echo "--------------------------------------------------"
else
    echo "--------------------------------------------------"
    echo "Erro na execução dos testes."
    echo "--------------------------------------------------"
    exit 1
fi
