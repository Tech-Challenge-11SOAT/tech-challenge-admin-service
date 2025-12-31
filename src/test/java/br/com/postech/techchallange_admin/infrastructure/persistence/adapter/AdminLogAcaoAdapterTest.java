package br.com.postech.techchallange_admin.infrastructure.persistence.adapter;

import br.com.postech.techchallange_admin.domain.model.AdminLogAcao;
import br.com.postech.techchallange_admin.infrastructure.persistence.entity.AdminLogAcaoEntity;
import br.com.postech.techchallange_admin.infrastructure.persistence.mapper.AdminLogAcaoMapper;
import br.com.postech.techchallange_admin.infrastructure.persistence.repository.AdminLogAcaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminLogAcaoAdapterTest {

    @Mock
    private AdminLogAcaoRepository repository;

    @Mock
    private AdminLogAcaoMapper mapper;

    @InjectMocks
    private AdminLogAcaoAdapter adminLogAcaoAdapter;

    private AdminLogAcao log;
    private AdminLogAcaoEntity entity;

    @BeforeEach
    void setUp() {
        log = new AdminLogAcao();
        log.setId("log123");
        log.setIdAdmin("admin123");
        log.setAcao("CADASTRO");
        log.setRecursoAfetado("ADMIN_USER");
        log.setIdRecursoAfetado("user123");
        log.setDataAcao(LocalDateTime.now());

        entity = new AdminLogAcaoEntity();
        entity.setId("log123");
        entity.setIdAdmin("admin123");
        entity.setAcao("CADASTRO");
        entity.setRecursoAfetado("ADMIN_USER");
        entity.setIdRecursoAfetado("user123");
        entity.setDataAcao(LocalDateTime.now());
    }

    @Test
    void registrarLog_deveSalvarLogNoRepository() {
        when(mapper.toEntity(log)).thenReturn(entity);
        when(repository.save(any(AdminLogAcaoEntity.class))).thenReturn(entity);

        adminLogAcaoAdapter.registrarLog(log);

        verify(mapper, times(1)).toEntity(log);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void registrarLog_deveConverterDomainParaEntityAntesDeSalvar() {
        when(mapper.toEntity(log)).thenReturn(entity);
        when(repository.save(any(AdminLogAcaoEntity.class))).thenReturn(entity);

        adminLogAcaoAdapter.registrarLog(log);

        verify(mapper, times(1)).toEntity(log);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void registrarLog_deveProcessarLogComTodosOsCampos() {
        when(mapper.toEntity(log)).thenReturn(entity);
        when(repository.save(any(AdminLogAcaoEntity.class))).thenReturn(entity);

        adminLogAcaoAdapter.registrarLog(log);

        verify(mapper, times(1)).toEntity(log);
        verify(repository, times(1)).save(any(AdminLogAcaoEntity.class));
    }

    @Test
    void registrarLog_deveProcessarLogQuandoEntityRetornadoPeloMapper() {
        when(mapper.toEntity(log)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        adminLogAcaoAdapter.registrarLog(log);

        verify(mapper, times(1)).toEntity(log);
        verify(repository, times(1)).save(entity);
    }
}
