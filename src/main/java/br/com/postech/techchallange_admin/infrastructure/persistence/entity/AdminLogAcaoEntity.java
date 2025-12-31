package br.com.postech.techchallange_admin.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entidade JPA para Logs de Ações dos Administradores
 * Tabela: admin_log_acao
 */
@Entity
@Table(name = "admin_log_acao", indexes = {
    @Index(name = "idx_admin_log_acao_id_admin", columnList = "id_admin"),
    @Index(name = "idx_admin_log_acao_data_acao", columnList = "data_acao"),
    @Index(name = "idx_admin_log_acao_acao", columnList = "acao"),
    @Index(name = "idx_admin_log_acao_recurso_afetado", columnList = "recurso_afetado")
})
public class AdminLogAcaoEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "id_admin", nullable = false, length = 36)
    private String idAdmin;

    @Column(name = "acao", nullable = false, length = 100)
    private String acao;

    @Column(name = "recurso_afetado", nullable = false, length = 100)
    private String recursoAfetado;

    @Column(name = "id_recurso_afetado", length = 255)
    private String idRecursoAfetado;

    @Column(name = "data_acao", nullable = false)
    private LocalDateTime dataAcao;

    // Construtores
    public AdminLogAcaoEntity() {}

    public AdminLogAcaoEntity(String id, String idAdmin, String acao, String recursoAfetado,
                              String idRecursoAfetado, LocalDateTime dataAcao) {
        this.id = id;
        this.idAdmin = idAdmin;
        this.acao = acao;
        this.recursoAfetado = recursoAfetado;
        this.idRecursoAfetado = idRecursoAfetado;
        this.dataAcao = dataAcao;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdAdmin() { return idAdmin; }
    public void setIdAdmin(String idAdmin) { this.idAdmin = idAdmin; }

    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }

    public String getRecursoAfetado() { return recursoAfetado; }
    public void setRecursoAfetado(String recursoAfetado) { this.recursoAfetado = recursoAfetado; }

    public String getIdRecursoAfetado() { return idRecursoAfetado; }
    public void setIdRecursoAfetado(String idRecursoAfetado) { this.idRecursoAfetado = idRecursoAfetado; }

    public LocalDateTime getDataAcao() { return dataAcao; }
    public void setDataAcao(LocalDateTime dataAcao) { this.dataAcao = dataAcao; }
}
