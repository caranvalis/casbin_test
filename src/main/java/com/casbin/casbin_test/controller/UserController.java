package com.casbin.casbin_test.controller;

import com.casbin.casbin_test.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs, rôles et permissions")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private AuthorizationService authorizationService;

    @Operation(summary = "Liste les utilisateurs")
    @GetMapping("/users")
    public ResponseEntity<String> getUsers() {
        return ResponseEntity.ok("Liste des utilisateurs");
    }

    @Operation(summary = "Crée un utilisateur")
    @PostMapping("/users")
    public ResponseEntity<String> createUser() {
        return ResponseEntity.ok("Utilisateur créé");
    }

    @Operation(summary = "Récupère le profil utilisateur")
    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("Profil utilisateur");
    }

    @Operation(summary = "Ajoute une permission à un utilisateur (admin seulement)")
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
    @GetMapping("/roles/{user}")
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
