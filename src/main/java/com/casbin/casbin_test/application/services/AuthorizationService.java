package com.casbin.casbin_test.application.services;

import com.casbin.casbin_test.domain.model.User;
import io.micrometer.core.instrument.MeterRegistry;
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

    @Autowired
    private MeterRegistry meterRegistry;

    private static final String METRIC_PREFIX = "casbin_";

    public Mono<Boolean> hasPermission(String user, String resource, String action) {
        return Mono.fromCallable(() -> enforcer.enforce(user, resource, action))
                .doOnSuccess(result -> {
                    meterRegistry.counter(METRIC_PREFIX + "authorization",
                                    "user", user,
                                    "resource", resource,
                                    "action", action,
                                    "authorized", result.toString())
                            .increment();
                })
                .doOnNext(result -> System.out.println("Permission check: " + user + ", " + resource + ", " + action + " = " + result))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> addPolicy(String user, String resource, String action) {
        return Mono.fromCallable(() -> enforcer.addPolicy(user, resource, action))
                .doOnSuccess(result -> {
                    meterRegistry.counter(METRIC_PREFIX + "policy_add",
                                    "user", user,
                                    "resource", resource,
                                    "action", action,
                                    "success", result.toString())
                            .increment();
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> removePolicy(String user, String resource, String action) {
        return Mono.fromCallable(() -> enforcer.removePolicy(user, resource, action))
                .doOnSuccess(result -> {
                    meterRegistry.counter(METRIC_PREFIX + "policy_remove",
                                    "user", user,
                                    "resource", resource,
                                    "action", action,
                                    "success", result.toString())
                            .increment();
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> addRoleForUser(String user, String role) {
        return Mono.fromCallable(() -> {
                    if (enforcer.hasRoleForUser(user, role)) {
                        return true;
                    }
                    return enforcer.addRoleForUser(user, role);
                })
                .doOnSuccess(result -> {
                    meterRegistry.counter(METRIC_PREFIX + "role_add",
                                    "user", user,
                                    "role", role,
                                    "success", result.toString())
                            .increment();
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> deleteRoleForUser(String user, String role) {
        return Mono.fromCallable(() -> enforcer.deleteRoleForUser(user, role))
                .doOnSuccess(result -> {
                    meterRegistry.counter(METRIC_PREFIX + "role_remove",
                                    "user", user,
                                    "role", role,
                                    "success", result.toString())
                            .increment();
                })
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

    public void initializePolicies() {
        // Politiques pour le rôle admin
        addPolicy("admin", "/api/users", "GET").block();
        addPolicy("admin", "/api/users", "POST").block();
        addPolicy("admin", "/api/users", "PUT").block();
        addPolicy("admin", "/api/users", "DELETE").block();

        // Politiques pour le rôle user
        addPolicy("user", "/api/users", "GET").block();
        addPolicy("user", "/api/profile", "GET").block();
        addPolicy("user", "/api/profile", "PUT").block();

        // Ajouter une politique pour l'accès public à /actuator/prometheus
        addPolicy("public", "/actuator/prometheus", "GET").block();
        addPolicy("public", "/api/documents", "GET").block();
    }

    public Mono<Boolean> isAuthorized(String subject, String object, String action) {
        return hasPermission(subject, object, action);
    }

    public Mono<Boolean> addPolicy(List<String> params) {
        if (params == null || params.size() < 3) {
            return Mono.just(false);
        }
        return addPolicy(params.get(0), params.get(1), params.get(2));
    }

    public Mono<Boolean> removePolicy(List<String> params) {
        if (params == null || params.size() < 3) {
            return Mono.just(false);
        }
        return removePolicy(params.get(0), params.get(1), params.get(2));
    }

    public Mono<List<List<String>>> getPolicies() {
        return Mono.fromCallable(() -> enforcer.getPolicy())
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> clearPolicy() {
        return Mono.fromRunnable(() -> enforcer.clearPolicy())
                .then()
                .subscribeOn(Schedulers.boundedElastic());
    }
}