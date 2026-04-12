-- CREATE database folha_pagamento
-- create schema pagamentos
CREATE TYPE parentesco AS enum('FILHO', 'SOBRINHO', 'OUTROS');

CREATE TABLE funcionario (

id_funcionario SERIAL PRIMARY KEY,
nome VARCHAR(100) NOT NULL,
cpf VARCHAR(14) UNIQUE NOT NULL,
data_nascimento DATE NOT NULL,
salario_bruto NUMBER (15,2) NOT NULL
);

CREATE TABLE dependente (

id_dependente SERIAL PRIMARY KEY,
nome VARCHAR(100) NOT NULL,
cpf VARCHAR(14) UNIQUE NOT NULL,
data_nascimento DATE NOT NULL,
parentesco parentesco NOT NULL,
id_funcionario INT REFERENCES funcionarios(id_funcionario) NOT NULL
);

CREATE TABLE folha_pagamento (

codigo SERIAL PRIMARY KEY,
data_pagamento DATE NOT NULL,
desconto_inss NUMBER (15,2),
desconto_ir	NUMBER (15,2),
salario_liquido NUMBER (15,2),
id_funcionario INT REFERENCES funcionarios(id_funcionario) NOT NULL
);	

