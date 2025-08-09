package com.casbin.casbin_test.application.ports.input;

import com.casbin.casbin_test.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserUseCase {
    Mono<User> createUser(User user);
    Mono<User> getUserById(String id);
    Mono<Void> deleteUser(String id);
}