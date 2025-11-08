package br.com.postech.techchallange_admin.domain.port.in;

public interface LogoutAdminUseCase {
    void logout(String token);
}