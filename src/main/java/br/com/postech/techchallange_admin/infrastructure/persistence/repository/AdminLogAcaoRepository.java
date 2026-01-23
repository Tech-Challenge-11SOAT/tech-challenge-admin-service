package br.com.postech.techchallange_admin.infrastructure.persistence.repository;

import br.com.postech.techchallange_admin.infrastructure.persistence.entity.AdminLogAcaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLogAcaoRepository extends JpaRepository<AdminLogAcaoEntity, String> {}
