package com.casbin.casbin_test.infrastructure.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("documents")
public class DocumentEntity {
    @Id
    private String id;
    private String title;
    private String content;
    private String owner;
    private String category;
    private String sharedWithJson = "{}";
    private String permissionsJson = "{}";
}