-- Adiciona senha e role com defaults para compatibilidade com registros existentes
ALTER TABLE usuario ADD COLUMN senha VARCHAR(255) NOT NULL DEFAULT '$2a$10$wE8wY01Yf8vQ9cK6zZf0xe9uHqQ8xY.gN0m0n0a0b0c0d0e0f0g';
ALTER TABLE usuario ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';
ALTER TABLE usuario ALTER COLUMN senha DROP DEFAULT;

-- Adiciona a coluna de auditoria de modificação
ALTER TABLE usuario ADD COLUMN data_modificacao TIMESTAMP;

-- Garante tamanho adequado para CPF e telefone (formatados ou não)
ALTER TABLE usuario ALTER COLUMN cpf TYPE VARCHAR(14);
ALTER TABLE usuario ALTER COLUMN telefone TYPE VARCHAR(20);