package br.com.postech.techchallange_admin.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenBlacklistServiceTest {

    private TokenBlacklistService tokenBlacklistService;

    @BeforeEach
    void setUp() {
        tokenBlacklistService = new TokenBlacklistService();
    }

    @Test
    void blacklistToken_deveAdicionarTokenABlacklist() {
        String token = "test-token-123";

        tokenBlacklistService.blacklistToken(token);

        assertThat(tokenBlacklistService.isTokenBlacklisted(token)).isTrue();
    }

    @Test
    void isTokenBlacklisted_deveRetornarTrueQuandoTokenEstaNaBlacklist() {
        String token = "test-token-123";
        tokenBlacklistService.blacklistToken(token);

        boolean result = tokenBlacklistService.isTokenBlacklisted(token);

        assertThat(result).isTrue();
    }

    @Test
    void isTokenBlacklisted_deveRetornarFalseQuandoTokenNaoEstaNaBlacklist() {
        String token = "test-token-123";

        boolean result = tokenBlacklistService.isTokenBlacklisted(token);

        assertThat(result).isFalse();
    }

    @Test
    void isTokenBlacklisted_deveRetornarFalseParaTokenDiferente() {
        String token1 = "test-token-123";
        String token2 = "test-token-456";
        tokenBlacklistService.blacklistToken(token1);

        boolean result = tokenBlacklistService.isTokenBlacklisted(token2);

        assertThat(result).isFalse();
    }
}
