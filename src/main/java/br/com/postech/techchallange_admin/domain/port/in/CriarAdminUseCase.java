package br.com.postech.techchallange_admin.domain.port.in;

import br.com.postech.techchallange_admin.domain.model.Admin;

public interface CriarAdminUseCase {
    Admin criar(Admin admin);
}
