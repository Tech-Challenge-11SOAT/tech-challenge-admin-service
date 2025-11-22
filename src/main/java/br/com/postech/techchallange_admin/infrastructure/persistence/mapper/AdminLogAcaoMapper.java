package br.com.postech.techchallange_admin.infrastructure.persistence.mapper;

import br.com.postech.techchallange_admin.domain.model.AdminLogAcao;
import br.com.postech.techchallange_admin.infrastructure.persistence.document.AdminLogAcaoDocument;
import org.springframework.stereotype.Component;

@Component
public class AdminLogAcaoMapper {

    public AdminLogAcaoDocument toDocument(AdminLogAcao domain) {
        if (domain == null) return null;

        AdminLogAcaoDocument doc = new AdminLogAcaoDocument();
        doc.setId(domain.getId());
        doc.setIdAdmin(domain.getIdAdmin());
        doc.setAcao(domain.getAcao());
        doc.setRecursoAfetado(domain.getRecursoAfetado());
        doc.setIdRecursoAfetado(domain.getIdRecursoAfetado());
        doc.setDataAcao(domain.getDataAcao());
        return doc;
    }
}