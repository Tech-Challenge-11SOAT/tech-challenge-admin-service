package br.com.postech.techchallange_admin.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderAdapterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordEncoderAdapter passwordEncoderAdapter;

    private String rawPassword;
    private String encodedPassword;

    @BeforeEach
    void setUp() {
        rawPassword = "senha123";
        encodedPassword = "$2a$10$encodedHash123";
    }

    @Test
    void encode_deveChamarPasswordEncoderEncode() {
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        String result = passwordEncoderAdapter.encode(rawPassword);

        assertThat(result).isEqualTo(encodedPassword);
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    void encode_deveRetornarHashCodificado() {
        String expectedHash = "$2a$10$N9qo8uLOickgx2ZMRZoMye";
        when(passwordEncoder.encode(rawPassword)).thenReturn(expectedHash);

        String result = passwordEncoderAdapter.encode(rawPassword);

        assertThat(result).isEqualTo(expectedHash);
        assertThat(result).isNotEqualTo(rawPassword);
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    void encode_deveProcessarSenhaVazia() {
        String emptyPassword = "";
        String emptyHash = "$2a$10$emptyHash";
        when(passwordEncoder.encode(emptyPassword)).thenReturn(emptyHash);

        String result = passwordEncoderAdapter.encode(emptyPassword);

        assertThat(result).isEqualTo(emptyHash);
        verify(passwordEncoder, times(1)).encode(emptyPassword);
    }

    @Test
    void matches_deveChamarPasswordEncoderMatches() {
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = passwordEncoderAdapter.matches(rawPassword, encodedPassword);

        assertThat(result).isTrue();
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    void matches_deveRetornarTrueQuandoSenhasCorrespondem() {
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = passwordEncoderAdapter.matches(rawPassword, encodedPassword);

        assertThat(result).isTrue();
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    void matches_deveRetornarFalseQuandoSenhasNaoCorrespondem() {
        String wrongPassword = "senhaErrada";
        when(passwordEncoder.matches(wrongPassword, encodedPassword)).thenReturn(false);

        boolean result = passwordEncoderAdapter.matches(wrongPassword, encodedPassword);

        assertThat(result).isFalse();
        verify(passwordEncoder, times(1)).matches(wrongPassword, encodedPassword);
    }

    @Test
    void matches_deveRetornarFalseParaHashInvalido() {
        String invalidHash = "hashInvalido";
        when(passwordEncoder.matches(rawPassword, invalidHash)).thenReturn(false);

        boolean result = passwordEncoderAdapter.matches(rawPassword, invalidHash);

        assertThat(result).isFalse();
        verify(passwordEncoder, times(1)).matches(rawPassword, invalidHash);
    }

    @Test
    void encode_e_matches_deveFuncionarJuntos() {
        String newPassword = "novaSenha456";
        String newEncodedPassword = "$2a$10$newHash456";
        
        when(passwordEncoder.encode(newPassword)).thenReturn(newEncodedPassword);
        when(passwordEncoder.matches(newPassword, newEncodedPassword)).thenReturn(true);

        String encoded = passwordEncoderAdapter.encode(newPassword);
        boolean matches = passwordEncoderAdapter.matches(newPassword, encoded);

        assertThat(encoded).isEqualTo(newEncodedPassword);
        assertThat(matches).isTrue();
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(passwordEncoder, times(1)).matches(newPassword, newEncodedPassword);
    }
}
