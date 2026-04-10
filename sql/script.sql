-- CREATE database folha_pagamento
-- create schema pagamentos
CREATE TYPE parentesco AS enum('FILHO', 'SOBRINHO', 'OUTROS');

CREATE TABLE funcionario (

id_funcionario SERIAL PRIMARY KEY,
nome VARCHAR(100)NOT NULL,
cpf VARCHAR(14)UNIQUE NOT NULL,
data_nascimento DATE NOT NULL,
salario_bruto DOUBLE NOT NULL
);

CREATE TABLE dependente (

id_dependente SERIAL PRIMARY KEY,
nome VARCHAR(100)NOT NULL,
cpf VARCHAR(14)UNIQUE NOT NULL,
data_nascimento DATE NOT NULL,
parentesco parentesco NOT NULL,
id_funcionario INT REFERENCES funcionarios(id_funcionario)
);

CREATE TABLE folha_pagamento (

id_folha SERIAL PRIMARY KEY,
data_pagamento DATE NOT NULL,
desconto_inss DOUBLE,
desconto_ir	DOUBLE,
salario_liquido DOUBLE,
id_funcionario INT REFERENCES funcionarios(id_funcionario)
);	
