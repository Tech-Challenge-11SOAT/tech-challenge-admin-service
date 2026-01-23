package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarAdminsServiceTest {

    @Mock
    private AdminRepositoryPort adminRepositoryPort;

    @InjectMocks
    private ListarAdminsService listarAdminsService;

    private Admin admin1;
    private Admin admin2;

    @BeforeEach
    void setUp() {
        admin1 = new Admin();
        admin1.setId("1");
        admin1.setNome("Admin 1");

        admin2 = new Admin();
        admin2.setId("2");
        admin2.setNome("Admin 2");
    }

    @Test
    void deveListarTodosOsAdmins() {
        when(adminRepositoryPort.findAll()).thenReturn(List.of(admin1, admin2));

        List<Admin> result = listarAdminsService.listarTodos();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("1");
        assertThat(result.get(1).getId()).isEqualTo("2");
        verify(adminRepositoryPort, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaAdmins() {
        when(adminRepositoryPort.findAll()).thenReturn(List.of());

        List<Admin> result = listarAdminsService.listarTodos();

        assertThat(result).isEmpty();
        verify(adminRepositoryPort, times(1)).findAll();
    }
}
