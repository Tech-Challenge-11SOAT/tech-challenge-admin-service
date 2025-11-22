package br.com.postech.techchallange_admin.domain.port.in;

public interface AlterarSenhaAdminUseCase {
    void alterarSenha(String email, String currentPassword, String newPassword);
}