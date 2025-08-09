package com.casbin.casbin_test.infrastructure.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("users")
public class UserEntity {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private String role;
}