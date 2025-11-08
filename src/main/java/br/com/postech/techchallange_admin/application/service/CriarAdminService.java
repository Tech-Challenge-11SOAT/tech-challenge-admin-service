package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.CriarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class CriarAdminService implements CriarAdminUseCase {

    private final AdminRepositoryPort adminRepositoryPort;

    public CriarAdminService(AdminRepositoryPort adminRepositoryPort) {
        this.adminRepositoryPort = adminRepositoryPort;
    }

    @Override
    public Admin criar(Admin admin) {
        if (adminRepositoryPort.existsByEmail(admin.getEmail())) {
            throw new IllegalArgumentException("Email ja cadastrado");
        }
        return adminRepositoryPort.save(admin);
    }
}

