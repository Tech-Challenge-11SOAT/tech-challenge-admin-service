package br.com.postech.techchallange_admin.infrastructure.rest.dto;

public record TokenResponse(String accessToken, String refreshToken) {
}