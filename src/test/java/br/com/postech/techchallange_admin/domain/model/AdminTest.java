package br.com.postech.techchallange_admin.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AdminTest {

    @Test
    void deveCriarAdminComConstrutorPadrao() {
        Admin admin = new Admin();
        
        assertThat(admin).isNotNull();
        assertThat(admin.getId()).isNull();
        assertThat(admin.getNome()).isNull();
        assertThat(admin.getEmail()).isNull();
    }

    @Test
    void deveCriarAdminComConstrutorComParametros() {
        String id = "123";
        String nome = "Admin Test";
        String email = "admin@test.com";
        String senhaHash = "hash123";
        Boolean ativo = true;
        LocalDateTime dataCriacao = LocalDateTime.now();
        List<String> roles = List.of("ADMIN");

        Admin admin = new Admin(id, nome, email, senhaHash, ativo, dataCriacao, roles);

        assertThat(admin.getId()).isEqualTo(id);
        assertThat(admin.getNome()).isEqualTo(nome);
        assertThat(admin.getEmail()).isEqualTo(email);
        assertThat(admin.getSenhaHash()).isEqualTo(senhaHash);
        assertThat(admin.getAtivo()).isEqualTo(ativo);
        assertThat(admin.getDataCriacao()).isEqualTo(dataCriacao);
        assertThat(admin.getRoles()).isEqualTo(roles);
    }

    @Test
    void deveTestarGettersESetters() {
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

        assertThat(admin.getId()).isEqualTo("123");
        assertThat(admin.getNome()).isEqualTo("Admin Test");
        assertThat(admin.getEmail()).isEqualTo("admin@test.com");
        assertThat(admin.getSenhaHash()).isEqualTo("hash123");
        assertThat(admin.getAtivo()).isTrue();
        assertThat(admin.getDataCriacao()).isEqualTo(dataCriacao);
        assertThat(admin.getRoles()).isEqualTo(roles);
    }

    @Test
    void semSenha_deveRetornarCopiaSemSenha() {
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

        Admin adminSemSenha = admin.semSenha();

        assertThat(adminSemSenha).isNotNull();
        assertThat(adminSemSenha.getId()).isEqualTo(admin.getId());
        assertThat(adminSemSenha.getNome()).isEqualTo(admin.getNome());
        assertThat(adminSemSenha.getEmail()).isEqualTo(admin.getEmail());
        assertThat(adminSemSenha.getSenhaHash()).isNull();
        assertThat(adminSemSenha.getAtivo()).isEqualTo(admin.getAtivo());
        assertThat(adminSemSenha.getDataCriacao()).isEqualTo(admin.getDataCriacao());
        assertThat(adminSemSenha.getRoles()).isEqualTo(admin.getRoles());
        assertThat(adminSemSenha).isNotSameAs(admin);
    }
}
