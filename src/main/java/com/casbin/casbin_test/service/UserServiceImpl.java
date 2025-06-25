package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// ici c'est un service d'authenfication qui permet de recuper l'id
@Service
public class UserServiceImpl implements UserService {

    // pour le stockage des users
    private final Map<String, User> users = new ConcurrentHashMap<>();


    public UserServiceImpl() {
        // Créer un administrateur
        User admin = new User();
        admin.setId("admin");
        admin.setUsername("admin");
        admin.setRole("admin");
        users.put(admin.getId(), admin);

        // Créer un utilisateur standard
        User user = new User();
        user.setId("user1");
        user.setUsername("utilisateur");
        user.setRole("user");
        users.put(user.getId(), user);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(String id) {
        return users.get(id);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Utilisateur introuvable pour la mise à jour");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(String id) {
        users.remove(id);
    }
    @Override
    public User Update(User user) {
        // Déléguer à la méthode existante update() pour éviter la duplication de code
        return this.update(user);
    }
}