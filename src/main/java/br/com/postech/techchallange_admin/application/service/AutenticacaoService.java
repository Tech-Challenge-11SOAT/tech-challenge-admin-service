package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.AutenticarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.in.LogoutAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import br.com.postech.techchallange_admin.infrastructure.security.TokenBlacklistService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.postech.techchallange_admin.domain.exception.BusinessException;

@Service
public class AutenticacaoService implements AutenticarAdminUseCase, LogoutAdminUseCase {

    private final AdminRepositoryPort adminRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;

    public AutenticacaoService(AdminRepositoryPort adminRepositoryPort,
                               PasswordEncoder passwordEncoder,
                               TokenBlacklistService tokenBlacklistService) {
        this.adminRepositoryPort = adminRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public Admin autenticar(String email, String senha) {

        Admin admin = adminRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(senha, admin.getSenhaHash())) {
            throw new BusinessException("Usuário ou senha inválidos");
        }
        return admin;
    }

    @Override
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        tokenBlacklistService.blacklistToken(token);
    }
}