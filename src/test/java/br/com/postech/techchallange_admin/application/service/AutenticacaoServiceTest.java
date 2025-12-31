package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.exception.BusinessException;
import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import br.com.postech.techchallange_admin.domain.port.out.PasswordEncoderPort;
import br.com.postech.techchallange_admin.domain.port.out.TokenBlacklistPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

    @Mock
    private AdminRepositoryPort adminRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private TokenBlacklistPort tokenBlacklistPort;

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    private Admin admin;
    private String email = "admin@test.com";
    private String senha = "senha123";
    private String senhaHash = "$2a$10$hash";

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId("123");
        admin.setEmail(email);
        admin.setSenhaHash(senhaHash);
    }

    @Test
    void deveAutenticarComCredenciaisValidas() {
        when(adminRepositoryPort.findByEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoderPort.matches(senha, senhaHash)).thenReturn(true);

        Admin result = autenticacaoService.autenticar(email, senha);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        verify(adminRepositoryPort, times(1)).findByEmail(email);
        verify(passwordEncoderPort, times(1)).matches(senha, senhaHash);
    }

    @Test
    void deveFalharQuandoEmailNaoExiste() {
        when(adminRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autenticacaoService.autenticar(email, senha))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Usuário ou senha inválidos");

        verify(adminRepositoryPort, times(1)).findByEmail(email);
        verify(passwordEncoderPort, never()).matches(anyString(), anyString());
    }

    @Test
    void deveFalharQuandoSenhaEstaIncorreta() {
        when(adminRepositoryPort.findByEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoderPort.matches(senha, senhaHash)).thenReturn(false);

        assertThatThrownBy(() -> autenticacaoService.autenticar(email, senha))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Usuário ou senha inválidos");

        verify(adminRepositoryPort, times(1)).findByEmail(email);
        verify(passwordEncoderPort, times(1)).matches(senha, senhaHash);
    }

    @Test
    void logout_deveProcessarTokenComPrefixoBearer() {
        String token = "Bearer token123";
        
        autenticacaoService.logout(token);

        verify(tokenBlacklistPort, times(1)).blacklistToken("token123");
    }

    @Test
    void logout_deveProcessarTokenSemPrefixoBearer() {
        String token = "token123";
        
        autenticacaoService.logout(token);

        verify(tokenBlacklistPort, times(1)).blacklistToken("token123");
    }

    @Test
    void logout_deveProcessarTokenNull() {
        autenticacaoService.logout(null);

        verify(tokenBlacklistPort, times(1)).blacklistToken(null);
    }
}
