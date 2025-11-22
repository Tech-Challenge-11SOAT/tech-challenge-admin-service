package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.model.AdminLogAcao;
import br.com.postech.techchallange_admin.domain.port.in.LogAdminActionUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminLogAcaoRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogAdminActionService implements LogAdminActionUseCase {

    private final AdminLogAcaoRepositoryPort repository;

    public LogAdminActionService(AdminLogAcaoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void registrar(String idAdmin, String acao, String recursoAfetado, String idRecursoAfetado) {
        AdminLogAcao log = new AdminLogAcao();
        log.setIdAdmin(idAdmin);
        log.setAcao(acao);
        log.setRecursoAfetado(recursoAfetado);
        log.setIdRecursoAfetado(idRecursoAfetado);
        log.setDataAcao(LocalDateTime.now());

        repository.registrarLog(log);
    }
}