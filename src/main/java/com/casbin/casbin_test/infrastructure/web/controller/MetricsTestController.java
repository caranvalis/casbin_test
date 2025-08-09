package com.casbin.casbin_test.infrastructure.web.controller;

import com.casbin.casbin_test.application.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/test")
public class MetricsTestController {

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/generate")
    public Mono<String> generateMetrics() {
        return authorizationService.hasPermission("admin", "/api/users", "GET")
                .then(authorizationService.hasPermission("user", "/api/restricted", "POST"))
                .then(authorizationService.addPolicy("guest", "/api/public", "GET"))
                .then(authorizationService.addRoleForUser("john", "admin"))
                .thenReturn("Métriques générées avec succès");
    }
}