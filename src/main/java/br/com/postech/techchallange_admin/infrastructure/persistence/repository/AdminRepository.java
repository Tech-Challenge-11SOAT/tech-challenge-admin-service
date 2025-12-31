package br.com.postech.techchallange_admin.infrastructure.persistence.repository;

import br.com.postech.techchallange_admin.infrastructure.persistence.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, String> {

    Optional<AdminEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
