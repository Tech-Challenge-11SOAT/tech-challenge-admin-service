package br.com.postech.techchallange_admin.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AdminEntityTest {

    @Test
    void deveCriarAdminEntityComConstrutorPadrao() {
        AdminEntity entity = new AdminEntity();

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getNome()).isNull();
        assertThat(entity.getEmail()).isNull();
        assertThat(entity.getSenhaHash()).isNull();
        assertThat(entity.getRoles()).isNull();
        assertThat(entity.getAtivo()).isNull();
        assertThat(entity.getDataCriacao()).isNull();
        assertThat(entity.getDataUltimaAtualizacao()).isNull();
        assertThat(entity.getVersao()).isNull();
    }

    @Test
    void deveCriarAdminEntityComConstrutorComParametros() {
        String id = "123";
        String nome = "Admin Test";
        String email = "admin@test.com";
        String senhaHash = "hash123";
        List<String> roles = List.of("ADMIN", "USER");
        Boolean ativo = true;
        LocalDateTime dataCriacao = LocalDateTime.now();
        LocalDateTime dataUltimaAtualizacao = LocalDateTime.now().plusHours(1);

        AdminEntity entity = new AdminEntity(
                id, nome, email, senhaHash, roles, ativo, dataCriacao, dataUltimaAtualizacao
        );

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getNome()).isEqualTo(nome);
        assertThat(entity.getEmail()).isEqualTo(email);
        assertThat(entity.getSenhaHash()).isEqualTo(senhaHash);
        assertThat(entity.getRoles()).isEqualTo(roles);
        assertThat(entity.getAtivo()).isEqualTo(ativo);
        assertThat(entity.getDataCriacao()).isEqualTo(dataCriacao);
        assertThat(entity.getDataUltimaAtualizacao()).isEqualTo(dataUltimaAtualizacao);
        assertThat(entity.getVersao()).isEqualTo(0);
    }

    @Test
    void deveTestarGettersESetters() {
        AdminEntity entity = new AdminEntity();

        entity.setId("123");
        entity.setNome("Admin Test");
        entity.setEmail("admin@test.com");
        entity.setSenhaHash("hash123");
        List<String> roles = List.of("ADMIN");
        entity.setRoles(roles);
        entity.setAtivo(true);
        LocalDateTime dataCriacao = LocalDateTime.now();
        entity.setDataCriacao(dataCriacao);
        LocalDateTime dataUltimaAtualizacao = LocalDateTime.now();
        entity.setDataUltimaAtualizacao(dataUltimaAtualizacao);
        entity.setVersao(1);

        assertThat(entity.getId()).isEqualTo("123");
        assertThat(entity.getNome()).isEqualTo("Admin Test");
        assertThat(entity.getEmail()).isEqualTo("admin@test.com");
        assertThat(entity.getSenhaHash()).isEqualTo("hash123");
        assertThat(entity.getRoles()).isEqualTo(roles);
        assertThat(entity.getAtivo()).isTrue();
        assertThat(entity.getDataCriacao()).isEqualTo(dataCriacao);
        assertThat(entity.getDataUltimaAtualizacao()).isEqualTo(dataUltimaAtualizacao);
        assertThat(entity.getVersao()).isEqualTo(1);
    }

    @Test
    void getVersao_deveRetornarVersao() {
        AdminEntity entity = new AdminEntity();
        entity.setVersao(5);

        Integer versao = entity.getVersao();

        assertThat(versao).isEqualTo(5);
    }

    @Test
    void setVersao_deveDefinirVersao() {
        AdminEntity entity = new AdminEntity();

        entity.setVersao(10);

        assertThat(entity.getVersao()).isEqualTo(10);
    }

    @Test
    void getVersao_deveRetornarNullQuandoNaoDefinido() {
        AdminEntity entity = new AdminEntity();

        Integer versao = entity.getVersao();

        assertThat(versao).isNull();
    }

    @Test
    void setVersao_deveAceitarNull() {
        AdminEntity entity = new AdminEntity();
        entity.setVersao(5);

        entity.setVersao(null);

        assertThat(entity.getVersao()).isNull();
    }

    @Test
    void construtorComParametros_deveInicializarVersaoComZero() {
        String id = "123";
        String nome = "Admin Test";
        String email = "admin@test.com";
        String senhaHash = "hash123";
        List<String> roles = List.of("ADMIN");
        Boolean ativo = true;
        LocalDateTime dataCriacao = LocalDateTime.now();
        LocalDateTime dataUltimaAtualizacao = LocalDateTime.now();

        AdminEntity entity = new AdminEntity(
                id, nome, email, senhaHash, roles, ativo, dataCriacao, dataUltimaAtualizacao
        );

        assertThat(entity.getVersao()).isEqualTo(0);
    }
}
