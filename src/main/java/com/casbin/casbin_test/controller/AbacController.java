package com.casbin.casbin_test.controller;

import com.casbin.casbin_test.model.User;
import com.casbin.casbin_test.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
   public Mono<ResponseEntity<Map<String, Object>>> checkAccess(
           @RequestParam String name,
           @RequestParam boolean isAdmin,
           @RequestParam String resource,
           @RequestParam String action) {

       User user = new User(name, isAdmin);

       return authorizationService.hasPermissionAbac(user, resource, action)
               .map(hasAccess -> {
                   Map<String, Object> response = new HashMap<>();
                   response.put("user", user);
                   response.put("resource", resource);
                   response.put("action", action);
                   response.put("hasAccess", hasAccess);

                   return ResponseEntity.ok(response);
               });
   }

   @Operation(summary = "Exemple d'accès administrateur")
   @GetMapping("/admin-example")
   public Mono<ResponseEntity<Map<String, Object>>> adminExample() {
       User admin = new User("superadmin", true);
       User regularUser = new User("regularuser", false);

       Map<String, Object> response = new HashMap<>();

       return Mono.zip(
               authorizationService.hasPermissionAbac(admin, "data1", "read"),
               authorizationService.hasPermissionAbac(admin, "data2", "write"),
               authorizationService.hasPermissionAbac(admin, "data3", "delete"),
               authorizationService.hasPermissionAbac(regularUser, "data1", "read"),
               authorizationService.hasPermissionAbac(regularUser, "data2", "write"),
               authorizationService.hasPermissionAbac(regularUser, "data3", "delete")
       ).map(tuple -> {
           response.put("admin-data1-read", tuple.getT1());
           response.put("admin-data2-write", tuple.getT2());
           response.put("admin-data3-delete", tuple.getT3());
           response.put("regular-data1-read", tuple.getT4());
           response.put("regular-data2-write", tuple.getT5());
           response.put("regular-data3-delete", tuple.getT6());

           return ResponseEntity.ok(response);
       });
   }
}