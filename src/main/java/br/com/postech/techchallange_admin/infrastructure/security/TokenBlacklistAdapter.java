package br.com.postech.techchallange_admin.infrastructure.security;

import br.com.postech.techchallange_admin.domain.port.out.TokenBlacklistPort;
import org.springframework.stereotype.Component;

/**
 * Adaptador de infraestrutura para TokenBlacklistPort
 * Implementa a porta usando TokenBlacklistService
 */
@Component
public class TokenBlacklistAdapter implements TokenBlacklistPort {

    private final TokenBlacklistService tokenBlacklistService;

    public TokenBlacklistAdapter(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public void blacklistToken(String token) {
        tokenBlacklistService.blacklistToken(token);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistService.isTokenBlacklisted(token);
    }
}
