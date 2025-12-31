package br.com.postech.techchallange_admin.infrastructure.security;

import br.com.postech.techchallange_admin.domain.model.Admin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtProviderTest {

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtProvider jwtProvider;

    private Admin admin;
    private String secret = "testSecretKeyThatIsLongEnoughForHS256AlgorithmToWorkProperly123456";

    @BeforeEach
    void setUp() {
        when(jwtProperties.getSecret()).thenReturn(secret);
        when(jwtProperties.getAccessTokenExpiration()).thenReturn(3600000L); // 1 hora
        when(jwtProperties.getRefreshTokenExpiration()).thenReturn(86400000L); // 24 horas

        jwtProvider.init();

        admin = new Admin();
        admin.setId("123");
        admin.setEmail("admin@test.com");
        admin.setRoles(List.of("ADMIN", "USER"));
    }

    @Test
    void generateAccessToken_deveGerarTokenComClaimsCorretos() {
        String token = jwtProvider.generateAccessToken(admin);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();

        String email = jwtProvider.getEmailFromToken(token);
        assertThat(email).isEqualTo(admin.getEmail());

        Claims claims = jwtProvider.getClaims(token);
        assertThat(claims.getSubject()).isEqualTo(admin.getEmail());
        assertThat(claims.get("idAdmin", String.class)).isEqualTo("123");
        assertThat(claims.get("roles", List.class)).containsExactlyInAnyOrder("ADMIN", "USER");
    }

    @Test
    void generateRefreshToken_deveGerarRefreshToken() {
        String token = jwtProvider.generateRefreshToken(admin);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();

        String email = jwtProvider.getEmailFromToken(token);
        assertThat(email).isEqualTo(admin.getEmail());

        boolean isValid = jwtProvider.validateToken(token);
        assertThat(isValid).isTrue();
    }

    @Test
    void validateToken_deveValidarTokenValido() {
        String token = jwtProvider.generateAccessToken(admin);

        boolean isValid = jwtProvider.validateToken(token);

        assertThat(isValid).isTrue();
    }

    @Test
    void validateToken_deveRejeitarTokenInvalido() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtProvider.validateToken(invalidToken);

        assertThat(isValid).isFalse();
    }

    @Test
    void validateToken_deveRejeitarTokenExpirado() throws InterruptedException {
        JwtProvider providerComExpiracaoCurta = new JwtProvider(jwtProperties);
        when(jwtProperties.getAccessTokenExpiration()).thenReturn(1L); // 1ms
        providerComExpiracaoCurta.init();

        String token = providerComExpiracaoCurta.generateAccessToken(admin);
        Thread.sleep(100); // Aguardar expiração

        boolean isValid = providerComExpiracaoCurta.validateToken(token);

        assertThat(isValid).isFalse();
    }

    @Test
    void getEmailFromToken_deveExtrairEmailDoToken() {
        String token = jwtProvider.generateAccessToken(admin);

        String email = jwtProvider.getEmailFromToken(token);

        assertThat(email).isEqualTo(admin.getEmail());
    }

    @Test
    void getClaims_deveExtrairClaimsDoToken() {
        String token = jwtProvider.generateAccessToken(admin);

        Claims claims = jwtProvider.getClaims(token);

        assertThat(claims).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(admin.getEmail());
        assertThat(claims.get("idAdmin", String.class)).isEqualTo("123");
    }

    @Test
    void getCurrentUserEmail_deveObterEmailDoContextoDeSeguranca() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("admin@test.com");
        SecurityContextHolder.setContext(securityContext);

        String email = jwtProvider.getCurrentUserEmail();

        assertThat(email).isEqualTo("admin@test.com");
    }

    @Test
    void getCurrentUserEmail_deveFalharQuandoNaoAutenticado() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertThatThrownBy(() -> jwtProvider.getCurrentUserEmail())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não autenticado");
    }

    @Test
    void getCurrentUserEmail_deveFalharQuandoAuthenticationNaoEstaAutenticado() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.setContext(securityContext);

        assertThatThrownBy(() -> jwtProvider.getCurrentUserEmail())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não autenticado");
    }

    @Test
    void init_deveInicializarChaveSecreta() {
        JwtProvider newProvider = new JwtProvider(jwtProperties);
        newProvider.init();

        String token = newProvider.generateAccessToken(admin);
        assertThat(token).isNotNull();
    }

    @Test
    void generateAccessToken_deveFuncionarComRolesNull() {
        admin.setRoles(null);
        
        String token = jwtProvider.generateAccessToken(admin);

        assertThat(token).isNotNull();
        assertThat(jwtProvider.validateToken(token)).isTrue();
    }

    @Test
    void generateAccessToken_deveFuncionarComRolesVazias() {
        admin.setRoles(List.of());
        
        String token = jwtProvider.generateAccessToken(admin);

        assertThat(token).isNotNull();
        assertThat(jwtProvider.validateToken(token)).isTrue();
    }
}
