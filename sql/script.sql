-- create database folha_pagamento
-- create schema pagamentos
create type parentesco as enum('FILHO', 'SOBRINHO', 'OUTROS');

create table funcionarios (

id_funcionario serial primary key,
nome varchar(100)not null,
cpf varchar(14)unique not null,
data_nascimento date not null,
salario_bruto numeric(10,2) not null
);

create table dependente(

id_dependente serial primary key,
nome varchar(100)not null,
cpf varchar(14)unique not null,
data_nascimento date not null,
parentesco parentesco not null,
id_funcionario int references funcionarios(id_funcionario)
);

create table folhade_pagamento(

id_folha serial primary key,
data_pagamento date not null,
desconto_inss numeric(10,2),
desconto_ir	numeric	(10,2),
salario_liquido numeric (10,2),
id_funcionario int references funcionarios(id_funcionario)
);	
