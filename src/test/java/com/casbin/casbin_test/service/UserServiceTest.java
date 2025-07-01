package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.User;
import com.casbin.casbin_test.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

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

        StepVerifier.create(userService.save(newUser))
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

        StepVerifier.create(userService.findById("123"))
                .assertNext(response -> {
                    assertTrue(response.getStatusCode().is2xxSuccessful());
                    User user = response.getBody();
                    assertNotNull(user);
                    assertEquals("testUser", user.getUsername());
                })
                .verifyComplete();
    }

    @Test
    void testDeleteUser() {
        when(userRepository.deleteById("123")).thenReturn(Mono.empty());

        StepVerifier.create(userService.deleteById("123"))
                .verifyComplete();
    }
}