package br.com.postech.techchallange_admin.infrastructure.persistence.adapter;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import br.com.postech.techchallange_admin.infrastructure.persistence.document.AdminDocument;
import br.com.postech.techchallange_admin.infrastructure.persistence.mapper.AdminMapper;
import br.com.postech.techchallange_admin.infrastructure.persistence.repository.AdminRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia MongoDB para Admin.
 * Implementa a porta de saida AdminRepositoryPort.
 */
@Component
public class AdminAdapter implements AdminRepositoryPort {

    private final AdminRepository mongoRepository;

    public AdminAdapter(AdminRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Admin save(Admin admin) {
        AdminDocument document = AdminMapper.toDocument(admin);
        AdminDocument saved = mongoRepository.save(document);
        return AdminMapper.toDomain(saved);
    }

    @Override
    public Admin update(Admin admin) {
        AdminDocument document = AdminMapper.toDocument(admin);
        AdminDocument updated = mongoRepository.save(document);
        return AdminMapper.toDomain(updated);
    }

    @Override
    public Optional<Admin> findById(String id) {
        return mongoRepository.findById(id)
                .map(AdminMapper::toDomain);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return mongoRepository.findByEmail(email)
                .map(AdminMapper::toDomain);
    }

    @Override
    public List<Admin> findAll() {
        return mongoRepository.findAll().stream()
                .map(AdminMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return mongoRepository.existsByEmail(email);
    }

    @Override
    public void delete(Admin admin) {
        mongoRepository.delete(AdminMapper.toDocument(admin));
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }
}
