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
        assertTrue(authorizationService.hasPermission("alice", "/api/users", "GET"));
        assertTrue(authorizationService.hasPermission("alice", "/api/users", "POST"));
        assertTrue(authorizationService.hasPermission("alice", "/api/users", "PUT"));
        assertTrue(authorizationService.hasPermission("alice", "/api/users", "DELETE"));
    }

    @Test
    public void testUserPermissions() {
        // Bob est user et ne devrait avoir accès qu'à GET sur /api/users
        assertTrue(authorizationService.hasPermission("bob", "/api/users", "GET"));
        assertFalse(authorizationService.hasPermission("bob", "/api/users", "POST"));
        assertFalse(authorizationService.hasPermission("bob", "/api/users", "DELETE"));
        // Mais il peut accéder à son profil
        assertTrue(authorizationService.hasPermission("bob", "/api/profile", "GET"));
        assertTrue(authorizationService.hasPermission("bob", "/api/profile", "PUT"));
    }

    @Test
    public void testAddRole() {
        // Ajouter un nouveau rôle à un utilisateur
        assertTrue(authorizationService.addRoleForUser("david", "user"));
        assertTrue(authorizationService.getRolesForUser("david").contains("user"));
    }

    @Test
    public void testAddPolicy() {
        // Ajouter une nouvelle politique
        assertTrue(authorizationService.addPolicy("moderator", "/api/posts", "DELETE"));
        assertTrue(authorizationService.hasPermission("moderator", "/api/posts", "DELETE"));
    }
}
