package br.com.postech.techchallange_admin.infrastructure.persistence.repository;

import br.com.postech.techchallange_admin.infrastructure.persistence.document.AdminLogAcaoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLogAcaoRepository extends MongoRepository<AdminLogAcaoDocument, String> {
}