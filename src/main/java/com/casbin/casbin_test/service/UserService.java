package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(String id);
    User save(User user);
    User Update(User user);

    User update(User user);

    void deleteById(String id);
}