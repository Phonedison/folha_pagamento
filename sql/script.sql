-- CREATE database folha_pagamento
-- create schema pagamentos

/*Criação do TIPO parentesco como ENUM*/
CREATE TYPE parentesco AS enum('FILHO', 'SOBRINHO', 'OUTROS');

/*Criação da tabela de funcionario*/
CREATE TABLE funcionario (

id_funcionario SERIAL PRIMARY KEY,
nome VARCHAR(100) NOT NULL,
cpf VARCHAR(14) UNIQUE NOT NULL,
data_nascimento DATE NOT NULL,
salario_bruto NUMERIC(15,2) NOT NULL
);

/*Criação da tabela de dependente*/
CREATE TABLE dependente (

id_dependente SERIAL PRIMARY KEY,
nome VARCHAR(100) NOT NULL,
cpf VARCHAR(14) UNIQUE NOT NULL,
data_nascimento DATE NOT NULL,
parentesco parentesco NOT NULL,
id_funcionario INT REFERENCES funcionario(id_funcionario) NOT NULL
);

/*Criação da tabela Folha de Pagamento*/
CREATE TABLE folha_pagamento (

codigo SERIAL PRIMARY KEY,
data_pagamento DATE NOT NULL,
desconto_inss NUMERIC(15,2),
desconto_ir	NUMERIC(15,2),
salario_liquido NUMERIC(15,2),
id_funcionario INT REFERENCES funcionario(id_funcionario) NOT NULL
);	

/*Comandos para atualizar as tabelas*/
UPDATE -- Define a Tabela que será Modificada.
SET -- Indica a Coluna a ser Alterada e o Novo Valor.
WHERE -- Filtra quais Linhas Serão Atualizadas.Se Omitido, Todos os Registros da Tabela Serão Alterados.  

--UPDATE funcionario SET nome = "Lucas" WHERE id_funcionario = 1

/*Comando para excluir algum dado em uma tabela*/
DELETE FROM --nome_tabela 
WHERE --condição 

-- DELETE funcionario WHERE id_funcionario = 1 

/*Comando para obter alguma informação em uma tabela*/
SELECT --qual_coluna 
FROM  --qual_tabela  
WHERE --candição  
ORDER BY  --qual_ordem 

--SELECT nome FROM funcionario WHERE nome = "Lucas" ORDER BY id_funcionario