package br.com.postech.techchallange_admin.domain.port.out;

import br.com.postech.techchallange_admin.domain.model.AdminLogAcao;

public interface AdminLogAcaoRepositoryPort {
    void registrarLog(AdminLogAcao log);
}