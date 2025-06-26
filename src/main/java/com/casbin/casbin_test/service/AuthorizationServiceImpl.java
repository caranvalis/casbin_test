package com.casbin.casbin_test.service;

import org.casbin.jcasbin.main.Enforcer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AuthorizationServiceImpl extends AuthorizationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

    private final Enforcer enforcer;

    @Autowired
    public AuthorizationServiceImpl(Enforcer enforcer) {
        this.enforcer = enforcer;
    }

    @Override
    public Mono<Boolean> hasPermission(String user, String resource, String action) {
        return Mono.fromCallable(() -> {
            boolean result = enforcer.enforce(user, resource, action);
            logger.debug("Authorization check: user={}, resource={}, action={}, result={}",
                    user, resource, action, result);
            return result;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}