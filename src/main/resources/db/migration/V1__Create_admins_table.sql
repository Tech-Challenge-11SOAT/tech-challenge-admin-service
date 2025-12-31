-- Tabela de administradores
CREATE TABLE admins (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    roles JSONB NOT NULL DEFAULT '[]'::jsonb,
    ativo BOOLEAN NOT NULL DEFAULT true,
    data_criacao TIMESTAMP NOT NULL,
    data_ultima_atualizacao TIMESTAMP NOT NULL,
    versao INTEGER NOT NULL DEFAULT 0
);

-- Índices
CREATE INDEX idx_admins_email ON admins(email);
CREATE INDEX idx_admins_nome ON admins USING gin(to_tsvector('portuguese', nome));
CREATE INDEX idx_admins_ativo ON admins(ativo);
