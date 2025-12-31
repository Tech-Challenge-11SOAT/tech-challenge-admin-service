package br.com.postech.techchallange_admin.infrastructure.rest.controller;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CriarAdminUseCase criarAdminUseCase;

    @MockBean
    private ListarAdminsUseCase listarAdminsUseCase;

    @MockBean
    private BuscarAdminUseCase buscarAdminUseCase;

    @MockBean
    private AtualizarAdminUseCase atualizarAdminUseCase;

    @MockBean
    private DeletarAdminUseCase deletarAdminUseCase;

    @MockBean
    private ToggleAdminUseCase toggleAdminUseCase;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId("123");
        admin.setNome("Admin Test");
        admin.setEmail("admin@test.com");
        admin.setAtivo(true);
        admin.setDataCriacao(LocalDateTime.now());
        admin.setRoles(List.of("ADMIN"));
    }

    @Test
    void criarAdmin_deveRetornar201QuandoSucesso() throws Exception {
        when(criarAdminUseCase.criar(any(Admin.class))).thenReturn(admin);

        String requestBody = """
                {
                    "nome": "Admin Test",
                    "email": "admin@test.com",
                    "senha": "senha123",
                    "roles": ["ADMIN"]
                }
                """;

        mockMvc.perform(post("/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.nome").value("Admin Test"))
                .andExpect(jsonPath("$.email").value("admin@test.com"));

        verify(criarAdminUseCase, times(1)).criar(any(Admin.class));
    }

    @Test
    void criarAdmin_deveRetornar400QuandoValidacaoFalha() throws Exception {
        String requestBody = """
                {
                    "nome": "",
                    "email": "invalid-email",
                    "senha": "123",
                    "roles": []
                }
                """;

        mockMvc.perform(post("/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(criarAdminUseCase, never()).criar(any(Admin.class));
    }

    @Test
    void listarAdmins_deveRetornar200ComLista() throws Exception {
        when(listarAdminsUseCase.listarTodos()).thenReturn(List.of(admin));

        mockMvc.perform(get("/admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("123"));

        verify(listarAdminsUseCase, times(1)).listarTodos();
    }

    @Test
    void buscarAdminPorId_deveRetornar200QuandoExiste() throws Exception {
        when(buscarAdminUseCase.buscarPorId("123")).thenReturn(Optional.of(admin));

        mockMvc.perform(get("/admins/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.nome").value("Admin Test"));

        verify(buscarAdminUseCase, times(1)).buscarPorId("123");
    }

    @Test
    void buscarAdminPorId_deveRetornar404QuandoNaoExiste() throws Exception {
        when(buscarAdminUseCase.buscarPorId("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/admins/999"))
                .andExpect(status().isNotFound());

        verify(buscarAdminUseCase, times(1)).buscarPorId("999");
    }

    @Test
    void atualizarAdmin_deveRetornar200QuandoExiste() throws Exception {
        Admin adminAtualizado = new Admin();
        adminAtualizado.setId("123");
        adminAtualizado.setNome("Admin Atualizado");
        adminAtualizado.setEmail("admin@test.com");
        adminAtualizado.setRoles(List.of("ADMIN"));

        when(buscarAdminUseCase.buscarPorId("123")).thenReturn(Optional.of(admin));
        when(atualizarAdminUseCase.atualizar(any(Admin.class))).thenReturn(adminAtualizado);

        String requestBody = """
                {
                    "nome": "Admin Atualizado",
                    "email": "admin@test.com",
                    "roles": ["ADMIN"]
                }
                """;

        mockMvc.perform(put("/admins/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Admin Atualizado"));

        verify(buscarAdminUseCase, times(1)).buscarPorId("123");
        verify(atualizarAdminUseCase, times(1)).atualizar(any(Admin.class));
    }

    @Test
    void atualizarAdmin_deveRetornar404QuandoNaoExiste() throws Exception {
        when(buscarAdminUseCase.buscarPorId("999")).thenReturn(Optional.empty());

        String requestBody = """
                {
                    "nome": "Admin Atualizado",
                    "email": "admin@test.com",
                    "roles": ["ADMIN"]
                }
                """;

        mockMvc.perform(put("/admins/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());

        verify(buscarAdminUseCase, times(1)).buscarPorId("999");
        verify(atualizarAdminUseCase, never()).atualizar(any(Admin.class));
    }

    @Test
    void deletarAdmin_deveRetornar204QuandoExiste() throws Exception {
        when(buscarAdminUseCase.buscarPorId("123")).thenReturn(Optional.of(admin));
        doNothing().when(deletarAdminUseCase).deletar("123");

        mockMvc.perform(delete("/admins/123"))
                .andExpect(status().isNoContent());

        verify(buscarAdminUseCase, times(1)).buscarPorId("123");
        verify(deletarAdminUseCase, times(1)).deletar("123");
    }

    @Test
    void deletarAdmin_deveRetornar404QuandoNaoExiste() throws Exception {
        when(buscarAdminUseCase.buscarPorId("999")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/admins/999"))
                .andExpect(status().isNotFound());

        verify(buscarAdminUseCase, times(1)).buscarPorId("999");
        verify(deletarAdminUseCase, never()).deletar(anyString());
    }

    @Test
    void inativarAdmin_deveRetornar200() throws Exception {
        Admin adminInativado = new Admin();
        adminInativado.setId("123");
        adminInativado.setAtivo(false);

        when(toggleAdminUseCase.toggle("123", false)).thenReturn(adminInativado);

        mockMvc.perform(patch("/admins/123/inactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));

        verify(toggleAdminUseCase, times(1)).toggle("123", false);
    }

    @Test
    void ativarAdmin_deveRetornar200() throws Exception {
        Admin adminAtivado = new Admin();
        adminAtivado.setId("123");
        adminAtivado.setAtivo(true);

        when(toggleAdminUseCase.toggle("123", true)).thenReturn(adminAtivado);

        mockMvc.perform(patch("/admins/123/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(true));

        verify(toggleAdminUseCase, times(1)).toggle("123", true);
    }
}
