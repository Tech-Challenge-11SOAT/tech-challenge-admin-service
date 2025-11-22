package br.com.postech.techchallange_admin.domain.port.in;

public interface LogAdminActionUseCase {
    void registrar(String idAdmin, String acao, String recursoAfetado, String idRecursoAfetado);
}