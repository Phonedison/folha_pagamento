# 📑 Sistema de Folha de Pagamento - Desafio POO - GRUPO 06 ANTIGO, NOVO 07

Este projeto é o trabalho final da disciplina de **Programação Orientada a Objetos** do Serratec. O objetivo é desenvolver um **sistema de gestão de folha de pagamento**, para o cálculo de salário líquido, aplicando conceitos e persistência de dados.

## 🚀 Sobre o projeto

A aplicação permite o gerenciamento completo de:

- Funcionários
- Dependentes
- Folha de pagamento

Além disso, realiza automaticamente o cálculo de:

- Desconto de INSS
- Desconto de Imposto de Renda (IR)
- Salário líquido

O sistema também realiza integração com:

- 📂 Arquivos CSV (entrada e saída)
- 🗄️ Banco de dados PostgreSQL

---

### 🔍 Funcionalidades

#### 👤 Funcionário

- Cadastro, listagem, atualização e exclusão
- Validação de CPF único
- Armazenamento de salário bruto

#### 👶 Dependente

- Associação com funcionário
- Validação de:
  - Idade (< 18 anos)
  - CPF único
- Definição de parentesco via ENUM:
  - FILHO
  - SOBRINHO
  - OUTROS

#### 💰 Folha de Pagamento

- Cálculo automático:
  - INSS (com teto)
  - IR (com base nas faixas salariais)
- Consideração de dependentes (dedução de R$ 189,59 por dependente)
- Armazenamento no banco de dados

---

### 📂 CSV (Importação / Exportação)

#### 📥 Importação:

- Leitura de arquivos CSV contendo:
  - Funcionários
  - Dependentes
- Inserção automática no banco
- Geração automática da folha de pagamento

#### 📤 Exportação:

- Geração de arquivo CSV com:
  - Nome
  - CPF
  - Desconto INSS
  - Desconto IR
  - Salário líquido
- Nome do arquivo gerado automaticamente:
  `folha_pagamento_YYYY-MM-DD.csv`

---

### 🛠️ Tecnologias e Conceitos Aplicados

#### 💻 Linguagem

- Java(JDK 17+)

#### 🗄️ Banco de Dados

- PostgreSQL
- JDBC

---

### 📚 Conceitos de POO aplicados

- ✅ Abstração
  - Classe `Pessoa`
- ✅ Herança
  - `Funcionario` e `Dependente` herdam de `Pessoa`
- ✅ Encapsulamento
  - Uso de getters/setters e validações internas
- ✅ Enum
  - `Parentesco` para padronização de valores
- ✅ Exceções personalizadas
  - `CpfDuplicado`
  - `DependenteException`
- ✅ Interfaces
  - `CriacaoTabela` para padronização de criação de tabelas
- ✅ Coleções
  - `HashSet` para dependentes
  - ` ArrayList` para leitura de CSV
- ✅ Separação por camadas
  - Model
  - Repository (DAO)
  - Service
  - App (menu)

- ***

### 🏗️ Estrutura do Projeto

```
...
src/sistema
├── app
│   ├── menu
│   │   ├── Menus.java
│   │   └── CustomLogger.java
│   └── Main.java
│
├── enums
│   └── Parentesco.java
│
├── exception
│   ├── CpfDuplicado.java
│   └── DependenteException.java
│
├── model
│   ├── Pessoa.java
│   ├── Funcionario.java
│   ├── Dependente.java
│   └── FolhaPagamento.java
│
├── repository
│   ├── ConexaoDB.java
│   ├── InicializarDB.java
│   ├── FuncionarioDAO.java
│   ├── DependenteDAO.java
│   └── FolhaPagamentoDAO.java
│
└── service
    ├── CsvService.java
    ├── LeitorCSV.java
    └── EscritorCSV.java
```

---

### ⚙️ Banco de Dados

O sistema cria automaticamente:

- Tabela `funcionario`
- Tabela `dependente`
- Tabela `folha_pagamento`
- Enum `parentesco`

Relacionamentos:

- Funcionário → Dependente (1:N)
- Funcionário → Folha de Pagamento (1:N)

---

## 📋 Como Executar

### Pré-requisitos:

- Java JDK 17+
- PostgreSQL instalado
- Driver JDBC configurado (`postgresql-42.7.10.jar)`)

### Passo a passo:

```bash
# Clone o repositório
git clone https://github.com/Phonedison/folha_pagamento.git

# Acesse o diretório
cd folha_pagamento

# Execute pela IDE (recomendado)
```

OU via Terminal

```bash
javac Main.java
java Main
```

---

## 💡 Fluxo de Uso

1.  Conectar ao banco de dados
2.  Inicializar tabelas automaticamente
3.  Escolher uma opção:

- Cadastro manual
- Importação CSV

4. Sistema:

- Persiste dados
- Calcula folha automaticamente

5. Exportar CSV com resultados

---

## ⚠️ Regras

- CPF deve ser único (funcionário e dependente)
- Dependente deve ter menos de 18 anos
- Cada dependente reduz R$ 189,59 no cálculo do IR
- INSS respeita teto de R$ 8.157,41
- Cálculo de IR baseado em faixas salariais

---

# 👥 Grupo

|                                  Integrantes                                   | GitHub                                                       |
| :----------------------------------------------------------------------------: | :----------------------------------------------------------- | :------------------------------------------------------------------------------------------: |
| <img src="https://avatars.githubusercontent.com/u/242983951?v=4" width="100"/> | [João Pedro Carneiro Motta](https://github.com/joaopedrobr3) |                         CONSTRUTOR DO CALCULO DA FOLHA DE PAGAMENTO                          |
| <img src="https://avatars.githubusercontent.com/u/266845673?v=4" width="100"/> | [Kenny](https://github.com/kennypavelka)                     | E DANDO DICAS <img src="https://i.ytimg.com/vi/URvro2UkQzY/maxresdefault.jpg"  width="200"/> |
|  <img src="https://avatars.githubusercontent.com/u/7592603?v=4" width="100"/>  | [Lucas Leal da Silva](https://github.com/Phonedison)         |                                     CONSTRUTOR DAS DAOS                                      |
| <img src="https://avatars.githubusercontent.com/u/267740773?v=4" width="100"/> | [Paulo Cesar Neto](https://github.com/paulocesar-neto)       |                                 CRIADOR DO CONCEITO DO MENU                                  |
| <img src="https://avatars.githubusercontent.com/u/267657658?v=4" width="100"/> | [Vinicius de Souza Lamas](https://github.com/vLamass)        |                                      TESTADOR DO CÓDIGO                                      |
| <img src="https://avatars.githubusercontent.com/u/267758048?v=4" width="100"/> | [Vitor Ribeiro](https://github.com/vitorribeiro77)           |                                CONSTRUTOR E CONSELHEIRO DO DB                                |
