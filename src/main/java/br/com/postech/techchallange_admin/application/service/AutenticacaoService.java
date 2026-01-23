package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.AutenticarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.in.LogoutAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import br.com.postech.techchallange_admin.domain.port.out.PasswordEncoderPort;
import br.com.postech.techchallange_admin.domain.port.out.TokenBlacklistPort;
import org.springframework.stereotype.Service;
import br.com.postech.techchallange_admin.domain.exception.BusinessException;

@Service
public class AutenticacaoService implements AutenticarAdminUseCase, LogoutAdminUseCase {

    private final AdminRepositoryPort adminRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenBlacklistPort tokenBlacklistPort;

    public AutenticacaoService(AdminRepositoryPort adminRepositoryPort,
                               PasswordEncoderPort passwordEncoderPort,
                               TokenBlacklistPort tokenBlacklistPort) {
        this.adminRepositoryPort = adminRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenBlacklistPort = tokenBlacklistPort;
    }

    @Override
    public Admin autenticar(String email, String senha) {

        Admin admin = adminRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário ou senha inválidos"));

        if (!passwordEncoderPort.matches(senha, admin.getSenhaHash())) {
            throw new BusinessException("Usuário ou senha inválidos");
        }
        return admin;
    }

    @Override
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        tokenBlacklistPort.blacklistToken(token);
    }
}
