package br.com.postech.techchallange_admin.domain.model;

import java.time.LocalDateTime;

public class AdminLogAcao {

    private String id;
    private String idAdmin;
    private String acao;
    private String recursoAfetado;
    private String idRecursoAfetado;
    private LocalDateTime dataAcao;

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