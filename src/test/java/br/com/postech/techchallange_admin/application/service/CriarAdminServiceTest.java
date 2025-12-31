package br.com.postech.techchallange_admin.application.service;

import br.com.postech.techchallange_admin.domain.exception.BusinessException;
import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.LogAdminActionUseCase;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import br.com.postech.techchallange_admin.domain.port.out.PasswordEncoderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarAdminServiceTest {

    @Mock
    private AdminRepositoryPort adminRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private LogAdminActionUseCase logAdminActionUseCase;

    @InjectMocks
    private CriarAdminService criarAdminService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setNome("Admin Test");
        admin.setEmail("admin@test.com");
        admin.setSenhaHash("senha123");
        admin.setRoles(List.of("ADMIN"));
        admin.setAtivo(true);
        admin.setDataCriacao(LocalDateTime.now());
    }

    @Test
    void deveCriarAdminComSucesso() {
        Admin adminSalvo = new Admin();
        adminSalvo.setId("123");
        adminSalvo.setNome(admin.getNome());
        adminSalvo.setEmail(admin.getEmail());
        adminSalvo.setSenhaHash("senhaCriptografada");
        adminSalvo.setRoles(admin.getRoles());

        when(adminRepositoryPort.existsByEmail(admin.getEmail())).thenReturn(false);
        when(passwordEncoderPort.encode(admin.getSenhaHash())).thenReturn("senhaCriptografada");
        when(adminRepositoryPort.save(any(Admin.class))).thenReturn(adminSalvo);

        Admin result = criarAdminService.criar(admin);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("123");
        verify(adminRepositoryPort, times(1)).existsByEmail(admin.getEmail());
        verify(passwordEncoderPort, times(1)).encode("senha123");
        verify(adminRepositoryPort, times(1)).save(any(Admin.class));
        verify(logAdminActionUseCase, times(1)).registrar(
                eq("123"),
                eq("CADASTRO"),
                eq("ADMIN_USER"),
                eq("123")
        );
    }

    @Test
    void deveFalharQuandoEmailJaExiste() {
        when(adminRepositoryPort.existsByEmail(admin.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> criarAdminService.criar(admin))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email ja cadastrado");

        verify(adminRepositoryPort, times(1)).existsByEmail(admin.getEmail());
        verify(adminRepositoryPort, never()).save(any(Admin.class));
    }

    @Test
    void deveFalharQuandoRolesEstaoVazias() {
        admin.setRoles(List.of());
        when(adminRepositoryPort.existsByEmail(admin.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> criarAdminService.criar(admin))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Um administrador precisa ter pelo menos uma role.");

        verify(adminRepositoryPort, times(1)).existsByEmail(admin.getEmail());
        verify(adminRepositoryPort, never()).save(any(Admin.class));
    }

    @Test
    void deveFalharQuandoRolesSaoNull() {
        admin.setRoles(null);
        when(adminRepositoryPort.existsByEmail(admin.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> criarAdminService.criar(admin))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Um administrador precisa ter pelo menos uma role.");

        verify(adminRepositoryPort, times(1)).existsByEmail(admin.getEmail());
        verify(adminRepositoryPort, never()).save(any(Admin.class));
    }

    @Test
    void deveCriptografarSenhaAntesDeSalvar() {
        Admin adminSalvo = new Admin();
        adminSalvo.setId("123");
        String senhaCriptografada = "senhaCriptografada";

        when(adminRepositoryPort.existsByEmail(admin.getEmail())).thenReturn(false);
        when(passwordEncoderPort.encode(admin.getSenhaHash())).thenReturn(senhaCriptografada);
        when(adminRepositoryPort.save(any(Admin.class))).thenAnswer(invocation -> {
            Admin adminToSave = invocation.getArgument(0);
            assertThat(adminToSave.getSenhaHash()).isEqualTo(senhaCriptografada);
            adminSalvo.setSenhaHash(senhaCriptografada);
            return adminSalvo;
        });

        criarAdminService.criar(admin);

        verify(passwordEncoderPort, times(1)).encode("senha123");
    }
}
