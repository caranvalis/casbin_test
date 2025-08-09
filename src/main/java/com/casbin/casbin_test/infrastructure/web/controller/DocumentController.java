package com.casbin.casbin_test.infrastructure.web.controller;

import com.casbin.casbin_test.domain.model.Document;
import com.casbin.casbin_test.application.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Base64;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public CorePublisher<Document> getAllDocuments(ServerWebExchange exchange) {
        String username = extractUsername(exchange);
        System.out.println("Requête GET documents pour l'utilisateur: " + username);

        // Pour les utilisateurs avec privilèges, retourner tous les documents
        if ("admin".equalsIgnoreCase(username) || "Zaim".equalsIgnoreCase(username)) {
            return documentService.findAll();
        }
        
        return documentService.findById(username);
    }

    @GetMapping("/{id}")
    public Mono<Document> getDocument(@PathVariable String id, ServerWebExchange exchange) {
        String username = extractUsername(exchange);
        System.out.println("Requête GET document " + id + " pour l'utilisateur: " + username);

        return documentService.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(doc -> {
                    // Les admins et Zaim peuvent voir tous les documents
                    if ("admin".equalsIgnoreCase(username) || "Zaim".equalsIgnoreCase(username)) {
                        return Mono.just(doc);
                    }

                    // Les autres utilisateurs ne peuvent voir que leurs propres documents
                    if (username.equalsIgnoreCase(doc.getOwner()) || doc.isSharedWith(username)) {
                        return Mono.just(doc);
                    }

                    return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN));
                });
    }

    @PostMapping
    public Mono<Document> createDocument(@RequestBody Document document, ServerWebExchange exchange) {
        String username = extractUsername(exchange);
        // Définir le propriétaire comme l'utilisateur authentifié
        document.setOwner(username);
        return documentService.save(document);
    }

    @PutMapping("/{id}")
    public Mono<Document> updateDocument(
            @PathVariable String id,
            @RequestBody Document document,
            ServerWebExchange exchange) {
        String username = extractUsername(exchange);
        return documentService.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(existingDoc -> {
                    if (!username.equalsIgnoreCase(existingDoc.getOwner())
                            && !"admin".equalsIgnoreCase(username)
                            && !"Zaim".equalsIgnoreCase(username)) {
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN));
                    }
                    document.setId(id);
                    document.setOwner(existingDoc.getOwner());
                    return documentService.save(document);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteDocument(@PathVariable String id, ServerWebExchange exchange) {
        String username = extractUsername(exchange);
        return documentService.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(doc -> {
                    if (username.equalsIgnoreCase(doc.getOwner())
                            || "admin".equalsIgnoreCase(username)
                            || "Zaim".equalsIgnoreCase(username)) {
                        return documentService.deleteById(id);
                    }
                    return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN));
                });
    }

    private String extractUsername(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            try {
                String base64Credentials = authHeader.substring("Basic ".length());
                String credentials = new String(Base64.getDecoder().decode(base64Credentials));
                return credentials.split(":", 2)[0]; // Extraire le nom d'utilisateur
            } catch (Exception e) {
                System.err.println("Erreur de décodage Basic Auth: " + e.getMessage());
            }
        }
        return "default_user";
    }
}