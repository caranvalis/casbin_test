package com.casbin.casbin_test.application.ports.input;

import reactor.core.publisher.Mono;

public interface AuthorizationUseCase {
    Mono<Boolean> isAuthorized(String subject, String object, String action);
    Mono<Boolean> addPolicy(String subject, String object, String action);
    Mono<Boolean> removePolicy(String subject, String object, String action);
}
