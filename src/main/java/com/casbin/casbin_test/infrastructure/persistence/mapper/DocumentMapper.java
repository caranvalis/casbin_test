package com.casbin.casbin_test.infrastructure.persistence.mapper;

import com.casbin.casbin_test.domain.model.Document;
import com.casbin.casbin_test.infrastructure.persistence.entity.DocumentEntity;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public Document toDomain(DocumentEntity entity) {
        Document document = new Document();
        document.setId(entity.getId());
        document.setTitle(entity.getTitle());
        document.setContent(entity.getContent());
        document.setOwner(entity.getOwner());
        document.setCategory(entity.getCategory());
        document.setSharedWithJson(entity.getSharedWithJson());
        document.setPermissionsJson(entity.getPermissionsJson());
        return document;
    }

    public DocumentEntity toEntity(Document document) {
        DocumentEntity entity = new DocumentEntity();
        entity.setId(document.getId());
        entity.setTitle(document.getTitle());
        entity.setContent(document.getContent());
        entity.setOwner(document.getOwner());
        entity.setCategory(document.getCategory());
        entity.setSharedWithJson(document.getSharedWithJson());
        entity.setPermissionsJson(document.getPermissionsJson());
        return entity;
    }
}
