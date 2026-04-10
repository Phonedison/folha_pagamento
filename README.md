# 📑 Sistema de Folha de Pagamento - Desafio POO

Este projeto é o trabalho final da disciplina de **Programação Orientada a Objetos** do Serratec. O objetivo é desenvolver um sistema robusto para o cálculo de salário líquido, aplicando conceitos de engenharia de software e persistência de dados.

## 🚀 Sobre o projeto

O sistema permite o cadastro e gerenciamento de funcionários e seus dependentes, realizando o cálculo automatizado do salário líquido com base nas regras definidas.

### 🔍 Principais funcionalidades:

- Cadastro de funcionários
- Associação de dependentes
- Validação de regras de negócio _(idade, parentesco, CPF único)_
- Cálculo de salário bruto e líquido
- Aplicação de descontos _(INSS e IR)_
- Persistência de dados via arquivo e/ou banco de dados

### 🛠️ Tecnologias e Conceitos Aplicados

- **Linguagem**: Java(JDK 17+)
- **Conceitos de POO**:
  - **Abstração e Herança**: Classe `Pessoa` como base para `Funcionario` e `Dependente`.
  - **Encapsulamento**: Proteção de dados sensíveis e cálculo via métodos acessores.
  - **Polimorfismo**: Implementação de interfaces para regras de cálculo.
    <!-- - **Tratamento de Erros**: Uso de exceções personalizadas como _(DependenteException)_ para validação de regras de negócio. -->
    <!-- - **Coleções**: uso de `HashSet` para garantir a unicidade de CPFs; -->

---

### 🏗️ Estrutura do Projeto

O projeto está organizado seguindo padrões de separação de responsabilidades:

- `model`: Entidades principais e representação de dados.
- `service`: Core business _(Lógica de cálculos de INSS e IR)_.
- `repository`: Camada de persistência _(DAO e I/O de arquivos)_.
- `exception`: Gestão de erros e validações específicas.

```
Folha_pagamento
├── 📁 sql
│   └── 📄 script.sql                     # Criação de tabelas, restrições e relacionamentos
├── 📁 src/sistema
│   ├── 📁 app
│   │   └── ☕ Main.java                  # Inicialização da aplicação e interface via console
│   ├── 📁 enums
│   │   └── ☕ Parentesco.java            # Definições de tipos enumerados para validação de dependentes
│   ├── 📁 exception
│   │   ├── ☕ CpfDuplicado.java          # Exceção customizada para violações de integridade de CPF
│   │   └── ☕ DependenteException.java   # Tratamento de regras de negócio (idade e parentesco)
│   ├── 📁 model
│   │   ├── ☕ Pessoa.java                # Superclasse abstrata com atributos base (Nome, CPF, Nascimento)
│   │   ├── ☕ Funcionario.java           # Entidade principal
│   │   ├── ☕ Dependente.java            # Entidade vinculada ao funcionário
│   │   └── ☕ FolhaPagamento.java        # Modelo para processamento e cálculo de vencimentos
│   └── 📁 repository
│       ├── ☕ ConexaoDB.java             # Gerenciamento da conexão
│       ├── ☕ FuncionarioDAO.java        # Operações CRUD no Banco de Dados
│       ├── ☕ ConsultasSQL.java          # Constantes com queries SQL para centralização do código
│       └── ☕ DadosConexao.java          # DTO para armazenamento de credenciais de acesso
├── ⚙️ .gitignore                         # Arquivos ignorados pelo controle de versão
└── 📝 README.md                          # Documentação técnica e equipe de desenvolvimento
```

---

## 📋 Como Executar

### Pré-requisitos:

- Java JDK 17 ou superior
- IDE _(IntelliJ, Eclipse ou VS Code)_

### Passo a passo:

```bash
# Clone o repositório
git clone https://github.com/Phonedison/folha_pagamento.git

# Acesse a pasta do projeto
cd folha_pagamento

# Compile o projeto
javac Main.java

# Execute
java Main
```

> 💡 Caso utilize uma IDE, basta importar o projeto e executar a classe Main.

# 👥 Grupo e Responsabilidades

|                                  Integrantes                                   | GitHub                                                       |
| :----------------------------------------------------------------------------: | :----------------------------------------------------------- |
| <img src="https://avatars.githubusercontent.com/u/242983951?v=4" width="100"/> | [João Pedro Carneiro Motta](https://github.com/joaopedrobr3) |
| <img src="https://avatars.githubusercontent.com/u/266845673?v=4" width="100"/> | [Kenny](https://github.com/kennypavelka)                     |
|  <img src="https://avatars.githubusercontent.com/u/7592603?v=4" width="100"/>  | [Lucas Leal da Silva](https://github.com/Phonedison)         |
| <img src="https://avatars.githubusercontent.com/u/267740773?v=4" width="100"/> | [Paulo Cesar Neto](https://github.com/paulocesar-neto)       |
| <img src="https://avatars.githubusercontent.com/u/267657658?v=4" width="100"/> | [Vinicius de Souza Lamas](https://github.com/vLamass)        |
| <img src="https://avatars.githubusercontent.com/u/267758048?v=4" width="100"/> | [Vitor Ribeiro](https://github.com/vitorribeiro77)           |
