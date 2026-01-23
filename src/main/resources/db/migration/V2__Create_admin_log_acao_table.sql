-- Tabela de logs de ações dos administradores
CREATE TABLE admin_log_acao (
    id VARCHAR(36) PRIMARY KEY,
    id_admin VARCHAR(36) NOT NULL,
    acao VARCHAR(100) NOT NULL,
    recurso_afetado VARCHAR(100) NOT NULL,
    id_recurso_afetado VARCHAR(255),
    data_acao TIMESTAMP NOT NULL
);

-- Índices
CREATE INDEX idx_admin_log_acao_id_admin ON admin_log_acao(id_admin);
CREATE INDEX idx_admin_log_acao_data_acao ON admin_log_acao(data_acao);
CREATE INDEX idx_admin_log_acao_acao ON admin_log_acao(acao);
CREATE INDEX idx_admin_log_acao_recurso_afetado ON admin_log_acao(recurso_afetado);
