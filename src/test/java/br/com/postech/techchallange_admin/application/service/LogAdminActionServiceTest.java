package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.model.AdminLogAcao;
import br.com.postech.techchallange_admin.domain.port.out.AdminLogAcaoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogAdminActionServiceTest {

    @Mock
    private AdminLogAcaoRepositoryPort repository;

    @InjectMocks
    private LogAdminActionService logAdminActionService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void deveRegistrarLogComTodosOsCampos() {
        String idAdmin = "123";
        String acao = "CADASTRO";
        String recursoAfetado = "ADMIN_USER";
        String idRecursoAfetado = "456";

        ArgumentCaptor<AdminLogAcao> logCaptor = ArgumentCaptor.forClass(AdminLogAcao.class);

        logAdminActionService.registrar(idAdmin, acao, recursoAfetado, idRecursoAfetado);

        verify(repository, times(1)).registrarLog(logCaptor.capture());
        AdminLogAcao log = logCaptor.getValue();
        assertThat(log.getIdAdmin()).isEqualTo(idAdmin);
        assertThat(log.getAcao()).isEqualTo(acao);
        assertThat(log.getRecursoAfetado()).isEqualTo(recursoAfetado);
        assertThat(log.getIdRecursoAfetado()).isEqualTo(idRecursoAfetado);
        assertThat(log.getDataAcao()).isNotNull();
    }

    @Test
    void deveVerificarDataHoraDaAcao() {
        ArgumentCaptor<AdminLogAcao> logCaptor = ArgumentCaptor.forClass(AdminLogAcao.class);
        LocalDateTime antes = LocalDateTime.now();

        logAdminActionService.registrar("123", "CADASTRO", "ADMIN_USER", "456");

        verify(repository, times(1)).registrarLog(logCaptor.capture());
        AdminLogAcao log = logCaptor.getValue();
        LocalDateTime depois = LocalDateTime.now();
        
        assertThat(log.getDataAcao()).isNotNull();
        assertThat(log.getDataAcao()).isAfterOrEqualTo(antes);
        assertThat(log.getDataAcao()).isBeforeOrEqualTo(depois);
    }
}
