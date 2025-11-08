package br.com.postech.techchallange_admin.infrastructure.persistence.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "admins")
public class AdminDocument {

    // Atributos -------------------------------------------------------------------------------------------------------
    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    @TextIndexed
    private String nome;

    private String senhaHash;

    private List<String> roles;

    private Boolean ativo;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAtualizacao;

    @Version
    private Integer versao;

    // Construtores ----------------------------------------------------------------------------------------------------
    public AdminDocument() {}

    public AdminDocument(String id, String nome, String email, String senhaHash, List<String> roles,
                         Boolean ativo, LocalDateTime dataCriacao, LocalDateTime dataUltimaAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.roles = roles;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    // Getters e Setters -----------------------------------------------------------------------------------------------
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
    public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) { this.dataUltimaAtualizacao = dataUltimaAtualizacao; }

    public Integer getVersao() { return versao; }
    public void setVersao(Integer versao) { this.versao = versao; }
}
