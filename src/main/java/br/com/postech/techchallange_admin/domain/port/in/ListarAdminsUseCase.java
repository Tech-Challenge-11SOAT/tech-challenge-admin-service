package br.com.postech.techchallange_admin.domain.port.in;

import br.com.postech.techchallange_admin.domain.model.Admin;
import java.util.List;

public interface ListarAdminsUseCase {
    List<Admin> listarTodos();
}
