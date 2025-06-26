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
    public void testAdminPermissions() {
        // Alice est admin et devrait avoir accès à toutes les opérations sur /api/users
        assertTrue(authorizationService.hasPermission("alice", "/api/users", "GET").block());
        assertTrue(authorizationService.hasPermission("alice", "/api/users", "POST").block());
        assertTrue(authorizationService.hasPermission("alice", "/api/users", "PUT").block());
        assertTrue(authorizationService.hasPermission("alice", "/api/users", "DELETE").block());
    }

    @Test
    public void testUserPermissions() {
        // Bob est user et ne devrait avoir accès qu'à GET sur /api/users
        assertTrue(authorizationService.hasPermission("bob", "/api/users", "GET").block());
        assertFalse(authorizationService.hasPermission("bob", "/api/users", "POST").block());
        assertFalse(authorizationService.hasPermission("bob", "/api/users", "DELETE").block());
        // Mais il peut accéder à son profil
        assertTrue(authorizationService.hasPermission("bob", "/api/profile", "GET").block());
        assertTrue(authorizationService.hasPermission("bob", "/api/profile", "PUT").block());
    }

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