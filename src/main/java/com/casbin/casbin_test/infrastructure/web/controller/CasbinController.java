package com.casbin.casbin_test.infrastructure.web.controller;

import com.casbin.casbin_test.application.services.AuthorizationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/casbin")
public class CasbinController {

    private final AuthorizationService authorizationService;

    public CasbinController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/policy/add")
    public Mono<Boolean> addPolicy(@RequestBody Map<String, Object> request) {
        List<String> params = (List<String>) request.get("params");
        return authorizationService.addPolicy(params);
    }

    @GetMapping("/policies")
    public Mono<List<List<String>>> getPolicies() {
        return authorizationService.getPolicies();
    }

    @PostMapping("/enforce")
    public Mono<Boolean> enforce(@RequestBody Map<String, String> request) {
        String sub = request.get("sub");
        String obj = request.get("obj");
        String act = request.get("act");
        return authorizationService.isAuthorized(sub, obj, act);
    }
    @PostMapping("/initialize")
    public Mono<String> initializePolicies() {
        authorizationService.initializePolicies();
        return Mono.just("Politiques Casbin initialisées avec succès");
    }

    @PostMapping("/policy/delete")
    public Mono<Boolean> deletePolicy(@RequestBody Map<String, Object> request) {
        List<String> params = (List<String>) request.get("params");
        return authorizationService.removePolicy(params);
    }

    @PostMapping("/policy/clear")
    public Mono<Void> clearPolicy() {
        return authorizationService.clearPolicy();
    }

    @GetMapping("/debug")
    public Mono<Map<String, Object>> debugCasbin() {
        return authorizationService.getPolicies()
                .map(policies -> Map.of("policies", policies));
    }
}