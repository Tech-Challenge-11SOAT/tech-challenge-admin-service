package br.com.postech.techchallange_admin.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AdminLogAcaoEntityTest {

    @Test
    void deveCriarAdminLogAcaoEntityComConstrutorPadrao() {
        AdminLogAcaoEntity entity = new AdminLogAcaoEntity();

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getIdAdmin()).isNull();
        assertThat(entity.getAcao()).isNull();
        assertThat(entity.getRecursoAfetado()).isNull();
        assertThat(entity.getIdRecursoAfetado()).isNull();
        assertThat(entity.getDataAcao()).isNull();
    }

    @Test
    void deveCriarAdminLogAcaoEntityComConstrutorComParametros() {
        String id = "log123";
        String idAdmin = "admin123";
        String acao = "CADASTRO";
        String recursoAfetado = "ADMIN_USER";
        String idRecursoAfetado = "user123";
        LocalDateTime dataAcao = LocalDateTime.now();

        AdminLogAcaoEntity entity = new AdminLogAcaoEntity(
                id, idAdmin, acao, recursoAfetado, idRecursoAfetado, dataAcao
        );

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getIdAdmin()).isEqualTo(idAdmin);
        assertThat(entity.getAcao()).isEqualTo(acao);
        assertThat(entity.getRecursoAfetado()).isEqualTo(recursoAfetado);
        assertThat(entity.getIdRecursoAfetado()).isEqualTo(idRecursoAfetado);
        assertThat(entity.getDataAcao()).isEqualTo(dataAcao);
    }

    @Test
    void deveTestarGettersESetters() {
        AdminLogAcaoEntity entity = new AdminLogAcaoEntity();

        entity.setId("log123");
        entity.setIdAdmin("admin123");
        entity.setAcao("CADASTRO");
        entity.setRecursoAfetado("ADMIN_USER");
        entity.setIdRecursoAfetado("user123");
        LocalDateTime dataAcao = LocalDateTime.now();
        entity.setDataAcao(dataAcao);

        assertThat(entity.getId()).isEqualTo("log123");
        assertThat(entity.getIdAdmin()).isEqualTo("admin123");
        assertThat(entity.getAcao()).isEqualTo("CADASTRO");
        assertThat(entity.getRecursoAfetado()).isEqualTo("ADMIN_USER");
        assertThat(entity.getIdRecursoAfetado()).isEqualTo("user123");
        assertThat(entity.getDataAcao()).isEqualTo(dataAcao);
    }

    @Test
    void construtorComParametros_deveInicializarTodosOsCampos() {
        String id = "log456";
        String idAdmin = "admin456";
        String acao = "ATUALIZACAO";
        String recursoAfetado = "PRODUTO";
        String idRecursoAfetado = "prod123";
        LocalDateTime dataAcao = LocalDateTime.now().minusHours(1);

        AdminLogAcaoEntity entity = new AdminLogAcaoEntity(
                id, idAdmin, acao, recursoAfetado, idRecursoAfetado, dataAcao
        );

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getIdAdmin()).isEqualTo(idAdmin);
        assertThat(entity.getAcao()).isEqualTo(acao);
        assertThat(entity.getRecursoAfetado()).isEqualTo(recursoAfetado);
        assertThat(entity.getIdRecursoAfetado()).isEqualTo(idRecursoAfetado);
        assertThat(entity.getDataAcao()).isEqualTo(dataAcao);
    }

    @Test
    void construtorComParametros_deveAceitarIdRecursoAfetadoNull() {
        String id = "log789";
        String idAdmin = "admin789";
        String acao = "EXCLUSAO";
        String recursoAfetado = "PEDIDO";
        String idRecursoAfetado = null;
        LocalDateTime dataAcao = LocalDateTime.now();

        AdminLogAcaoEntity entity = new AdminLogAcaoEntity(
                id, idAdmin, acao, recursoAfetado, idRecursoAfetado, dataAcao
        );

        assertThat(entity.getIdRecursoAfetado()).isNull();
    }
}
