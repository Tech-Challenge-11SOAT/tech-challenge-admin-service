package br.com.postech.techchallange_admin.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenBlacklistAdapterTest {

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @InjectMocks
    private TokenBlacklistAdapter tokenBlacklistAdapter;

    private String token;

    @BeforeEach
    void setUp() {
        token = "test-token-123";
    }

    @Test
    void blacklistToken_deveChamarTokenBlacklistServiceBlacklistToken() {
        tokenBlacklistAdapter.blacklistToken(token);

        verify(tokenBlacklistService, times(1)).blacklistToken(token);
    }

    @Test
    void blacklistToken_deveAdicionarTokenABlacklist() {
        doNothing().when(tokenBlacklistService).blacklistToken(token);
        when(tokenBlacklistService.isTokenBlacklisted(token)).thenReturn(true);

        tokenBlacklistAdapter.blacklistToken(token);
        boolean isBlacklisted = tokenBlacklistAdapter.isTokenBlacklisted(token);

        assertThat(isBlacklisted).isTrue();
        verify(tokenBlacklistService, times(1)).blacklistToken(token);
        verify(tokenBlacklistService, times(1)).isTokenBlacklisted(token);
    }

    @Test
    void blacklistToken_deveProcessarTokenComPrefixoBearer() {
        String tokenWithBearer = "Bearer " + token;
        String tokenWithoutBearer = token;

        tokenBlacklistAdapter.blacklistToken(tokenWithBearer);

        verify(tokenBlacklistService, times(1)).blacklistToken(tokenWithBearer);
    }

    @Test
    void blacklistToken_deveProcessarTokenNull() {
        String nullToken = null;

        tokenBlacklistAdapter.blacklistToken(nullToken);

        verify(tokenBlacklistService, times(1)).blacklistToken(nullToken);
    }

    @Test
    void blacklistToken_deveProcessarTokenVazio() {
        String emptyToken = "";

        tokenBlacklistAdapter.blacklistToken(emptyToken);

        verify(tokenBlacklistService, times(1)).blacklistToken(emptyToken);
    }

    @Test
    void isTokenBlacklisted_deveChamarTokenBlacklistServiceIsTokenBlacklisted() {
        when(tokenBlacklistService.isTokenBlacklisted(token)).thenReturn(true);

        boolean result = tokenBlacklistAdapter.isTokenBlacklisted(token);

        assertThat(result).isTrue();
        verify(tokenBlacklistService, times(1)).isTokenBlacklisted(token);
    }

    @Test
    void isTokenBlacklisted_deveRetornarTrueQuandoTokenEstaNaBlacklist() {
        when(tokenBlacklistService.isTokenBlacklisted(token)).thenReturn(true);

        boolean result = tokenBlacklistAdapter.isTokenBlacklisted(token);

        assertThat(result).isTrue();
        verify(tokenBlacklistService, times(1)).isTokenBlacklisted(token);
    }

    @Test
    void isTokenBlacklisted_deveRetornarFalseQuandoTokenNaoEstaNaBlacklist() {
        when(tokenBlacklistService.isTokenBlacklisted(token)).thenReturn(false);

        boolean result = tokenBlacklistAdapter.isTokenBlacklisted(token);

        assertThat(result).isFalse();
        verify(tokenBlacklistService, times(1)).isTokenBlacklisted(token);
    }

    @Test
    void isTokenBlacklisted_deveRetornarFalseParaTokenDiferente() {
        String differentToken = "different-token-456";
        when(tokenBlacklistService.isTokenBlacklisted(differentToken)).thenReturn(false);

        boolean result = tokenBlacklistAdapter.isTokenBlacklisted(differentToken);

        assertThat(result).isFalse();
        verify(tokenBlacklistService, times(1)).isTokenBlacklisted(differentToken);
    }

    @Test
    void blacklistToken_e_isTokenBlacklisted_deveFuncionarJuntos() {
        String newToken = "new-token-789";
        
        doNothing().when(tokenBlacklistService).blacklistToken(newToken);
        when(tokenBlacklistService.isTokenBlacklisted(newToken))
                .thenReturn(false)
                .thenReturn(true);

        boolean beforeBlacklist = tokenBlacklistAdapter.isTokenBlacklisted(newToken);
        tokenBlacklistAdapter.blacklistToken(newToken);
        boolean afterBlacklist = tokenBlacklistAdapter.isTokenBlacklisted(newToken);

        assertThat(beforeBlacklist).isFalse();
        assertThat(afterBlacklist).isTrue();
        verify(tokenBlacklistService, times(1)).blacklistToken(newToken);
        verify(tokenBlacklistService, times(2)).isTokenBlacklisted(newToken);
    }

    @Test
    void isTokenBlacklisted_deveProcessarTokenNull() {
        String nullToken = null;
        when(tokenBlacklistService.isTokenBlacklisted(nullToken)).thenReturn(false);

        boolean result = tokenBlacklistAdapter.isTokenBlacklisted(nullToken);

        assertThat(result).isFalse();
        verify(tokenBlacklistService, times(1)).isTokenBlacklisted(nullToken);
    }
}
