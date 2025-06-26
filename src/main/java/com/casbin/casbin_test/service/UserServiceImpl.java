package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// ici c'est un service d'authenfication qui permet de recuper l'id
@Service
public class UserServiceImpl extends UserService {

    // pour le stockage des users
    private final Map<String, User> users = new ConcurrentHashMap<>();


    public UserServiceImpl() {
        super();
        // Créer un administrateur
        User admin = new User();
        admin.setId("admin");
        admin.setUsername("admin"); // Utiliser setUsername au lieu de setName
        admin.setEmail("admin@example.com"); // Ajout de l'email nécessaire
        admin.setRole("admin");
        users.put(admin.getId(), admin);

        // Créer un utilisateur standard
        User user = new User();
        user.setId("user1");
        user.setUsername("utilisateur"); // Utiliser setUsername au lieu de setName
        user.setEmail("user@example.com"); // Ajout de l'email nécessaire
        user.setRole("user");
        users.put(user.getId(), user);
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Mono<ResponseEntity<User>> findById(String id) {
        User user = users.get(id);
        if (user != null) {
            return Mono.just(ResponseEntity.ok(user));
        } else {
            return Mono.just(ResponseEntity.notFound().build());
        }
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