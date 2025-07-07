package com.casbin.casbin_test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AuthorizationServiceTest {

    @Autowired
    private AuthorizationService authorizationService;

    @BeforeEach
    public void setup() {
        // Supprime la relation existante avant le test
        authorizationService.deleteRoleForUser("david", "user").block();
    }

    @Test
    public void testAddRole() {
        // Ajouter un nouveau rôle à un utilisateur
        assertTrue(authorizationService.addRoleForUser("david", "user").block());
        assertTrue(authorizationService.getRolesForUser("david").collectList().block().contains("user"));
    }

    @Test
    public void testAddPolicy() {
        // Supprimer d'abord la politique si elle existe
        authorizationService.removePolicy("moderator", "/api/posts", "DELETE").block();

        // Ajouter une nouvelle politique
        assertTrue(authorizationService.addPolicy("moderator", "/api/posts", "DELETE").block());
        assertTrue(authorizationService.hasPermission("moderator", "/api/posts", "DELETE").block());
    }
}