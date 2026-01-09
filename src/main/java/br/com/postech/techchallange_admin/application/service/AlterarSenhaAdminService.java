package br.com.postech.techchallange_admin.application.service;

import org.springframework.stereotype.Service;

import br.com.postech.techchallange_admin.domain.exception.BusinessException;
import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.AlterarSenhaAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.in.LogAdminActionUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import br.com.postech.techchallange_admin.domain.port.out.PasswordEncoderPort;

@Service
public class AlterarSenhaAdminService implements AlterarSenhaAdminUseCase {

	private final AdminRepositoryPort adminRepositoryPort;
	private final PasswordEncoderPort passwordEncoderPort;
	private final LogAdminActionUseCase logAdminActionUseCase;

	public AlterarSenhaAdminService(AdminRepositoryPort adminRepositoryPort,
			PasswordEncoderPort passwordEncoderPort,
			LogAdminActionUseCase logAdminActionUseCase) {
		this.adminRepositoryPort = adminRepositoryPort;
		this.passwordEncoderPort = passwordEncoderPort;
		this.logAdminActionUseCase = logAdminActionUseCase;
	}

	@Override
	public void alterarSenha(String email, String currentPassword, String newPassword) {
		Admin admin = adminRepositoryPort.findByEmail(email)
				.orElseThrow(() -> new BusinessException("Administrador não encontrado."));

		if (!passwordEncoderPort.matches(currentPassword, admin.getSenhaHash())) {
			throw new BusinessException("Senha atual incorreta.");
		}

		validarNovaSenha(currentPassword, newPassword);

		admin.setSenhaHash(passwordEncoderPort.encode(newPassword));
		adminRepositoryPort.update(admin);

		logAdminActionUseCase.registrar(
				admin.getId(),
				"ALTERACAO_SENHA",
				"ADMIN_USER",
				admin.getId());
	}

	private void validarNovaSenha(String senhaAtual, String novaSenha) {
		if (senhaAtual.equals(novaSenha)) {
			throw new BusinessException("A nova senha não pode ser igual à senha atual.");
		}

		if (novaSenha.length() < 8 || !novaSenha.matches(".*\\d.*") || !novaSenha.matches(".*[a-zA-Z].*")) { // NOSONAR
			throw new BusinessException("A nova senha deve conter pelo menos 8 caracteres, letras e números.");
		}
	}
}
