package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.exception.BusinessException;
import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
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
class ToggleAdminServiceTest {

    @Mock
    private AdminRepositoryPort adminRepositoryPort;

    @InjectMocks
    private ToggleAdminService toggleAdminService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId("123");
        admin.setNome("Admin Test");
        admin.setAtivo(true);
    }

    @Test
    void deveAtivarAdminExistente() {
        admin.setAtivo(false);
        Admin adminAtivado = new Admin();
        adminAtivado.setId("123");
        adminAtivado.setAtivo(true);

        when(adminRepositoryPort.findById("123")).thenReturn(Optional.of(admin));
        when(adminRepositoryPort.update(any(Admin.class))).thenAnswer(invocation -> {
            Admin updated = invocation.getArgument(0);
            adminAtivado.setAtivo(updated.getAtivo());
            return adminAtivado;
        });

        Admin result = toggleAdminService.toggle("123", true);

        assertThat(result.getAtivo()).isTrue();
        verify(adminRepositoryPort, times(1)).findById("123");
        verify(adminRepositoryPort, times(1)).update(any(Admin.class));
    }

    @Test
    void deveInativarAdminExistente() {
        Admin adminInativado = new Admin();
        adminInativado.setId("123");
        adminInativado.setAtivo(false);

        when(adminRepositoryPort.findById("123")).thenReturn(Optional.of(admin));
        when(adminRepositoryPort.update(any(Admin.class))).thenAnswer(invocation -> {
            Admin updated = invocation.getArgument(0);
            adminInativado.setAtivo(updated.getAtivo());
            return adminInativado;
        });

        Admin result = toggleAdminService.toggle("123", false);

        assertThat(result.getAtivo()).isFalse();
        verify(adminRepositoryPort, times(1)).findById("123");
        verify(adminRepositoryPort, times(1)).update(any(Admin.class));
    }

    @Test
    void deveFalharQuandoAdminNaoExiste() {
        when(adminRepositoryPort.findById("999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> toggleAdminService.toggle("999", true))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Administrador não encontrado");

        verify(adminRepositoryPort, times(1)).findById("999");
        verify(adminRepositoryPort, never()).update(any(Admin.class));
    }
}
