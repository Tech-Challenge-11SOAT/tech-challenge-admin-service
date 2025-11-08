package br.com.postech.techchallange_admin.infrastructure.persistence.mapper;

import br.com.postech.techchallange_admin.domain.model.Admin;
import br.com.postech.techchallange_admin.infrastructure.persistence.document.AdminDocument;

import java.util.List;

/**
 * Mapper responsavel pela conversão entre Admin (Domain) e AdminDocument (MongoDB)
 */
public class AdminMapper {

    public static Admin toDomain(AdminDocument document) {
        if (document == null) {
            return null;
        }

        return new Admin(
                document.getId(),
                document.getNome(),
                document.getEmail(),
                document.getSenhaHash(),
                document.getAtivo(),
                document.getDataCriacao(),
                document.getRoles()
        );
    }

    public static AdminDocument toDocument(Admin admin) {
        if (admin == null) {
            return null;
        }

        AdminDocument document = new AdminDocument();
        document.setId(admin.getId());
        document.setNome(admin.getNome());
        document.setEmail(admin.getEmail());
        document.setSenhaHash(admin.getSenhaHash());
        document.setAtivo(admin.getAtivo());
        document.setDataCriacao(admin.getDataCriacao());
        document.setRoles(admin.getRoles());
        document.setDataUltimaAtualizacao(java.time.LocalDateTime.now());

        return document;
    }

    public static List<Admin> toDomainList(List<AdminDocument> documents) {
        return documents == null ? List.of() : documents.stream().map(AdminMapper::toDomain).toList();
    }

    public static List<AdminDocument> toDocumentList(List<Admin> admins) {
        return admins == null ? List.of() : admins.stream().map(AdminMapper::toDocument).toList();
    }
}
