package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.User;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> save(User user);
    Flux<User> findAll();
    Mono<ResponseEntity<User>> findById(String id);
    Mono<User> update(User user);
    Mono<Void> deleteById(String id);
}