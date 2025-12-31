package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarAdminServiceTest {

    @Mock
    private AdminRepositoryPort adminRepositoryPort;

    @InjectMocks
    private AtualizarAdminService atualizarAdminService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId("123");
        admin.setNome("Admin Test");
        admin.setEmail("admin@test.com");
    }

    @Test
    void deveAtualizarAdminComSucesso() {
        Admin adminAtualizado = new Admin();
        adminAtualizado.setId("123");
        adminAtualizado.setNome("Admin Atualizado");
        adminAtualizado.setEmail("admin@test.com");

        when(adminRepositoryPort.update(admin)).thenReturn(adminAtualizado);

        Admin result = atualizarAdminService.atualizar(admin);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("Admin Atualizado");
        verify(adminRepositoryPort, times(1)).update(admin);
    }

    @Test
    void deveVerificarChamadaAoRepository() {
        when(adminRepositoryPort.update(admin)).thenReturn(admin);

        atualizarAdminService.atualizar(admin);

        verify(adminRepositoryPort, times(1)).update(admin);
    }
}
