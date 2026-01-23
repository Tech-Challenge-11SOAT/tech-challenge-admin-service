package br.com.postech.techchallange_admin.infrastructure.rest.controller;

import br.com.postech.techchallange_admin.domain.exception.BusinessException;
import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.AlterarSenhaAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.in.AutenticarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.in.BuscarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.in.LogoutAdminUseCase;
import br.com.postech.techchallange_admin.infrastructure.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutenticarAdminUseCase autenticarAdminUseCase;

    @MockBean
    private LogoutAdminUseCase logoutAdminUseCase;

    @MockBean
    private BuscarAdminUseCase buscarAdminUseCase;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private AlterarSenhaAdminUseCase alterarSenhaAdminUseCase;

    private Admin admin;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        
        admin = new Admin();
        admin.setId("123");
        admin.setEmail("admin@test.com");
        admin.setRoles(List.of("ADMIN"));
    }

    @Test
    void login_deveRetornar200QuandoSucesso() throws Exception {
        when(autenticarAdminUseCase.autenticar("admin@test.com", "senha123"))
                .thenReturn(admin);
        when(jwtProvider.generateAccessToken(admin)).thenReturn("accessToken");
        when(jwtProvider.generateRefreshToken(admin)).thenReturn("refreshToken");

        String requestBody = """
                {
                    "email": "admin@test.com",
                    "senha": "senha123"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));

        verify(autenticarAdminUseCase, times(1)).autenticar("admin@test.com", "senha123");
        verify(jwtProvider, times(1)).generateAccessToken(admin);
        verify(jwtProvider, times(1)).generateRefreshToken(admin);
    }

    @Test
    void login_deveRetornar400QuandoCredenciaisInvalidas() throws Exception {
        when(autenticarAdminUseCase.autenticar(anyString(), anyString()))
                .thenThrow(new BusinessException("Usuário ou senha inválidos"));

        String requestBody = """
                {
                    "email": "admin@test.com",
                    "senha": "senhaErrada"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(autenticarAdminUseCase, times(1)).autenticar(anyString(), anyString());
    }

    @Test
    void refresh_deveRetornar200QuandoTokenValido() throws Exception {
        String refreshToken = "validRefreshToken";
        when(jwtProvider.validateToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getEmailFromToken(refreshToken)).thenReturn("admin@test.com");
        when(buscarAdminUseCase.buscarPorEmail("admin@test.com")).thenReturn(Optional.of(admin));
        when(jwtProvider.generateAccessToken(admin)).thenReturn("newAccessToken");
        when(jwtProvider.generateRefreshToken(admin)).thenReturn("newRefreshToken");

        String requestBody = """
                {
                    "refreshToken": "validRefreshToken"
                }
                """;

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("newRefreshToken"));

        verify(jwtProvider, times(1)).validateToken(refreshToken);
        verify(buscarAdminUseCase, times(1)).buscarPorEmail("admin@test.com");
    }

    @Test
    void refresh_deveRetornar500QuandoTokenInvalido() throws Exception {
        String refreshToken = "invalidToken";
        when(jwtProvider.validateToken(refreshToken)).thenReturn(false);

        String requestBody = """
                {
                    "refreshToken": "invalidToken"
                }
                """;

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError());

        verify(jwtProvider, times(1)).validateToken(refreshToken);
        verify(buscarAdminUseCase, never()).buscarPorEmail(anyString());
    }

    @Test
    void refresh_deveRetornar500QuandoUsuarioNaoEncontrado() throws Exception {
        String refreshToken = "validRefreshToken";
        when(jwtProvider.validateToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getEmailFromToken(refreshToken)).thenReturn("admin@test.com");
        when(buscarAdminUseCase.buscarPorEmail("admin@test.com")).thenReturn(Optional.empty());

        String requestBody = """
                {
                    "refreshToken": "validRefreshToken"
                }
                """;

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError());

        verify(jwtProvider, times(1)).validateToken(refreshToken);
        verify(buscarAdminUseCase, times(1)).buscarPorEmail("admin@test.com");
    }

    @Test
    void logout_deveRetornar200QuandoSucesso() throws Exception {
        doNothing().when(logoutAdminUseCase).logout(anyString());

        String requestBody = """
                {
                    "refreshToken": "token123"
                }
                """;

        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(logoutAdminUseCase, times(1)).logout("token123");
    }

    @Test
    void changePassword_deveRetornar204QuandoSucesso() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("admin@test.com");
        SecurityContextHolder.setContext(securityContext);

        doNothing().when(alterarSenhaAdminUseCase)
                .alterarSenha("admin@test.com", "senhaAtual", "novaSenha");

        String requestBody = """
                {
                    "currentPassword": "senhaAtual",
                    "newPassword": "novaSenha"
                }
                """;

        mockMvc.perform(post("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());

        verify(alterarSenhaAdminUseCase, times(1))
                .alterarSenha("admin@test.com", "senhaAtual", "novaSenha");
    }

    @Test
    void changePassword_deveRetornar400QuandoValidacaoFalha() throws Exception {
        String requestBody = """
                {
                    "currentPassword": "",
                    "newPassword": ""
                }
                """;

        mockMvc.perform(post("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(alterarSenhaAdminUseCase, never()).alterarSenha(anyString(), anyString(), anyString());
    }
}
