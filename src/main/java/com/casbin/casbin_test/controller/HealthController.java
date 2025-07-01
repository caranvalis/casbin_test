package com.casbin.casbin_test.controller;

import com.casbin.casbin_test.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthController {

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/test-db")
    public Mono<String> testDatabase() {
        return documentRepository.count()
            .map(count -> "Connexion OK - " + count + " documents")
            .onErrorReturn("Erreur de connexion à la BD");
    }
}