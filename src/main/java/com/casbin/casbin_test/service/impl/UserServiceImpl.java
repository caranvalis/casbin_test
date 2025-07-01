package com.casbin.casbin_test.service.impl;

import com.casbin.casbin_test.model.User;
import com.casbin.casbin_test.repository.UserRepository;
import com.casbin.casbin_test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<ResponseEntity<User>> findById(String id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<User> update(User user) {
        return userRepository.existsById(user.getId())
                .flatMap(exists -> exists ? userRepository.save(user) : Mono.empty());
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }
}