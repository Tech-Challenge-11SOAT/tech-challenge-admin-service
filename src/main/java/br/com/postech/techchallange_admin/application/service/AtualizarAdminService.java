package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.AtualizarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class AtualizarAdminService implements AtualizarAdminUseCase {

    private final AdminRepositoryPort adminRepositoryPort;

    public AtualizarAdminService(AdminRepositoryPort adminRepositoryPort) {
        this.adminRepositoryPort = adminRepositoryPort;
    }

    @Override
    public Admin atualizar(Admin admin) {
        return adminRepositoryPort.update(admin);
    }
}