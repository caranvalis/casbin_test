package com.casbin.casbin_test.controller;

import com.casbin.casbin_test.model.Document;
import com.casbin.casbin_test.service.AuthorizationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final AuthorizationService authorizationService;

    public DocumentController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public Flux<Document> getAllDocuments() {
        // Implémentation réactive à adapter selon votre service
        return Flux.empty(); // Placeholder
    }

    @GetMapping("/{id}")
    public Mono<Document> getDocument(@PathVariable String id) {
        // Implémentation réactive à adapter selon votre service
        return Mono.empty(); // Placeholder
    }

    @PostMapping
    public Mono<Document> createDocument(@RequestBody Document document) {
        // Implémentation réactive à adapter selon votre service
        return Mono.just(document); // Placeholder
    }

    @PutMapping("/{id}")
    public Mono<Document> updateDocument(@PathVariable String id, @RequestBody Document document) {
        // Implémentation réactive à adapter selon votre service
        return Mono.just(document); // Placeholder
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteDocument(@PathVariable String id) {
        // Implémentation réactive à adapter selon votre service
        return Mono.empty(); // Placeholder
    }

    @GetMapping("/{id}/permissions/{user}")
    public Mono<Boolean> checkDocumentPermission(@PathVariable String id,
                                                @PathVariable String user,
                                                @RequestParam String action) {
        return authorizationService.hasPermission(user, "/api/documents/" + id, action);
    }
}