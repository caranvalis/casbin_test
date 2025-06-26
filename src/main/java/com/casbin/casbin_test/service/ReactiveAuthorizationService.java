package com.casbin.casbin_test.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ReactiveAuthorizationService {

    private final AuthorizationService authorizationService;

    public ReactiveAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public Mono<Boolean> addPolicy(String user, String resource, String action) {
        return authorizationService.addPolicy(user, resource, action)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> addRoleForUser(String user, String role) {
        return authorizationService.addRoleForUser(user, role)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<String> getRolesForUser(String user) {
        return authorizationService.getRolesForUser(user)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<String> getUsersForRole(String role) {
        return authorizationService.getUsersForRole(role)
                .subscribeOn(Schedulers.boundedElastic());
    }
}