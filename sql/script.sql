-- create database folha_pagamento
-- create schema pagamentos
CREATE TYPE parentesco AS enum('FILHO', 'SOBRINHO', 'OUTROS');

CREATE TABLE funcionario (

id_funcionario SERIAL PRIMARY KEY,
nome VARCHAR(100)NOT NULL,
cpf VARCHAR(14)UNIQUE NOT NULL,
data_nascimento DATE NOT NULL,
salario_bruto DECIMAL(10,2) NOT NULL
);

CREATE TABLE dependente(

id_dependente SERIAL PRIMARY KEY,
nome VARCHAR(100)NOT NULL,
cpf VARCHAR(14)UNIQUE NOT NULL,
data_nascimento DATE NOT NULL,
parentesco parentesco NOT NULL,
id_funcionario INT REFERENCES funcionarios(id_funcionario)
);

CREATE TABLE folhade_pagamento(

id_folha SERIAL PRIMARY KEY,
data_pagamento DATE NOT NULL,
desconto_inss DECIMAL(10,2),
desconto_ir	DECIMAL	(10,2),
salario_liquido DECIMAL (10,2),
id_funcionario INT REFERENCES funcionarios(id_funcionario)
);

UPDATE -- Define a Tabela que será Modificada.
SET -- Indica a Coluna a ser Alterada e o Novo Valor.
WHERE -- Filtra quais Linhas Serão Atualizadas.Se Omitido, Todos os Registros da Tabela Serão Alterados.  

DELETE FROM --nome_tabela 
WHERE --condição 

SELECT --qual_coluna 
FROM  --qual_tabela  
WHERE --candição  
ORDER BY  --qual_ordem 