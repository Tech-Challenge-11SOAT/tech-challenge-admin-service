package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.CriarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CriarAdminService implements CriarAdminUseCase {

    private final AdminRepositoryPort adminRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public CriarAdminService(AdminRepositoryPort adminRepositoryPort, PasswordEncoder passwordEncoder) {
        this.adminRepositoryPort = adminRepositoryPort;
        this.passwordEncoder = passwordEncoder; // <-- ADICIONE
    }

    @Override
    public Admin criar(Admin admin) {

        if (adminRepositoryPort.existsByEmail(admin.getEmail())) {
            throw new IllegalArgumentException("Email ja cadastrado");
        }
        if (admin.getRoles() == null || admin.getRoles().isEmpty()) {
            throw new IllegalArgumentException("Um administrador precisa ter pelo menos uma role.");
        }

        String senhaCriptografada = passwordEncoder.encode(admin.getSenhaHash());
        admin.setSenhaHash(senhaCriptografada);

        return adminRepositoryPort.save(admin);
    }
}

