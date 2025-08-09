package com.casbin.casbin_test.infrastructure.persistence.repository;

import com.casbin.casbin_test.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SpringDataR2dbcUserRepository extends ReactiveCrudRepository<UserEntity, String> {
    Mono<UserEntity> findByUsername(String username);
}
