package br.com.postech.techchallange_admin.domain.port.in;

import br.com.postech.techchallange_admin.domain.model.Admin;
import java.util.Optional;

public interface BuscarAdminUseCase {
    Optional<Admin> buscarPorId(String id);
    Optional<Admin> buscarPorEmail(String email);
}
