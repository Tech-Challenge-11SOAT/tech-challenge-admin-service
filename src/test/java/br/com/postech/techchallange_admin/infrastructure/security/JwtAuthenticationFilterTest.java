package br.com.postech.techchallange_admin.infrastructure.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private String validToken = "valid.token.here";
    private Claims claims;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        
        claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("admin@test.com");
        when(claims.get("roles", List.class)).thenReturn(List.of("ADMIN"));
    }

    @Test
    void deveProcessarRequisicaoComTokenValido() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtProvider.validateToken(validToken)).thenReturn(true);
        when(tokenBlacklistService.isTokenBlacklisted(validToken)).thenReturn(false);
        when(jwtProvider.getClaims(validToken)).thenReturn(claims);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtProvider, times(1)).validateToken(validToken);
        verify(tokenBlacklistService, times(1)).isTokenBlacklisted(validToken);
        verify(jwtProvider, times(1)).getClaims(validToken);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void deveProcessarRequisicaoSemToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtProvider, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void deveProcessarRequisicaoComHeaderVazio() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtProvider, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void deveRejeitarTokenInvalido() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtProvider.validateToken(validToken)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtProvider, times(1)).validateToken(validToken);
        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void deveRejeitarTokenNaBlacklist() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtProvider.validateToken(validToken)).thenReturn(true);
        when(tokenBlacklistService.isTokenBlacklisted(validToken)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtProvider, times(1)).validateToken(validToken);
        verify(tokenBlacklistService, times(1)).isTokenBlacklisted(validToken);
        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void deveExtrairEConfigurarAuthoritiesCorretamente() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtProvider.validateToken(validToken)).thenReturn(true);
        when(tokenBlacklistService.isTokenBlacklisted(validToken)).thenReturn(false);
        when(jwtProvider.getClaims(validToken)).thenReturn(claims);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtProvider, times(1)).getClaims(validToken);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void deveTratarExcecoesDuranteProcessamento() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtProvider.validateToken(validToken)).thenThrow(new RuntimeException("Erro ao validar"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erro na autenticação");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void deveProcessarTokenSemPrefixoBearer() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtProvider.validateToken(validToken)).thenReturn(true);
        when(tokenBlacklistService.isTokenBlacklisted(validToken)).thenReturn(false);
        when(jwtProvider.getClaims(validToken)).thenReturn(claims);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtProvider, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
