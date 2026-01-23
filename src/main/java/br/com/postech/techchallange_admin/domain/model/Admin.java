package br.com.postech.techchallange_admin.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Admin {

    // Atributos -------------------------------------------------------------------------------------------------------
    private String id;
    private String nome;
    private String email;
    private String senhaHash;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private List<String> roles; // Manter as roles como strings simples nesta camada

    // Construtores ----------------------------------------------------------------------------------------------------
    public Admin() {}

    public Admin(String id, String nome, String email, String senhaHash, Boolean ativo, LocalDateTime dataCriacao, List<String> roles) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.roles = roles;
    }

    // Getters e Setters -----------------------------------------------------------------------------------------------
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    // Metodo que retorna uma copia do admin sem o hash da senha -------------------------------------------------------
    public Admin semSenha() {
        return new Admin(this.id, this.nome, this.email, null, this.ativo, this.dataCriacao, this.roles);
    }
}
