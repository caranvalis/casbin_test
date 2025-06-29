package com.casbin.casbin_test.controller;

import com.casbin.casbin_test.model.User;
import com.casbin.casbin_test.service.AuthorizationService;
import com.casbin.casbin_test.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs, rôles et permissions")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthorizationService authorizationService;

    @Autowired
    public UserController(UserService userService, AuthorizationService authorizationService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    @Operation(summary = "Liste tous les utilisateurs")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "Récupère un utilisateur par ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Crée un utilisateur")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @Operation(summary = "Met à jour un utilisateur")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        user.setId(id);
        try {
            return ResponseEntity.ok(userService.update(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprime un utilisateur")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupère le profil utilisateur")
    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("Profil utilisateur");
    }

    @Operation(summary = "Ajoute une permission à un utilisateur")
    @PostMapping("/permissions")
    public ResponseEntity<String> addPermission(
            @RequestParam String user,
            @RequestParam String resource,
            @RequestParam String action) {
        boolean added = authorizationService.addPolicy(user, resource, action);
        return ResponseEntity.ok("Permission " + (added ? "ajoutée" : "déjà existante"));
    }

    @Operation(summary = "Ajoute un rôle à un utilisateur")
    @PostMapping("/roles")
    public ResponseEntity<String> addRole(
            @RequestParam String user,
            @RequestParam String role) {
        boolean added = authorizationService.addRoleForUser(user, role);
        return ResponseEntity.ok("Rôle " + (added ? "ajouté" : "déjà existant"));
    }

    @Operation(summary = "Liste les rôles d'un utilisateur")
    @GetMapping("/{user}/roles")
    public ResponseEntity<List<String>> getUserRoles(@PathVariable String user) {
        List<String> roles = authorizationService.getRolesForUser(user);
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Liste les admins")
    @GetMapping("/admins")
    public ResponseEntity<List<String>> getAdmins() {
        List<String> admins = authorizationService.getUsersForRole("admin");
        return ResponseEntity.ok(admins);
    }
}