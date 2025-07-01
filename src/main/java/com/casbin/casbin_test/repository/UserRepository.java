package com.casbin.casbin_test.repository;

import com.casbin.casbin_test.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, String> {
    Mono<User> findByUsername(String username);
    Mono<User> findByEmail(String email);
}