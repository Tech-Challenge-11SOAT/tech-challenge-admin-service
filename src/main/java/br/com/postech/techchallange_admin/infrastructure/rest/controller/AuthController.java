package br.com.postech.techchallange_admin.infrastructure.rest.controller;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.AutenticarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.in.AlterarSenhaAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.in.BuscarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.in.LogoutAdminUseCase;
import br.com.postech.techchallange_admin.infrastructure.security.JwtProvider;
import br.com.postech.techchallange_admin.infrastructure.rest.dto.ChangePasswordDTO;
import br.com.postech.techchallange_admin.infrastructure.rest.dto.LoginDTO;
import br.com.postech.techchallange_admin.infrastructure.rest.dto.RefreshTokenDTO;
import br.com.postech.techchallange_admin.infrastructure.rest.dto.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth") // Endereço base: /auth
public class AuthController {

    private final AutenticarAdminUseCase autenticarAdminUseCase;
    private final LogoutAdminUseCase logoutAdminUseCase;
    private final BuscarAdminUseCase buscarAdminUseCase;
    private final JwtProvider jwtProvider;
    private final AlterarSenhaAdminUseCase alterarSenhaAdminUseCase;

    public AuthController(AutenticarAdminUseCase autenticarAdminUseCase,
                          LogoutAdminUseCase logoutAdminUseCase,
                          BuscarAdminUseCase buscarAdminUseCase,
                          JwtProvider jwtProvider,
                          AlterarSenhaAdminUseCase alterarSenhaAdminUseCase) {
        this.autenticarAdminUseCase = autenticarAdminUseCase;
        this.logoutAdminUseCase = logoutAdminUseCase;
        this.buscarAdminUseCase = buscarAdminUseCase;
        this.jwtProvider = jwtProvider;
        this.alterarSenhaAdminUseCase = alterarSenhaAdminUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginDTO request) {
        // 1. Chama o serviço de autenticação
        Admin admin = autenticarAdminUseCase.autenticar(request.email(), request.senha());

        // 2. Gera os tokens
        String accessToken = jwtProvider.generateAccessToken(admin);
        String refreshToken = jwtProvider.generateRefreshToken(admin);

        // 3. Retorna os tokens
        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenDTO request) {
        // 1. Valida o refresh token
        if (!jwtProvider.validateToken(request.refreshToken())) {
            throw new RuntimeException("Refresh token inválido ou expirado.");
        }

        // 2. Pega o email de dentro do token
        String email = jwtProvider.getEmailFromToken(request.refreshToken());

        // 3. Busca o usuário no banco
        Admin admin = buscarAdminUseCase.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário do token não encontrado"));

        // 4. Gera novos tokens
        String newAccessToken = jwtProvider.generateAccessToken(admin);
        String newRefreshToken = jwtProvider.generateRefreshToken(admin);

        return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenDTO request) {
        // (Esta lógica está estranha no monolito, mas vamos manter por enquanto)
        // O ideal seria invalidar o Access Token, não o Refresh Token.
        logoutAdminUseCase.logout(request.refreshToken());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        alterarSenhaAdminUseCase.alterarSenha(email, request.currentPassword(), request.newPassword());

        return ResponseEntity.noContent().build();
    }
}
