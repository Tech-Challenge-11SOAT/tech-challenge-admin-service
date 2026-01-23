package br.com.postech.techchallange_admin.infrastructure.persistence.mapper;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.infrastructure.persistence.entity.AdminEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper responsável pela conversão entre Admin (Domain) e AdminEntity (JPA)
 */
@Component
public class AdminMapper {

    public static Admin toDomain(AdminEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Admin(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getSenhaHash(),
                entity.getAtivo(),
                entity.getDataCriacao(),
                entity.getRoles()
        );
    }

    public static AdminEntity toEntity(Admin admin) {
        if (admin == null) {
            return null;
        }

        AdminEntity entity = new AdminEntity();
        entity.setId(admin.getId());
        entity.setNome(admin.getNome());
        entity.setEmail(admin.getEmail());
        entity.setSenhaHash(admin.getSenhaHash());
        entity.setAtivo(admin.getAtivo());
        entity.setDataCriacao(admin.getDataCriacao());
        entity.setRoles(admin.getRoles());
        entity.setDataUltimaAtualizacao(java.time.LocalDateTime.now());

        return entity;
    }

    public static List<Admin> toDomainList(List<AdminEntity> entities) {
        return entities == null ? List.of() : entities.stream().map(AdminMapper::toDomain).toList();
    }

    public static List<AdminEntity> toEntityList(List<Admin> admins) {
        return admins == null ? List.of() : admins.stream().map(AdminMapper::toEntity).toList();
    }
}
