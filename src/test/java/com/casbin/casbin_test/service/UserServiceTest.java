package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    void testCreateUser() {
        // Créer un utilisateur
        User newUser = new User();
        newUser.setName("testUser"); // Utiliser setName() directement au lieu de setUsername()

        // Sauvegarder l'utilisateur (méthode synchrone)
        User createdUser = userService.save(newUser);

        // Vérifications
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals("testUser", createdUser.getName());
    }

    @Test
    void testGetUserById() {
        // Créer et sauvegarder un utilisateur
        User newUser = new User();
        newUser.setName("testUser"); // Utiliser setName() directement
        User createdUser = userService.save(newUser);

        // Récupérer l'utilisateur par ID (méthode réactive)
        StepVerifier.create(userService.findById(createdUser.getId()))
            .assertNext(response -> {
                assertTrue(response.getStatusCode().is2xxSuccessful());
                User foundUser = response.getBody();
                assertNotNull(foundUser);
                assertEquals(createdUser.getId(), foundUser.getId());
            })
            .verifyComplete();
    }

    @Test
    void testGetNonExistentUser() {
        // Test avec un ID qui n'existe pas
        StepVerifier.create(userService.findById("nonexistent"))
            .assertNext(response -> {
                assertFalse(response.getStatusCode().is2xxSuccessful());
            })
            .verifyComplete();
    }

    @Test
    void testUpdateUser() {
        // Créer et sauvegarder un utilisateur
        User newUser = new User();
        newUser.setName("originalName"); // Utiliser setName() directement
        User createdUser = userService.save(newUser);

        // Modifier l'utilisateur
        createdUser.setName("updatedName"); // Utiliser setName() directement au lieu de setUsername()
        User updatedUser = userService.update(createdUser);

        // Vérifications
        assertNotNull(updatedUser);
        assertEquals("updatedName", updatedUser.getName());
    }

    @Test
    void testDeleteUser() {
        // Créer et sauvegarder un utilisateur
        User newUser = new User();
        newUser.setName("userToDelete"); // Utiliser setName() directement
        User createdUser = userService.save(newUser);

        // Supprimer l'utilisateur
        userService.deleteById(createdUser.getId());

        // Vérifier que l'utilisateur a bien été supprimé
        StepVerifier.create(userService.findById(createdUser.getId()))
            .assertNext(response -> {
                assertFalse(response.getStatusCode().is2xxSuccessful());
            })
            .verifyComplete();
    }
}