package com.casbin.casbin_test.infrastructure.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("casbin_rule")
public class CasbinRuleEntity {
    @Id
    private Long id;

    @Column("p_type")
    private String pType;

    @Column("v0")
    private String v0;

    @Column("v1")
    private String v1;

    @Column("v2")
    private String v2;

    @Column("v3")
    private String v3;

    @Column("v4")
    private String v4;

    @Column("v5")
    private String v5;
}