package br.com.postech.techchallange_admin.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade JPA para Administradores
 * Tabela: admins
 */
@Entity
@Table(name = "admins", indexes = {
    @Index(name = "idx_admins_email", columnList = "email"),
    @Index(name = "idx_admins_ativo", columnList = "ativo")
})
public class AdminEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "roles", nullable = false, columnDefinition = "jsonb")
    private List<String> roles;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_ultima_atualizacao", nullable = false)
    private LocalDateTime dataUltimaAtualizacao;

    @Version
    @Column(name = "versao", nullable = false)
    private Integer versao;

    // Construtores
    public AdminEntity() {}

    public AdminEntity(String id, String nome, String email, String senhaHash, List<String> roles,
                       Boolean ativo, LocalDateTime dataCriacao, LocalDateTime dataUltimaAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.roles = roles;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
        this.versao = 0;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataUltimaAtualizacao() { return dataUltimaAtualizacao; }
    public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) { 
        this.dataUltimaAtualizacao = dataUltimaAtualizacao; 
    }

    public Integer getVersao() { return versao; }
    public void setVersao(Integer versao) { this.versao = versao; }
}
