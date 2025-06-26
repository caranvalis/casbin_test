package com.casbin.casbin_test.controller;

import com.casbin.casbin_test.model.Document;
import com.casbin.casbin_test.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Documents", description = "Gestion des documents")
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Operation(summary = "Liste tous les documents")
    @GetMapping
    public Flux<Document> getAllDocuments() {
        return documentService.findAll(); // Utilisation directe du Flux
    }

    @Operation(summary = "Récupère un document par ID")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Document>> getDocumentById(@PathVariable String id) {
        return documentService.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crée un document")
    @PostMapping
    public Mono<ResponseEntity<Document>> createDocument(@RequestBody Mono<Document> documentMono) {
        return documentMono
            .flatMap(documentService::save)
            .map(savedDoc -> ResponseEntity.status(HttpStatus.CREATED).body(savedDoc));
    }

    @Operation(summary = "Met à jour un document")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Document>> updateDocument(@PathVariable String id, @RequestBody Mono<Document> documentMono) {
        return documentMono
            .flatMap(doc -> {
                doc.setId(id);
                return documentService.update(doc);
            })
            .map(ResponseEntity::ok)
            .onErrorResume(IllegalArgumentException.class, e ->
                Mono.just(ResponseEntity.notFound().build()));
    }

    @Operation(summary = "Supprime un document")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDocument(@PathVariable String id) {
        return documentService.deleteById(id)
            .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @Operation(summary = "Recherche des documents par mot-clé")
    @GetMapping("/search")
    public Flux<Document> searchDocuments(@RequestParam String keyword) {
        return documentService.searchByKeyword(keyword); // Utilisation directe du Flux
    }

    @Operation(summary = "Liste les documents par catégorie")
    @GetMapping("/category/{categoryId}")
    public Flux<Document> getDocumentsByCategory(@PathVariable String categoryId) {
        return documentService.findByCategory(categoryId); // Utilisation directe du Flux
    }
}