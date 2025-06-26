package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.User;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class AuthorizationService {

    @Autowired
    private Enforcer enforcer;

    @Autowired
    @Qualifier("abacEnforcer")
    private Enforcer abacEnforcer;

    public Mono<Boolean> hasPermission(String user, String resource, String action) {
        return Mono.fromCallable(() -> enforcer.enforce(user, resource, action))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> addPolicy(String user, String resource, String action) {
        return Mono.fromCallable(() -> enforcer.addPolicy(user, resource, action))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> removePolicy(String user, String resource, String action) {
        return Mono.fromCallable(() -> enforcer.removePolicy(user, resource, action))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> addRoleForUser(String user, String role) {
        return Mono.fromCallable(() -> enforcer.addRoleForUser(user, role))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> deleteRoleForUser(String user, String role) {
        return Mono.fromCallable(() -> enforcer.deleteRoleForUser(user, role))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<String> getRolesForUser(String user) {
        return Mono.fromCallable(() -> enforcer.getRolesForUser(user))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<String> getUsersForRole(String role) {
        return Mono.fromCallable(() -> enforcer.getUsersForRole(role))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> hasPermissionAbac(User user, String resource, String action) {
        return Mono.fromCallable(() -> abacEnforcer.enforce(user, resource, action))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public boolean isAllowed(String bob, String id, String read) {
        return enforcer.enforce(bob, id, read);
    }
}