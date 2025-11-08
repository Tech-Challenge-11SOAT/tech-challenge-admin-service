package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.port.in.DeletarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class DeletarAdminService implements DeletarAdminUseCase {

    private final AdminRepositoryPort adminRepositoryPort;

    public DeletarAdminService(AdminRepositoryPort adminRepositoryPort) {
        this.adminRepositoryPort = adminRepositoryPort;
    }

    @Override
    public void deletar(String id) {
        adminRepositoryPort.deleteById(id);
    }
}