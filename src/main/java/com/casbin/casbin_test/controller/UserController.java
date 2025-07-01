package com.casbin.casbin_test.controller;

import com.casbin.casbin_test.service.AuthorizationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthorizationService authorizationService;

    public UserController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/{user}/roles")
    public Flux<String> getUserRoles(@PathVariable String user) {
        return authorizationService.getRolesForUser(user);
    }

    @PostMapping("/{user}/roles/{role}")
    public Mono<Boolean> addRoleToUser(@PathVariable String user, @PathVariable String role) {
        return authorizationService.addRoleForUser(user, role);
    }

    @DeleteMapping("/{user}/roles/{role}")
    public Mono<Boolean> removeRoleFromUser(@PathVariable String user, @PathVariable String role) {
        return authorizationService.deleteRoleForUser(user, role);
    }

    @GetMapping("/{user}/permissions/{resource}/{action}")
    public Mono<Boolean> checkPermission(@PathVariable String user,
                                        @PathVariable String resource,
                                        @PathVariable String action) {
        return authorizationService.hasPermission(user, resource, action);
    }
}