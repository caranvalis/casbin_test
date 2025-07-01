package com.casbin.casbin_test.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthorizationServiceTest {

    @Autowired
    private AuthorizationService authorizationService;

    @Test
    public void testAddRole() {
        // Ajouter un nouveau rôle à un utilisateur
        assertTrue(authorizationService.addRoleForUser("david", "user").block());
        assertTrue(authorizationService.getRolesForUser("david").collectList().block().contains("user"));
    }

    @Test
    public void testAddPolicy() {
        // Ajouter une nouvelle politique
        assertTrue(authorizationService.addPolicy("moderator", "/api/posts", "DELETE").block());
        assertTrue(authorizationService.hasPermission("moderator", "/api/posts", "DELETE").block());
    }
}