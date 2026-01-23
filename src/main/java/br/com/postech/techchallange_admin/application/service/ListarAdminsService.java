package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.ListarAdminsUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarAdminsService implements ListarAdminsUseCase {

    private final AdminRepositoryPort adminRepositoryPort;

    public ListarAdminsService(AdminRepositoryPort adminRepositoryPort) {
        this.adminRepositoryPort = adminRepositoryPort;
    }

    @Override
    public List<Admin> listarTodos() {
        return adminRepositoryPort.findAll();
    }
}