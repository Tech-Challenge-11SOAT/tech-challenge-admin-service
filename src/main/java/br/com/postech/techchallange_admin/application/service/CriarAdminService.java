package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.CriarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.postech.techchallange_admin.domain.port.in.LogAdminActionUseCase;

@Service
public class CriarAdminService implements CriarAdminUseCase {

    private final AdminRepositoryPort adminRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final LogAdminActionUseCase logAdminActionUseCase;

    public CriarAdminService(AdminRepositoryPort adminRepositoryPort,
                             PasswordEncoder passwordEncoder,
                             LogAdminActionUseCase logAdminActionUseCase) {
        this.adminRepositoryPort = adminRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.logAdminActionUseCase = logAdminActionUseCase;
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

        Admin adminSalvo = adminRepositoryPort.save(admin);

        logAdminActionUseCase.registrar(
                adminSalvo.getId(),
                "CADASTRO",
                "ADMIN_USER",
                adminSalvo.getId()
        );

        return adminSalvo;
    }
}

