package br.com.postech.techchallange_admin.application.service;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarAdminServiceTest {

    @Mock
    private AdminRepositoryPort adminRepositoryPort;

    @InjectMocks
    private BuscarAdminService buscarAdminService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId("123");
        admin.setNome("Admin Test");
        admin.setEmail("admin@test.com");
    }

    @Test
    void buscarPorId_deveRetornarAdminQuandoExiste() {
        when(adminRepositoryPort.findById("123")).thenReturn(Optional.of(admin));

        Optional<Admin> result = buscarAdminService.buscarPorId("123");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("123");
        verify(adminRepositoryPort, times(1)).findById("123");
    }

    @Test
    void buscarPorId_deveRetornarEmptyQuandoNaoExiste() {
        when(adminRepositoryPort.findById("999")).thenReturn(Optional.empty());

        Optional<Admin> result = buscarAdminService.buscarPorId("999");

        assertThat(result).isEmpty();
        verify(adminRepositoryPort, times(1)).findById("999");
    }

    @Test
    void buscarPorEmail_deveRetornarAdminQuandoExiste() {
        String email = "admin@test.com";
        when(adminRepositoryPort.findByEmail(email)).thenReturn(Optional.of(admin));

        Optional<Admin> result = buscarAdminService.buscarPorEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
        verify(adminRepositoryPort, times(1)).findByEmail(email);
    }

    @Test
    void buscarPorEmail_deveRetornarEmptyQuandoNaoExiste() {
        String email = "notfound@test.com";
        when(adminRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

        Optional<Admin> result = buscarAdminService.buscarPorEmail(email);

        assertThat(result).isEmpty();
        verify(adminRepositoryPort, times(1)).findByEmail(email);
    }
}
