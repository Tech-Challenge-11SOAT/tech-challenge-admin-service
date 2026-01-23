package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.BuscarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarAdminService implements BuscarAdminUseCase {

    private final AdminRepositoryPort adminRepositoryPort;

    public BuscarAdminService(AdminRepositoryPort adminRepositoryPort) {
        this.adminRepositoryPort = adminRepositoryPort;
    }

    @Override
    public Optional<Admin> buscarPorId(String id) {
        return adminRepositoryPort.findById(id);
    }

    @Override
    public Optional<Admin> buscarPorEmail(String email) {
        return adminRepositoryPort.findByEmail(email);
    }
}