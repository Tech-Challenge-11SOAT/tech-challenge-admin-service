package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.exception.BusinessException;
import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.CriarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import br.com.postech.techchallange_admin.domain.port.out.PasswordEncoderPort;
import org.springframework.stereotype.Service;
import br.com.postech.techchallange_admin.domain.port.in.LogAdminActionUseCase;

import java.util.UUID;

@Service
public class CriarAdminService implements CriarAdminUseCase {

    private final AdminRepositoryPort adminRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final LogAdminActionUseCase logAdminActionUseCase;

    public CriarAdminService(AdminRepositoryPort adminRepositoryPort,
                             PasswordEncoderPort passwordEncoderPort,
                             LogAdminActionUseCase logAdminActionUseCase) {
        this.adminRepositoryPort = adminRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.logAdminActionUseCase = logAdminActionUseCase;
    }

    @Override
    public Admin criar(Admin admin) {
        if (adminRepositoryPort.existsByEmail(admin.getEmail())) {
            throw new BusinessException("Email ja cadastrado");
        }
        if (admin.getRoles() == null || admin.getRoles().isEmpty()) {
            throw new BusinessException("Um administrador precisa ter pelo menos uma role.");
        }

        // Gerar UUID se o ID não foi definido
        if (admin.getId() == null || admin.getId().isEmpty()) {
            admin.setId(UUID.randomUUID().toString());
        }

        String senhaCriptografada = passwordEncoderPort.encode(admin.getSenhaHash());
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
