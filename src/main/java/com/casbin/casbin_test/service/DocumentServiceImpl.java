package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.Document;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final Map<String, Document> documents = new ConcurrentHashMap<>();

    @Override
    public Flux<Document> findAll() {
        return Flux.fromIterable(documents.values());
    }

    @Override
    public Mono<Document> findById(String id) {
        return Mono.justOrEmpty(documents.get(id));
    }

    @Override
    public Mono<Document> save(Document document) {
        if (document.getId() == null || document.getId().isEmpty()) {
            document.setId(UUID.randomUUID().toString());
        }
        documents.put(document.getId(), document);
        return Mono.just(document);
    }

    @Override
    public Mono<Boolean> shareDocument(String documentId, String userId, String permission) {
        return findById(documentId)
            .flatMap(document -> {
                // Initialisation de la map de partage si elle n'existe pas
                if (document.getSharedWith() == null) {
                    document.setSharedWith(new HashMap<>());
                }

                // Ajouter l'utilisateur avec la permission spécifiée
                document.getSharedWith().put(userId, permission);

                // Sauvegarder les modifications
                return update(document).map(updatedDoc -> true);
            })
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Document non trouvé avec l'ID: " + documentId)));
    }

    @Override
    public Mono<Document> update(Document document) {
        if (document.getId() == null || !documents.containsKey(document.getId())) {
            return Mono.error(new IllegalArgumentException("Document introuvable pour la mise à jour"));
        }
        documents.put(document.getId(), document);
        return Mono.just(document);
    }

    @Override
    public Flux<Document> searchByKeyword(String keyword) {
        return Flux.fromStream(documents.values().stream()
            .filter(doc -> doc.getTitle().contains(keyword) ||
                (doc.getContent() != null && doc.getContent().contains(keyword))));
    }

    @Override
    public Flux<Document> findByCategory(String categoryId) {
        return Flux.fromStream(documents.values().stream()
            .filter(doc -> categoryId.equals(doc.getCategoryId())));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        documents.remove(id);
        return Mono.empty();
    }
}