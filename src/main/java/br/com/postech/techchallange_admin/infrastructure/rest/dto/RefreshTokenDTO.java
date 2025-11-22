package br.com.postech.techchallange_admin.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDTO(
        @NotBlank(message = "O refreshToken é obrigatório")
        String refreshToken
) {
}