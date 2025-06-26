package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.Document;
import com.casbin.casbin_test.model.User;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AbacAuthorizationService {
    private final Enforcer enforcer;

    @Autowired
    public AbacAuthorizationService(Enforcer enforcer) {
        this.enforcer = enforcer;
    }

    public Mono<Boolean> canEditDocument(User user, Document document) {
        return Mono.fromCallable(() -> enforcer.enforce(user, document, "edit"))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> checkPermission(User user, Document document, String action) {
        return Mono.fromCallable(() -> enforcer.enforce(user, document, action))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> canShareDocument(User currentUser, Document document) {
        return Mono.fromCallable(() -> enforcer.enforce(currentUser, document, "share"))
                .subscribeOn(Schedulers.boundedElastic());
    }
}