package br.com.postech.techchallange_admin.infrastructure.rest.dto;

import br.com.postech.techchallange_admin.domain.model.Admin;
import java.time.LocalDateTime;
import java.util.List;

public record AdminResponse(
        String id,
        String nome,
        String email,
        Boolean ativo,
        LocalDateTime dataCriacao,
        List<String> roles
) {

    public static AdminResponse fromDomain(Admin admin) {
        return new AdminResponse(
                admin.getId(),
                admin.getNome(),
                admin.getEmail(),
                admin.getAtivo(),
                admin.getDataCriacao(),
                admin.getRoles()
        );
    }
}