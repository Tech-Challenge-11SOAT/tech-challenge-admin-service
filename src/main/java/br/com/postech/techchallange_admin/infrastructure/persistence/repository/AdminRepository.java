package br.com.postech.techchallange_admin.infrastructure.persistence.repository;

import br.com.postech.techchallange_admin.infrastructure.persistence.document.AdminDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends MongoRepository<AdminDocument, String> {

    Optional<AdminDocument> findByEmail(String email);

    boolean existsByEmail(String email);
}
