package com.casbin.casbin_test.infrastructure.persistence.adapter;

import com.casbin.casbin_test.application.ports.output.UserRepository;
import com.casbin.casbin_test.domain.model.User;
import com.casbin.casbin_test.infrastructure.persistence.entity.UserEntity;
import com.casbin.casbin_test.infrastructure.persistence.mapper.UserMapper;
import com.casbin.casbin_test.infrastructure.persistence.repository.SpringDataR2dbcUserRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class R2dbcUserRepository implements UserRepository {

    private final SpringDataR2dbcUserRepository repository;
    private final UserMapper mapper;

    public R2dbcUserRepository(SpringDataR2dbcUserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<User> save(User user) {
        return repository.save(mapper.toEntity(user))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<User> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<User> findAll() {
        return repository.findAll()
                .map(mapper::toDomain);
    }
}
