package br.com.postech.techchallange_admin.infrastructure.rest.controller;

import br.com.postech.techchallange_admin.application.service.*;
import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.domain.port.in.CriarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.in.ListarAdminsUseCase;
import br.com.postech.techchallange_admin.domain.port.in.BuscarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.in.AtualizarAdminUseCase;
import br.com.postech.techchallange_admin.domain.port.in.DeletarAdminUseCase;
import br.com.postech.techchallange_admin.infrastructure.rest.dto.AdminDTO;
import br.com.postech.techchallange_admin.infrastructure.rest.dto.AdminResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final CriarAdminUseCase criarAdminUseCase;
    private final ListarAdminsUseCase listarAdminsUseCase;
    private final BuscarAdminUseCase buscarAdminUseCase;
    private final AtualizarAdminUseCase atualizarAdminUseCase;
    private final DeletarAdminUseCase deletarAdminUseCase;

    public AdminController(CriarAdminUseCase criarAdminUseCase,
                           ListarAdminsUseCase listarAdminsUseCase,
                           BuscarAdminUseCase buscarAdminUseCase,
                           AtualizarAdminUseCase atualizarAdminUseCase,
                           DeletarAdminUseCase deletarAdminUseCase) {
        this.criarAdminUseCase = criarAdminUseCase;
        this.listarAdminsUseCase = listarAdminsUseCase;
        this.buscarAdminUseCase = buscarAdminUseCase;
        this.atualizarAdminUseCase = atualizarAdminUseCase;
        this.deletarAdminUseCase = deletarAdminUseCase;
    }

    @PostMapping
    public ResponseEntity<AdminResponse> criarAdmin(@RequestBody AdminDTO request) {

        Admin adminParaCriar = new Admin();
        adminParaCriar.setNome(request.nome());
        adminParaCriar.setEmail(request.email());
        adminParaCriar.setSenhaHash(request.senha());
        adminParaCriar.setRoles(request.roles());
        adminParaCriar.setAtivo(true);
        adminParaCriar.setDataCriacao(LocalDateTime.now());

        Admin adminCriado = criarAdminUseCase.criar(adminParaCriar);

        return ResponseEntity.status(HttpStatus.CREATED).body(AdminResponse.fromDomain(adminCriado));
    }

    @GetMapping
    public ResponseEntity<List<AdminResponse>> listarAdmins() {
        List<AdminResponse> listaDeAdmins = listarAdminsUseCase.listarTodos()
                .stream()
                .map(AdminResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listaDeAdmins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminResponse> buscarAdminPorId(@PathVariable String id) {
        return buscarAdminUseCase.buscarPorId(id)
                .map(admin -> ResponseEntity.ok(AdminResponse.fromDomain(admin))) // Se encontrar, retorna 200 OK
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminResponse> atualizarAdmin(@PathVariable String id, @RequestBody AdminDTO request) {

        Optional<Admin> adminOptional = buscarAdminUseCase.buscarPorId(id);

        if (adminOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Admin adminExistente = adminOptional.get();
        adminExistente.setNome(request.nome());
        adminExistente.setEmail(request.email());
        adminExistente.setRoles(request.roles());

        Admin adminAtualizado = atualizarAdminUseCase.atualizar(adminExistente);
        return ResponseEntity.ok(AdminResponse.fromDomain(adminAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAdmin(@PathVariable String id) {
        Optional<Admin> adminOptional = buscarAdminUseCase.buscarPorId(id);

        if (adminOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        deletarAdminUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}