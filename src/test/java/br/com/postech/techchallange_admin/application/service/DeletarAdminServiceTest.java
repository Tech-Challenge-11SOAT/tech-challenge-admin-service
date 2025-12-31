package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarAdminServiceTest {

    @Mock
    private AdminRepositoryPort adminRepositoryPort;

    @InjectMocks
    private DeletarAdminService deletarAdminService;

    @Test
    void deveDeletarAdminPorId() {
        String id = "123";

        deletarAdminService.deletar(id);

        verify(adminRepositoryPort, times(1)).deleteById(id);
    }

    @Test
    void deveVerificarChamadaAoRepository() {
        String id = "123";

        deletarAdminService.deletar(id);

        verify(adminRepositoryPort, times(1)).deleteById(id);
        verifyNoMoreInteractions(adminRepositoryPort);
    }
}
