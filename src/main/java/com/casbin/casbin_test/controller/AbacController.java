package com.casbin.casbin_test.controller;

import com.casbin.casbin_test.model.User;
import com.casbin.casbin_test.service.AuthorizationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/abac")
public class AbacController {

    private final AuthorizationService authorizationService;

    public AbacController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/check")
    public Mono<Boolean> checkAbacPermission(@RequestBody User user,
                                           @RequestParam String resource,
                                           @RequestParam String action) {
        return authorizationService.hasPermissionAbac(user, resource, action);
    }

    @PostMapping("/evaluate")
    public Mono<String> evaluateAbacPolicy(@RequestBody User user,
                                          @RequestParam String resource,
                                          @RequestParam String action) {
        return authorizationService.hasPermissionAbac(user, resource, action)
                .map(hasPermission -> hasPermission ? "GRANTED" : "DENIED");
    }
}