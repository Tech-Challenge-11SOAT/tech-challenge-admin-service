package br.com.postech.techchallange_admin.infrastructure.persistence.adapter;

import br.com.postech.techchallange_admin.domain.model.AdminLogAcao;
import br.com.postech.techchallange_admin.domain.port.out.AdminLogAcaoRepositoryPort;
import br.com.postech.techchallange_admin.infrastructure.persistence.mapper.AdminLogAcaoMapper;
import br.com.postech.techchallange_admin.infrastructure.persistence.repository.AdminLogAcaoRepository;
import org.springframework.stereotype.Component;

/**
 * Adaptador de persistência JPA para AdminLogAcao.
 * Implementa a porta de saída AdminLogAcaoRepositoryPort.
 */
@Component
public class AdminLogAcaoAdapter implements AdminLogAcaoRepositoryPort {

    private final AdminLogAcaoRepository repository;
    private final AdminLogAcaoMapper mapper;

    public AdminLogAcaoAdapter(AdminLogAcaoRepository repository, AdminLogAcaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void registrarLog(AdminLogAcao log) {
        repository.save(mapper.toEntity(log));
    }
}
