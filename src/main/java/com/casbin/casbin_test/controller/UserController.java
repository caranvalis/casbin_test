package com.casbin.casbin_test.controller;

import com.casbin.casbin_test.model.User;
import com.casbin.casbin_test.service.AuthorizationService;
import com.casbin.casbin_test.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;

    public UserController(AuthorizationService authorizationService, UserRepository userRepository) {
        this.authorizationService = authorizationService;
        this.userRepository = userRepository;
    }

    // Ajouter un utilisateur
    @PostMapping
    public Mono<User> createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // Récupérer tous les utilisateurs
    @GetMapping
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Récupérer les rôles d’un utilisateur
    @GetMapping("/{user}/roles")
    public Flux<String> getUserRoles(@PathVariable String user) {
        return authorizationService.getRolesForUser(user);
    }

    // Ajouter un rôle à un utilisateur
    @PostMapping("/{user}/roles/{role}")
    public Mono<Boolean> addRoleToUser(@PathVariable String user, @PathVariable String role) {
        return authorizationService.addRoleForUser(user, role);
    }

    // Supprimer un rôle à un utilisateur
    @DeleteMapping("/{user}/roles/{role}")
    public Mono<Boolean> removeRoleFromUser(@PathVariable String user, @PathVariable String role) {
        return authorizationService.deleteRoleForUser(user, role);
    }

    // Vérifier une permission
    @GetMapping("/{user}/permissions/{resource}/{action}")
    public Mono<Boolean> checkPermission(@PathVariable String user,
                                         @PathVariable String resource,
                                         @PathVariable String action) {
        return authorizationService.hasPermission(user, resource, action);
    }
}
