package br.com.postech.techchallange_admin.infrastructure.persistence.adapter;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import br.com.postech.techchallange_admin.infrastructure.persistence.mapper.AdminMapper;
import br.com.postech.techchallange_admin.infrastructure.persistence.repository.AdminRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistência JPA para Admin.
 * Implementa a porta de saída AdminRepositoryPort.
 */
@Component
public class AdminAdapter implements AdminRepositoryPort {

    private final AdminRepository jpaRepository;

    public AdminAdapter(AdminRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Admin save(Admin admin) {
        var entity = AdminMapper.toEntity(admin);
        var saved = jpaRepository.save(entity);
        return AdminMapper.toDomain(saved);
    }

    @Override
    public Admin update(Admin admin) {
        var entity = AdminMapper.toEntity(admin);
        var updated = jpaRepository.save(entity);
        return AdminMapper.toDomain(updated);
    }

    @Override
    public Optional<Admin> findById(String id) {
        return jpaRepository.findById(id)
                .map(AdminMapper::toDomain);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(AdminMapper::toDomain);
    }

    @Override
    public List<Admin> findAll() {
        return jpaRepository.findAll().stream()
                .map(AdminMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public void delete(Admin admin) {
        jpaRepository.delete(AdminMapper.toEntity(admin));
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
