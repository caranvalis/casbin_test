package com.casbin.casbin_test.service;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class CasbinService {

    private final Enforcer enforcer;

    public CasbinService(Enforcer enforcer) {
        this.enforcer = enforcer;
    }

    public Mono<Boolean> checkPermission(String subject, String object, String action) {
        return Mono.fromCallable(() -> enforcer.enforce(subject, object, action));
    }

    public Mono<Boolean> addPolicy(List<String> params) {
        String[] stringArray = params.toArray(new String[0]);
        return Mono.fromCallable(() -> enforcer.addPolicy(stringArray));
    }

    public Mono<Boolean> removePolicy(List<String> params) {
        String[] stringArray = params.toArray(new String[0]);
        return Mono.fromCallable(() -> enforcer.removePolicy(stringArray));
    }

    public Mono<List<List<String>>> getPolicies() {
        return Mono.fromCallable(() -> enforcer.getPolicy());
    }

    public Mono<Void> clearPolicy() {
        return Mono.fromRunnable(() -> enforcer.clearPolicy());
    }
}