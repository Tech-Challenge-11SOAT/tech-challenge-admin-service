package br.com.postech.techchallange_admin.infrastructure.persistence.mapper;

import br.com.postech.techchallange_admin.domain.model.AdminLogAcao;
import br.com.postech.techchallange_admin.infrastructure.persistence.entity.AdminLogAcaoEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AdminLogAcaoMapperTest {

    @Test
    void toEntity_deveConverterDomainParaEntity() {
        AdminLogAcaoMapper mapper = new AdminLogAcaoMapper();
        
        AdminLogAcao domain = new AdminLogAcao();
        domain.setId("123");
        domain.setIdAdmin("admin123");
        domain.setAcao("CADASTRO");
        domain.setRecursoAfetado("ADMIN_USER");
        domain.setIdRecursoAfetado("user123");
        LocalDateTime dataAcao = LocalDateTime.now();
        domain.setDataAcao(dataAcao);

        AdminLogAcaoEntity entity = mapper.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(domain.getId());
        assertThat(entity.getIdAdmin()).isEqualTo(domain.getIdAdmin());
        assertThat(entity.getAcao()).isEqualTo(domain.getAcao());
        assertThat(entity.getRecursoAfetado()).isEqualTo(domain.getRecursoAfetado());
        assertThat(entity.getIdRecursoAfetado()).isEqualTo(domain.getIdRecursoAfetado());
        assertThat(entity.getDataAcao()).isEqualTo(domain.getDataAcao());
    }

    @Test
    void toEntity_deveRetornarNullQuandoDomainENull() {
        AdminLogAcaoMapper mapper = new AdminLogAcaoMapper();
        
        AdminLogAcaoEntity entity = mapper.toEntity(null);

        assertThat(entity).isNull();
    }
}
