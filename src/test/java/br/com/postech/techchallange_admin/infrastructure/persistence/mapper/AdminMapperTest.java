package br.com.postech.techchallange_admin.infrastructure.persistence.mapper;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.infrastructure.persistence.entity.AdminEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AdminMapperTest {

    @Test
    void toDomain_deveConverterEntityParaDomain() {
        AdminEntity entity = new AdminEntity();
        entity.setId("123");
        entity.setNome("Admin Test");
        entity.setEmail("admin@test.com");
        entity.setSenhaHash("hash123");
        entity.setAtivo(true);
        LocalDateTime dataCriacao = LocalDateTime.now();
        entity.setDataCriacao(dataCriacao);
        List<String> roles = List.of("ADMIN");
        entity.setRoles(roles);

        Admin admin = AdminMapper.toDomain(entity);

        assertThat(admin).isNotNull();
        assertThat(admin.getId()).isEqualTo(entity.getId());
        assertThat(admin.getNome()).isEqualTo(entity.getNome());
        assertThat(admin.getEmail()).isEqualTo(entity.getEmail());
        assertThat(admin.getSenhaHash()).isEqualTo(entity.getSenhaHash());
        assertThat(admin.getAtivo()).isEqualTo(entity.getAtivo());
        assertThat(admin.getDataCriacao()).isEqualTo(entity.getDataCriacao());
        assertThat(admin.getRoles()).isEqualTo(entity.getRoles());
    }

    @Test
    void toDomain_deveRetornarNullQuandoEntityENull() {
        Admin admin = AdminMapper.toDomain(null);

        assertThat(admin).isNull();
    }

    @Test
    void toEntity_deveConverterDomainParaEntity() {
        Admin admin = new Admin();
        admin.setId("123");
        admin.setNome("Admin Test");
        admin.setEmail("admin@test.com");
        admin.setSenhaHash("hash123");
        admin.setAtivo(true);
        LocalDateTime dataCriacao = LocalDateTime.now();
        admin.setDataCriacao(dataCriacao);
        List<String> roles = List.of("ADMIN");
        admin.setRoles(roles);

        AdminEntity entity = AdminMapper.toEntity(admin);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(admin.getId());
        assertThat(entity.getNome()).isEqualTo(admin.getNome());
        assertThat(entity.getEmail()).isEqualTo(admin.getEmail());
        assertThat(entity.getSenhaHash()).isEqualTo(admin.getSenhaHash());
        assertThat(entity.getAtivo()).isEqualTo(admin.getAtivo());
        assertThat(entity.getDataCriacao()).isEqualTo(admin.getDataCriacao());
        assertThat(entity.getRoles()).isEqualTo(admin.getRoles());
        assertThat(entity.getDataUltimaAtualizacao()).isNotNull();
    }

    @Test
    void toEntity_deveRetornarNullQuandoDomainENull() {
        AdminEntity entity = AdminMapper.toEntity(null);

        assertThat(entity).isNull();
    }

    @Test
    void toEntity_deveDefinirDataUltimaAtualizacao() {
        Admin admin = new Admin();
        admin.setId("123");
        admin.setNome("Admin Test");

        AdminEntity entity = AdminMapper.toEntity(admin);

        assertThat(entity.getDataUltimaAtualizacao()).isNotNull();
        assertThat(entity.getDataUltimaAtualizacao()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void toDomainList_deveConverterListaDeEntities() {
        AdminEntity entity1 = new AdminEntity();
        entity1.setId("1");
        entity1.setNome("Admin 1");

        AdminEntity entity2 = new AdminEntity();
        entity2.setId("2");
        entity2.setNome("Admin 2");

        List<AdminEntity> entities = List.of(entity1, entity2);
        List<Admin> admins = AdminMapper.toDomainList(entities);

        assertThat(admins).hasSize(2);
        assertThat(admins.get(0).getId()).isEqualTo("1");
        assertThat(admins.get(1).getId()).isEqualTo("2");
    }

    @Test
    void toDomainList_deveRetornarListaVaziaQuandoNull() {
        List<Admin> admins = AdminMapper.toDomainList(null);

        assertThat(admins).isNotNull();
        assertThat(admins).isEmpty();
    }

    @Test
    void toEntityList_deveConverterListaDeDomains() {
        Admin admin1 = new Admin();
        admin1.setId("1");
        admin1.setNome("Admin 1");

        Admin admin2 = new Admin();
        admin2.setId("2");
        admin2.setNome("Admin 2");

        List<Admin> admins = List.of(admin1, admin2);
        List<AdminEntity> entities = AdminMapper.toEntityList(admins);

        assertThat(entities).hasSize(2);
        assertThat(entities.get(0).getId()).isEqualTo("1");
        assertThat(entities.get(1).getId()).isEqualTo("2");
    }

    @Test
    void toEntityList_deveRetornarListaVaziaQuandoNull() {
        List<AdminEntity> entities = AdminMapper.toEntityList(null);

        assertThat(entities).isNotNull();
        assertThat(entities).isEmpty();
    }
}
