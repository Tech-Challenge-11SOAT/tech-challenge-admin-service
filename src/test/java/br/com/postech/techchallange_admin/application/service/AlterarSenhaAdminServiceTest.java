package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.exception.BusinessException;
import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.LogAdminActionUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import br.com.postech.techchallange_admin.domain.port.out.PasswordEncoderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlterarSenhaAdminServiceTest {

    @Mock
    private AdminRepositoryPort adminRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private LogAdminActionUseCase logAdminActionUseCase;

    @InjectMocks
    private AlterarSenhaAdminService alterarSenhaAdminService;

    private Admin admin;
    private String email = "admin@test.com";
    private String senhaAtual = "senhaAtual123";
    private String senhaHash = "$2a$10$hash";

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId("123");
        admin.setEmail(email);
        admin.setSenhaHash(senhaHash);
    }

    @Test
    void deveAlterarSenhaComSucesso() {
        String novaSenha = "novaSenha123";
        String novaSenhaHash = "$2a$10$newHash";

        when(adminRepositoryPort.findByEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoderPort.matches(senhaAtual, senhaHash)).thenReturn(true);
        when(passwordEncoderPort.encode(novaSenha)).thenReturn(novaSenhaHash);

        alterarSenhaAdminService.alterarSenha(email, senhaAtual, novaSenha);

        verify(adminRepositoryPort, times(1)).findByEmail(email);
        verify(passwordEncoderPort, times(1)).matches(senhaAtual, senhaHash);
        verify(passwordEncoderPort, times(1)).encode(novaSenha);
        verify(adminRepositoryPort, times(1)).update(any(Admin.class));
        verify(logAdminActionUseCase, times(1)).registrar(
                eq("123"),
                eq("ALTERACAO_SENHA"),
                eq("ADMIN_USER"),
                eq("123")
        );
    }

    @Test
    void deveFalharQuandoAdminNaoExiste() {
        when(adminRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> alterarSenhaAdminService.alterarSenha(email, senhaAtual, "novaSenha123"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Administrador não encontrado.");

        verify(adminRepositoryPort, times(1)).findByEmail(email);
        verify(passwordEncoderPort, never()).matches(anyString(), anyString());
    }

    @Test
    void deveFalharQuandoSenhaAtualEstaIncorreta() {
        when(adminRepositoryPort.findByEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoderPort.matches(senhaAtual, senhaHash)).thenReturn(false);

        assertThatThrownBy(() -> alterarSenhaAdminService.alterarSenha(email, senhaAtual, "novaSenha123"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Senha atual incorreta.");

        verify(adminRepositoryPort, times(1)).findByEmail(email);
        verify(passwordEncoderPort, times(1)).matches(senhaAtual, senhaHash);
        verify(adminRepositoryPort, never()).update(any(Admin.class));
    }

    @Test
    void deveFalharQuandoNovaSenhaEIgualAAtual() {
        when(adminRepositoryPort.findByEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoderPort.matches(senhaAtual, senhaHash)).thenReturn(true);

        assertThatThrownBy(() -> alterarSenhaAdminService.alterarSenha(email, senhaAtual, senhaAtual))
                .isInstanceOf(BusinessException.class)
                .hasMessage("A nova senha não pode ser igual à senha atual.");

        verify(adminRepositoryPort, times(1)).findByEmail(email);
        verify(passwordEncoderPort, times(1)).matches(senhaAtual, senhaHash);
        verify(adminRepositoryPort, never()).update(any(Admin.class));
    }

    @Test
    void deveFalharQuandoNovaSenhaTemMenosDe8Caracteres() {
        String novaSenha = "abc123";
        when(adminRepositoryPort.findByEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoderPort.matches(senhaAtual, senhaHash)).thenReturn(true);

        assertThatThrownBy(() -> alterarSenhaAdminService.alterarSenha(email, senhaAtual, novaSenha))
                .isInstanceOf(BusinessException.class)
                .hasMessage("A nova senha deve conter pelo menos 8 caracteres, letras e números.");

        verify(adminRepositoryPort, times(1)).findByEmail(email);
        verify(passwordEncoderPort, times(1)).matches(senhaAtual, senhaHash);
        verify(adminRepositoryPort, never()).update(any(Admin.class));
    }

    @Test
    void deveFalharQuandoNovaSenhaNaoTemNumeros() {
        String novaSenha = "senhasemnumero";
        when(adminRepositoryPort.findByEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoderPort.matches(senhaAtual, senhaHash)).thenReturn(true);

        assertThatThrownBy(() -> alterarSenhaAdminService.alterarSenha(email, senhaAtual, novaSenha))
                .isInstanceOf(BusinessException.class)
                .hasMessage("A nova senha deve conter pelo menos 8 caracteres, letras e números.");

        verify(adminRepositoryPort, times(1)).findByEmail(email);
        verify(passwordEncoderPort, times(1)).matches(senhaAtual, senhaHash);
        verify(adminRepositoryPort, never()).update(any(Admin.class));
    }

    @Test
    void deveFalharQuandoNovaSenhaNaoTemLetras() {
        String novaSenha = "12345678";
        when(adminRepositoryPort.findByEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoderPort.matches(senhaAtual, senhaHash)).thenReturn(true);

        assertThatThrownBy(() -> alterarSenhaAdminService.alterarSenha(email, senhaAtual, novaSenha))
                .isInstanceOf(BusinessException.class)
                .hasMessage("A nova senha deve conter pelo menos 8 caracteres, letras e números.");

        verify(adminRepositoryPort, times(1)).findByEmail(email);
        verify(passwordEncoderPort, times(1)).matches(senhaAtual, senhaHash);
        verify(adminRepositoryPort, never()).update(any(Admin.class));
    }
}
