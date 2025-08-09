package com.casbin.casbin_test.application.ports.output;

import com.casbin.casbin_test.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User user);
    Mono<User> findById(String id);
    Mono<Void> deleteById(String id);

    Flux<User> findAll();
}
