package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.exception.BusinessException;
import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.ToggleAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class ToggleAdminService implements ToggleAdminUseCase {

    private final AdminRepositoryPort adminRepositoryPort;

    public ToggleAdminService(AdminRepositoryPort adminRepositoryPort) {
        this.adminRepositoryPort = adminRepositoryPort;
    }

    @Override
    public Admin toggle(String id, boolean ativar) {

        Admin admin = adminRepositoryPort.findById(id)
                .orElseThrow(() -> new BusinessException("Administrador não encontrado"));

        admin.setAtivo(ativar);

        return adminRepositoryPort.update(admin);
    }
}