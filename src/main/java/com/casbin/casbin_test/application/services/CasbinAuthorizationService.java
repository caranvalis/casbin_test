package com.casbin.casbin_test.application.services;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.List;

@Service
public class CasbinAuthorizationService extends AuthorizationService {

    private final Enforcer enforcer;

    public CasbinAuthorizationService(Enforcer enforcer) {
        this.enforcer = enforcer;
    }

    @Override
    public Mono<Boolean> isAuthorized(String subject, String object, String action) {
        return Mono.fromCallable(() -> enforcer.enforce(subject, object, action))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Boolean> addPolicy(List<String> params) {
        String[] stringArray = params.toArray(new String[0]);
        return Mono.fromCallable(() -> enforcer.addPolicy(stringArray))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Boolean> removePolicy(List<String> params) {
        String[] stringArray = params.toArray(new String[0]);
        return Mono.fromCallable(() -> enforcer.removePolicy(stringArray))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<List<List<String>>> getPolicies() {
        return Mono.fromCallable(() -> enforcer.getPolicy())
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> clearPolicy() {
        return Mono.fromRunnable(() -> enforcer.clearPolicy())
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
