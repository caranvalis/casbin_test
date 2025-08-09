package com.casbin.casbin_test.serviceTest;

import com.casbin.casbin_test.domain.model.User;
import com.casbin.casbin_test.application.ports.input.UserUseCase;
import com.casbin.casbin_test.application.ports.output.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserUseCase userUseCase; // Utiliser le port d'entrée au lieu du service

    @MockBean
    private UserRepository userRepository;

    @Test
    void testCreateUser() {
        User newUser = new User();
        newUser.setUsername("testUser");

        User savedUser = new User();
        savedUser.setId("123");
        savedUser.setUsername("testUser");

        when(userRepository.save(any(User.class))).thenReturn(Mono.just(savedUser));

        StepVerifier.create(userUseCase.createUser(newUser)) // Méthode createUser au lieu de save
                .assertNext(createdUser -> {
                    assertNotNull(createdUser);
                    assertNotNull(createdUser.getId());
                    assertEquals("testUser", createdUser.getUsername());
                })
                .verifyComplete();
    }

    @Test
    void testGetUserById() {
        User foundUser = new User();
        foundUser.setId("123");
        foundUser.setUsername("testUser");

        when(userRepository.findById("123")).thenReturn(Mono.just(foundUser));

        StepVerifier.create(userUseCase.getUserById("123")) // Méthode getUserById au lieu de findById
                .assertNext(user -> {
                    assertNotNull(user);
                    assertEquals("123", user.getId());
                    assertEquals("testUser", user.getUsername());
                })
                .verifyComplete();
    }

    @Test
    void testDeleteUser() {
        when(userRepository.deleteById("123")).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.deleteUser("123")) // Méthode deleteUser au lieu de deleteById
                .verifyComplete();
    }
}