package com.casbin.casbin_test.controller;

import com.casbin.casbin_test.model.User;
import com.casbin.casbin_test.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "ABAC", description = "Gestion des autorisations basées sur les attributs")
@RestController
@RequestMapping("/api/abac")
public class AbacController {

    @Autowired
    private AuthorizationService authorizationService;

    @Operation(summary = "Vérifie l'accès d'un utilisateur avec attributs")
    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAccess(
            @RequestParam String name,
            @RequestParam boolean isAdmin,
            @RequestParam String resource,
            @RequestParam String action) {

        User user = new User(name, isAdmin);
        boolean hasAccess = authorizationService.hasPermissionAbac(user, resource, action);

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("resource", resource);
        response.put("action", action);
        response.put("hasAccess", hasAccess);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Exemple d'accès administrateur")
    @GetMapping("/admin-example")
    public ResponseEntity<Map<String, Object>> adminExample() {
        User admin = new User("superadmin", true);
        User regularUser = new User("regularuser", false);

        Map<String, Object> response = new HashMap<>();

        // Un admin devrait avoir accès à toutes les ressources en théorie
        response.put("admin-data1-read", authorizationService.hasPermissionAbac(admin, "data1", "read"));
        response.put("admin-data2-write", authorizationService.hasPermissionAbac(admin, "data2", "write"));
        response.put("admin-data3-delete", authorizationService.hasPermissionAbac(admin, "data3", "delete"));

        // Un utilisateur régulier devrait suivre les règles normalement mais bonnnnnn
        response.put("regular-data1-read", authorizationService.hasPermissionAbac(regularUser, "data1", "read"));
        response.put("regular-data2-write", authorizationService.hasPermissionAbac(regularUser, "data2", "write"));
        response.put("regular-data3-delete", authorizationService.hasPermissionAbac(regularUser, "data3", "delete"));

        return ResponseEntity.ok(response);
    }
}
