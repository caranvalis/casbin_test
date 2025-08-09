package com.casbin.casbin_test.infrastructure.persistence.mapper;

import com.casbin.casbin_test.domain.model.User;
import com.casbin.casbin_test.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        // Ajoutez d'autres propriétés selon votre modèle User
        return user;
    }

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        // Ajoutez d'autres propriétés selon votre modèle User
        return entity;
    }
}
