CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS usuario;

CREATE TABLE usuario (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         nome VARCHAR(150) NOT NULL,
                         email VARCHAR(150) NOT NULL,
                         cpf VARCHAR(11) NOT NULL,
                         telefone VARCHAR(20) NOT NULL,
                         data_nascimento DATE NOT NULL,
                         data_cadastro TIMESTAMP NOT NULL DEFAULT now(),

                         CONSTRAINT uk_usuario_email UNIQUE (email),
                         CONSTRAINT uk_usuario_cpf UNIQUE (cpf),
                         CONSTRAINT uk_usuario_telefone UNIQUE (telefone)
);