package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public abstract class UserService {
    @Autowired
    public abstract List<User> findAll();

    public abstract Mono<ResponseEntity<User>> findById(String id);

    public abstract User save(User user);

    public abstract User update(User user);

    public abstract void deleteById(String id);

    public abstract User Update(User user);
}