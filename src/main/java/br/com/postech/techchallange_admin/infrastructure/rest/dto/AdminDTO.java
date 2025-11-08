package br.com.postech.techchallange_admin.infrastructure.rest.dto;

import java.util.List;

public record AdminDTO(
        String nome,
        String email,
        String senha,
        List<String> roles
) {
}