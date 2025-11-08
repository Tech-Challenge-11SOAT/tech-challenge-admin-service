package br.com.postech.techchallange_admin.domain.port.out;

import br.com.postech.techchallange_admin.domain.model.Admin;
import java.util.List;
import java.util.Optional;

/**
 * Porta de saída (Outbound Port) para persistência de Administrador
 * Interface que define o contrato para o repositório
 * Implementada pela camada de infraestrutura
 */
public interface AdminRepositoryPort {

    Admin save(Admin admin);
    Admin update(Admin admin);
    Optional<Admin> findById(String id);
    Optional<Admin> findByEmail(String email);
    List<Admin> findAll();
    boolean existsByEmail(String email);
    void delete(Admin admin);
    void deleteById(String id);
}
