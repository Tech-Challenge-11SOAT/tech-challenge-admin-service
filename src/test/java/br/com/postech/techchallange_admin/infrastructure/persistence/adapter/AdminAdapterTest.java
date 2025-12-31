package br.com.postech.techchallange_admin.infrastructure.persistence.adapter;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.infrastructure.persistence.entity.AdminEntity;
import br.com.postech.techchallange_admin.infrastructure.persistence.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminAdapterTest {

    @Mock
    private AdminRepository jpaRepository;

    @InjectMocks
    private AdminAdapter adminAdapter;

    private Admin admin;
    private AdminEntity entity;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId("123");
        admin.setNome("Admin Test");
        admin.setEmail("admin@test.com");
        admin.setSenhaHash("hash123");
        admin.setAtivo(true);
        admin.setDataCriacao(LocalDateTime.now());
        admin.setRoles(List.of("ADMIN"));

        entity = new AdminEntity();
        entity.setId("123");
        entity.setNome("Admin Test");
        entity.setEmail("admin@test.com");
        entity.setSenhaHash("hash123");
        entity.setAtivo(true);
        entity.setDataCriacao(LocalDateTime.now());
        entity.setRoles(List.of("ADMIN"));
        entity.setVersao(0);
    }

    @Test
    void save_deveSalvarAdmin() {
        when(jpaRepository.save(any(AdminEntity.class))).thenReturn(entity);

        Admin saved = adminAdapter.save(admin);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(admin.getId());
        verify(jpaRepository, times(1)).save(any(AdminEntity.class));
    }

    @Test
    void update_deveAtualizarAdmin() {
        when(jpaRepository.save(any(AdminEntity.class))).thenReturn(entity);

        Admin updated = adminAdapter.update(admin);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(admin.getId());
        verify(jpaRepository, times(1)).save(any(AdminEntity.class));
    }

    @Test
    void findById_deveEncontrarPorIdExistente() {
        when(jpaRepository.findById("123")).thenReturn(Optional.of(entity));

        Optional<Admin> result = adminAdapter.findById("123");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("123");
        verify(jpaRepository, times(1)).findById("123");
    }

    @Test
    void findById_deveRetornarEmptyQuandoNaoExiste() {
        when(jpaRepository.findById("999")).thenReturn(Optional.empty());

        Optional<Admin> result = adminAdapter.findById("999");

        assertThat(result).isEmpty();
        verify(jpaRepository, times(1)).findById("999");
    }

    @Test
    void findByEmail_deveEncontrarPorEmailExistente() {
        when(jpaRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(entity));

        Optional<Admin> result = adminAdapter.findByEmail("admin@test.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("admin@test.com");
        verify(jpaRepository, times(1)).findByEmail("admin@test.com");
    }

    @Test
    void findByEmail_deveRetornarEmptyQuandoNaoExiste() {
        when(jpaRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        Optional<Admin> result = adminAdapter.findByEmail("notfound@test.com");

        assertThat(result).isEmpty();
        verify(jpaRepository, times(1)).findByEmail("notfound@test.com");
    }

    @Test
    void findAll_deveListarTodos() {
        AdminEntity entity2 = new AdminEntity();
        entity2.setId("456");
        entity2.setNome("Admin 2");
        
        when(jpaRepository.findAll()).thenReturn(List.of(entity, entity2));

        List<Admin> result = adminAdapter.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("123");
        assertThat(result.get(1).getId()).isEqualTo("456");
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void existsByEmail_deveVerificarExistenciaPorEmail() {
        when(jpaRepository.existsByEmail("admin@test.com")).thenReturn(true);

        boolean exists = adminAdapter.existsByEmail("admin@test.com");

        assertThat(exists).isTrue();
        verify(jpaRepository, times(1)).existsByEmail("admin@test.com");
    }

    @Test
    void delete_deveDeletarAdmin() {
        adminAdapter.delete(admin);

        verify(jpaRepository, times(1)).delete(any(AdminEntity.class));
    }

    @Test
    void deleteById_deveDeletarPorId() {
        adminAdapter.deleteById("123");

        verify(jpaRepository, times(1)).deleteById("123");
    }
}
