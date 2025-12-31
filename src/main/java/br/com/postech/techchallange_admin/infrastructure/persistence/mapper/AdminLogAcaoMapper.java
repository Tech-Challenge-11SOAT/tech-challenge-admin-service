package br.com.postech.techchallange_admin.infrastructure.persistence.mapper;

import br.com.postech.techchallange_admin.domain.model.AdminLogAcao;
import br.com.postech.techchallange_admin.infrastructure.persistence.entity.AdminLogAcaoEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre AdminLogAcao (Domain) e AdminLogAcaoEntity (JPA)
 */
@Component
public class AdminLogAcaoMapper {

    public AdminLogAcaoEntity toEntity(AdminLogAcao domain) {
        if (domain == null) return null;

        AdminLogAcaoEntity entity = new AdminLogAcaoEntity();
        entity.setId(domain.getId());
        entity.setIdAdmin(domain.getIdAdmin());
        entity.setAcao(domain.getAcao());
        entity.setRecursoAfetado(domain.getRecursoAfetado());
        entity.setIdRecursoAfetado(domain.getIdRecursoAfetado());
        entity.setDataAcao(domain.getDataAcao());
        return entity;
    }
}
