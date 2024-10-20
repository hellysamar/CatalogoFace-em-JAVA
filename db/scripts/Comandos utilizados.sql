/**
	Projeto de Carometro, cataloga dados de usuário, seja aluno, cliente, etc
    @author Dev Hellysamar
*/

CREATE DATABASE dbCatalogoFace;
USE dbCatalogoFace;

-- Cria uma tabela com seus campos
CREATE TABLE tblUsuarios (
	-- Infirma-se NOME | TIPO | PADRÃO dos Campos
	registro INT PRIMARY KEY AUTO_INCREMENT, 	-- PRIMARY KEY informa que esse é o campo para conexão com outra tabela, AUTO_INCREMENT adiciona automaticamente o numero a esse Campo
    nome varchar(30) NOT NULL, 					-- NOT NULL informa que o campo é obrigatório, não permitindo NULLo
    foto LONGBLOB NOT NULL 						-- BINARY LARGE OBJECT (BLOB)
);

-- Descreve a tabela
DESCRIBE tblUsuarios;

ALTER TABLE tblUsuarios ADD COLUMN endereco VARCHAR(50) NOT NULL;
	
select * from tblusuarios;
describe tblusuarios;

SELECT * FROM tblusuarios WHERE nome LIKE '%sa%' ORDER BY nome LIMIT 1;