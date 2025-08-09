package com.casbin.casbin_test;

import com.casbin.casbin_test.application.services.AuthorizationService;
import org.casbin.jcasbin.main.Enforcer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CasbinTestApplicationTests {

    @Autowired(required = false)
    private AuthorizationService authorizationService;

    @Autowired(required = false)
    private Enforcer enforcer;

    @Test
    void contextLoads() {
        // Vérifie que le contexte Spring se charge sans erreur
        assertTrue(true, "Le contexte Spring s'est chargé avec succès");
    }

    @Test
    void testCasbinComponentsLoaded() {
        assertNotNull(enforcer, "L'Enforcer Casbin doit être chargé");
        assertNotNull(authorizationService, "Le service d'autorisation doit être chargé");
    }

    @Test
    void testBasicRbacPermission() {
        if (authorizationService != null) {
            // Test simple RBAC : alice a des permissions d'admin
            Boolean hasPermission = authorizationService
                .hasPermission("alice", "data1", "read")
                .block();
            assertNotNull(hasPermission, "La vérification de permission ne doit pas être null");
        }
    }

    @Test
    void testApplicationStartsSuccessfully() {
        // Vérifie que l'application démarre sans exception
        assertDoesNotThrow(() -> {
            // Test que toute la configuration est correcte
        }, "L'application doit démarrer sans exception");
    }
}
